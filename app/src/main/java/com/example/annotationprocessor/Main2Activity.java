package com.example.annotationprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.zrouter_annotation.ZRouter;

@ZRouter(path = "main/activity2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
