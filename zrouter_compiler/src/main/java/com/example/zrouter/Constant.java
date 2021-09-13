package com.example.zrouter;

public class Constant {
    public static final String SEPARATOR="$$$";
    public static final String PROJECT="ZRouter";
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
    public static final String NAME_OF_PATH = PROJECT + SEPARATOR + "Path" + SEPARATOR;
    public static final String PACKAGE_OF_GENERATE_FILE = "com.zr.android.arouter.routes";

    public static final String IGroupLoad = "com.example.zrouter_api.IGroupLoad";
    public static final String IRouterLoad = "com.example.zrouter_api.IRouterLoad";
    public static final String GROUP_METHOD_NAME = "loadGroup";
    public static final String PATH_METHOD_NAME = "loadRouter";

    public static final String ACTIVITY="android.app.Activity";
    public static final String CREATE_FILE_PACKAGE_NAME="com.github.zr";

    public static void main(String[] args) {
        String a="123456789123";
        System.out.println(a.indexOf("1"));
        System.out.println(a.indexOf("1",0));
        System.out.println(a.indexOf("1",1));
    }
}
