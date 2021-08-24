package com.example.annotationprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.annotation.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btTest1)
    Button btTest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
