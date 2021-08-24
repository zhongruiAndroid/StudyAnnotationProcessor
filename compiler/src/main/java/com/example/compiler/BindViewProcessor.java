package com.example.compiler;

import com.example.annotation.BindView;
import com.google.auto.service.AutoService;
import com.sun.xml.internal.ws.api.message.Message;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(BindView.class)
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
        if(set==null||set.isEmpty()){
            return false;
        }
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        if(elementsAnnotatedWith==null){
            return false;
        }
        for (Element element:elementsAnnotatedWith){
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            Name simpleName = enclosingElement.getSimpleName();
            Name qualifiedName = enclosingElement.getQualifiedName();
            messager.printMessage(Diagnostic.Kind.NOTE,"simpleName:"+simpleName);
            messager.printMessage(Diagnostic.Kind.NOTE,"qualifiedName:"+qualifiedName);
            messager.printMessage(Diagnostic.Kind.NOTE,"==============");

            TypeMirror typeMirror = element.asType();
            if (typeMirror.getKind() == TypeKind.TYPEVAR) {
                TypeVariable typeVariable = (TypeVariable) typeMirror;
                typeMirror = typeVariable.getUpperBound();
            }

        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


























}
