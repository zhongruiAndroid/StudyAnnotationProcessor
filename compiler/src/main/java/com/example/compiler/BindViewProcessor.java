package com.example.compiler;

import com.example.annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.element.ElementKind.PACKAGE;


@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.example.annotation.BindView"})
@SupportedOptions({"moduleName", "testParam"})
public class BindViewProcessor extends AbstractProcessor {
    private Elements elements;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.elements = processingEnvironment.getElementUtils();
        this.filer = processingEnvironment.getFiler();
        this.messager = processingEnvironment.getMessager();

        messager.printMessage(Diagnostic.Kind.NOTE, "moduleName=================");
        // 通过ProcessingEnvironment去获取对应的参数
        Map<String, String> options = processingEnvironment.getOptions();
        if (!(options == null || options.isEmpty())) {
            String moduleName = options.get("moduleName");
            String testParam = options.get("testParam");
            // 有坑：Diagnostic.Kind.ERROR，异常会自动结束，不像安卓中Log.e
            messager.printMessage(Diagnostic.Kind.NOTE, "moduleName >>> " + moduleName);
            messager.printMessage(Diagnostic.Kind.NOTE, "testParam >>> " + testParam);
        }
    }

    public static PackageElement getPackage(Element element) {
        while (element.getKind() != PACKAGE) {
            element = element.getEnclosingElement();
        }
        return (PackageElement) element;
    }

    /**
     * 相当于main函数，开始处理注解
     * 注解处理器的核心方法，处理具体的注解，生成Java文件
     *
     * @param set              使用了支持处理注解的节点集合
     * @param roundEnvironment 当前或是之前的运行环境,可以通过该对象查找的注解。
     * @return true 表示后续处理器不会再处理（已经处理完成）
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            return false;
        }
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        if (elementsAnnotatedWith == null) {
            return false;
        }
        Map<String, BindBean> map = new HashMap<String, BindBean>();

        for (Element element : elementsAnnotatedWith) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            Name simpleName = enclosingElement.getSimpleName();
            String qualifiedName = enclosingElement.getQualifiedName().toString();

            TypeName targetType = TypeName.get(enclosingElement.asType());

            String packageName = getPackage(element).getQualifiedName().toString();
            int value = element.getAnnotation(BindView.class).value();
            BindBean bindBean = map.get(qualifiedName);
            if (bindBean == null) {
                bindBean = new BindBean();
                bindBean.setTargetType(targetType);
                bindBean.setTypeMirror(enclosingElement.asType());
                bindBean.setPackageName(packageName);
                bindBean.setSimpleName(simpleName.toString());

                BindFieldBean bindFieldBean = new BindFieldBean(value, element.getSimpleName().toString());
                bindFieldBean.setTypeName(TypeName.get(element.asType()));
                bindBean.getList().add(bindFieldBean);
                map.put(qualifiedName, bindBean);
            } else {
                BindFieldBean bindFieldBean = new BindFieldBean(value, element.getSimpleName().toString());
                bindFieldBean.setTypeName(TypeName.get(element.asType()));
                bindBean.getList().add(bindFieldBean);
            }


            messager.printMessage(Diagnostic.Kind.NOTE, "simpleName:" + simpleName);
            messager.printMessage(Diagnostic.Kind.NOTE, "qualifiedName:" + qualifiedName);
            messager.printMessage(Diagnostic.Kind.NOTE, "==============");

            TypeMirror typeMirror = element.asType();
            if (element.getKind() == ElementKind.FIELD) {
                messager.printMessage(Diagnostic.Kind.NOTE, "=======element.getKind()=======");
            }
            if (typeMirror.getKind() == TypeKind.TYPEVAR) {
                TypeVariable typeVariable = (TypeVariable) typeMirror;
                typeMirror = typeVariable.getUpperBound();
            }


            MethodSpec act = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(targetType, "act")
                    .addStatement("this(act, act.getWindow().getDecorView())")
                    .build();
            MethodSpec act2 = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                    .addParameter(targetType, "act")
                    .addParameter(ClassName.get("android.view", "View"), "view")
//                    .addStatement()
                    .build();

            TypeSpec.classBuilder(simpleName.toString())
                    .addField(targetType, "act", Modifier.PRIVATE)
                    .addMethod(act)
                    .addMethod(act2)

            ;
            messager.printMessage(Diagnostic.Kind.NOTE, "==============");

//            FieldSpec.builder()
//            TypeSpec.classBuilder("").addField()
//            FieldSpec fieldSpec=FieldSpec.builder()
        }

        if (map.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, BindBean> item : map.entrySet()) {
            String key = item.getKey();
            BindBean value = item.getValue();
            MethodSpec unBind = MethodSpec.methodBuilder("unBind").addModifiers(Modifier.PUBLIC).build();

            MethodSpec.Builder builder = MethodSpec.constructorBuilder().addParameter(value.getTargetType(), "act").addParameter(ClassName.get("android.view", "View"), "view");
            for (BindFieldBean bean : value.getList()) {
                builder.addStatement("this.act.$L= $T.class.cast(view.findViewById($L));",bean.getFieldName(),bean.getTypeName(),bean.getValue());
            }

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(value.getSimpleName() + "_ZrBindView")
                    .addSuperinterface(ClassName.get("com.example.bindapi", "Bind"))
                    .addField(value.getTargetType(), "act")
                    .addMethod(unBind)
                    .addMethod(builder.build());
            if(Helper.isSubtypeOfType(value.getTypeMirror(),"android.app.Activity")){
                MethodSpec act = MethodSpec.constructorBuilder().addParameter(value.getTargetType(), "act").addStatement("this(act, act.getWindow().getDecorView())").build();
                typeBuilder.addMethod(act);
            }
            JavaFile javaFile = JavaFile.builder(value.getPackageName(), typeBuilder.build()).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
