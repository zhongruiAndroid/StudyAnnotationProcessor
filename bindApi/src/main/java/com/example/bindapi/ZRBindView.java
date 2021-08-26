package com.example.bindapi;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ZRBindView {
    public static Bind bind(Activity activity) {
        return bind(activity, activity.getWindow().getDecorView());
    }

    public static Bind bind(Activity activity, View view) {
        Class<?> aClass = activity.getClass();
        String name = aClass.getName();
        try {
            Class<?> newClass = aClass.getClassLoader().loadClass(name + "_ZrBindView");

            //noinspection unchecked
            Constructor<? extends Bind> constructor = (Constructor<? extends Bind>) newClass.getConstructor(aClass, View.class);
            Bind bindingCtor = constructor.newInstance(activity, view);
            return bindingCtor;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
