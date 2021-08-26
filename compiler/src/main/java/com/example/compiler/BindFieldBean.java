package com.example.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;

public class BindFieldBean {
    private int value;
    private String fieldName;
    private TypeName typeName;

    public BindFieldBean(int value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
