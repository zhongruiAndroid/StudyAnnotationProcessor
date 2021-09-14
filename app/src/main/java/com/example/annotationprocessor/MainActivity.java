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
import com.example.zrouter_annotation.RouterBean;
import com.example.zrouter_annotation.ZRouter;
import com.example.zrouter_api.IGroupLoad;
import com.example.zrouter_api.IRouterLoad;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import java.lang.reflect.Modifier;
import java.util.Map;

@ZRouter(path = "main/activity")
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
    private Map<String, Class<? extends IRouterLoad>> stringClassMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
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

        btTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start("main/activity2");
            }
        });
        btTest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start("order/activity3");
            }
        });
    }
    private void start(String path){
        String group;
        if (path.startsWith("/")) {
            group = path.substring(1, path.indexOf("/", 1));
        } else {
            group = path.substring(0, path.indexOf("/"));
        }
        Class<? extends IRouterLoad> aClass = stringClassMap.get(group);
        if(aClass==null){
            return;
        }
        try {
            IRouterLoad iRouterLoad = aClass.newInstance();
            Map<String, RouterBean> stringRouterBeanMap = iRouterLoad.loadRouter();
            RouterBean routerBean = stringRouterBeanMap.get(path);
            Class<?> destination = routerBean.getDestination();
            Intent intent = new Intent(getApplicationContext(),destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    private void init() {
        try {
            Class<?> aClass = Class.forName("com.zr.android.arouter.routes.ZRouter$$$Group$$$Module");
            IGroupLoad iGroupLoad = (IGroupLoad) aClass.newInstance();
            stringClassMap = iGroupLoad.loadGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
