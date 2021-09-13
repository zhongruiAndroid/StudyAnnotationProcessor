package com.example.zrouter;


import com.example.zrouter_annotation.RouterBean;
import com.example.zrouter_annotation.ZRouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.example.zrouter_annotation.ZRouter"})
public class ZRouterProcessor extends AbstractProcessor {
    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    private Map<String, Set<RouterBean>> routerPathMap = new HashMap();
    private Map<String, String> groupClassPathMap = new HashMap<String, String>();

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();


    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 一旦没有类使用@ARouter注解
        if (isEmpty(set)) {
            return false;
        }
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(ZRouter.class);
        if (elementsAnnotatedWith == null || elementsAnnotatedWith.isEmpty()) {
            return false;
        }
        try {

            TypeMirror typeMirror = elementUtils.getTypeElement(Constant.ACTIVITY).asType();
            groupClassPathMap.clear();


            for (Element element : elementsAnnotatedWith) {
                /*用于判断是否是Activity的注解*/
                TypeMirror elementTypeMirror = element.asType();
                if (!typeUtils.isSubtype(elementTypeMirror, typeMirror)) {
                    continue;
                }
                ZRouter annotation = element.getAnnotation(ZRouter.class);
                String path = annotation.path();
                if (isEmpty(path) || !path.contains("/")) {
                    continue;
                }
                String group;
                if (path.startsWith("/")) {
                    group = path.substring(1, path.indexOf("/", 1));
                } else {
                    group = path.substring(0, path.indexOf("/"));
                }
                RouterBean routerBean = new RouterBean(null, path, group);
                routerBean.setElement(element);

                Set<RouterBean> routerBeans = routerPathMap.get(group);
                if (routerBeans == null) {
                    routerBeans = new HashSet<>();
                    routerBeans.add(routerBean);
                    routerPathMap.put(group, routerBeans);

                    groupClassPathMap.put(group, Constant.NAME_OF_PATH + group);
                } else {
                    routerBeans.add(routerBean);
                }
            }
            createPathFile();

            createGroupFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createPathFile() throws IOException {
        ParameterizedTypeName methodSpecMethodSpec = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(RouterBean.class));


       /* implements IRouterLoad{
            @Override
            public Map<String, RouterBean> loadRouter() {
                Map<String,RouterBean>map=new HashMap<String,RouterBean>();
                map.put("order/OrderActivity",new RouterBean(null,"order/OrderActivity","order"));
                map.put("order/OrderActivity1",new RouterBean(null,"order/OrderActivity1","order"));
                return map;
            }*/

        for (Map.Entry<String, Set<RouterBean>> item : routerPathMap.entrySet()) {
            if (item == null) {
                continue;
            }
            //public Map<String, RouterBean> loadRouter() {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(Constant.PATH_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodSpecMethodSpec);

            //Map<String,RouterBean>map=new HashMap<String,RouterBean>();
            methodSpec.addStatement("$T<$T,$T> map=new $T<>()",
                    Map.class,
                    String.class,
                    RouterBean.class,
                    HashMap.class
            );


            String group = item.getKey();
            String classPath = groupClassPathMap.get(group);
            for (RouterBean bean : item.getValue()) {
                if (bean == null) {
                    continue;
                }
                methodSpec.addStatement("map.put($S,$T.create($T.class,$S,$S))",
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup()
                );
            }
            methodSpec.addStatement("return map");

            TypeSpec.Builder builder = TypeSpec.classBuilder(classPath)
                    .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.IRouterLoad)))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec.build());

            JavaFile.builder(Constant.PACKAGE_OF_GENERATE_FILE, builder.build()).build().writeTo(filer);

        }
    }

    private void createGroupFile() throws IOException {

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(elementUtils.getTypeElement(Constant.IRouterLoad))));
        ParameterizedTypeName methodSpecMethodSpec = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), parameterizedTypeName);


        //Map<String,Class<? extends IRouterLoad>> loadGroup();
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(Constant.GROUP_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodSpecMethodSpec);

        //Map<String, Class<? extends IRouterLoad>> map=new HashMap<>();
        methodSpec.addStatement("$T<$T,$T> map=new $T<>()",
                Map.class,
                String.class,
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(elementUtils.getTypeElement(Constant.IRouterLoad)))),
                HashMap.class
        );

        for (Map.Entry<String, Set<RouterBean>> item : routerPathMap.entrySet()) {
            if (item == null) {
                continue;
            }

            String group = item.getKey();
            String classPath = groupClassPathMap.get(group);
            methodSpec.addStatement("map.put($S,$T.class)", group, ClassName.get(Constant.PACKAGE_OF_GENERATE_FILE, classPath));


        }
        methodSpec.addStatement("return map");
        TypeSpec.Builder builder = TypeSpec.classBuilder(Constant.NAME_OF_GROUP + "Module")
                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constant.IGroupLoad)))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec.build());
        JavaFile.builder(Constant.PACKAGE_OF_GENERATE_FILE, builder.build()).build().writeTo(filer);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
