package com.example.zrouter_annotation;

import javax.lang.model.element.Element;

public class RouterBean {
    private Class<?> destination;   // Destination
    private String path;            // Path of route
    private String group;           // Group of route
    private Element element;

    public RouterBean(Class<?> destination, String path, String group) {
        this.destination = destination;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(Class<?> destination, String path, String group) {
        return new RouterBean(destination, path, group);
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
