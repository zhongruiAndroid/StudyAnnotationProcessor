package com.example.annotationprocessor;

import android.app.Activity;
import android.view.View;

public class MainActivity_ViewBind implements Bind {
    private MainActivity activity;

    public MainActivity_ViewBind(MainActivity activity) {
        this(activity, activity.getWindow().getDecorView());
    }

    public MainActivity_ViewBind(MainActivity activity, View view) {
        this.activity = activity;
        this.activity.btTest1 = view.findViewById(R.id.btTest1);
        this.activity.btTest2 = view.findViewById(R.id.btTest2);

    }

    public void unBind() {
        this.activity.btTest1 = null;
        this.activity.btTest2 = null;
    }
}
