package com.example.zrouter_api;

import com.example.zrouter_annotation.RouterBean;

import java.util.Map;

public interface IRouterLoad {
    Map<String, RouterBean> loadRouter();
}
