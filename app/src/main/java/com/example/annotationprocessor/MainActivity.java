package com.example.annotationprocessor;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.annotation.BindView;
import com.example.bindapi.ZRBindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btTest1)
    Button btTest1;


    @butterknife.BindView(R.id.btTest2)
    Button btTest2;


    @butterknife.BindView(R.id.btTest3)
    Button btTest3;


    @butterknife.BindView(R.id.btTest4)
    Button btTest4;


    @butterknife.BindView(R.id.btTest5)
    Button btTest5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZRBindView.bind(this);
        ButterKnife.bind(this);

        btTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg("ZRBindView.bind(this)生效");
            }
        });
        btTest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }
    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    public void test() {
        Unbinder bind = ButterKnife.bind(this);
        bind.unbind();
    }
/*    public   void tes2t(){
        FieldSpec build = FieldSpec.builder(String.class, "android")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", "Android")
                .build();
    }*/
}
