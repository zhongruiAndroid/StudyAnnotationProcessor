package com.example.zrouter;


import com.example.zrouter_annotation.RouterBean;
import com.example.zrouter_annotation.ZRouter;
import com.google.auto.service.AutoService;

import java.lang.annotation.ElementType;
import java.util.Collection;
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
        if(elementsAnnotatedWith==null||elementsAnnotatedWith.isEmpty()){
            return false;
        }
        TypeMirror typeMirror = elementUtils.getTypeElement(Constant.ACTIVITY).asType();

        for (Element element:elementsAnnotatedWith){
            /*用于判断是否是Activity的注解*/
            TypeMirror elementTypeMirror = element.asType();
            if(!typeUtils.isSubtype(elementTypeMirror,typeMirror)){
                continue;
            }
            ZRouter annotation = element.getAnnotation(ZRouter.class);
            String path = annotation.path();
            if(isEmpty(path)){
                continue;
            }
            if(path.startsWith("/")){

            }
//            RouterBean routerBean=new RouterBean(path);


        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
