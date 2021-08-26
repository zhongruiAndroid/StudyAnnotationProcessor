package com.example.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.TypeMirror;

public class BindBean {
    private TypeName targetType;
    private String packageName;
    private String simpleName;
    private TypeMirror typeMirror;
    private List<BindFieldBean> list = new ArrayList<>();


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public TypeName getTargetType() {
        return targetType;
    }

    public void setTargetType(TypeName targetType) {
        this.targetType = targetType;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public List<BindFieldBean> getList() {
        return list == null ? new ArrayList<BindFieldBean>() : list;
    }
}
