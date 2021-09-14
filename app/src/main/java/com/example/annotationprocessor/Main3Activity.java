package com.example.annotationprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.zrouter_annotation.ZRouter;

@ZRouter(path = "order/activity3")
public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
}
