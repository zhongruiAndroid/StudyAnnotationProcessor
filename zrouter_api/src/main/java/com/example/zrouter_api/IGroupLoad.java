package com.example.zrouter_api;

import java.util.Map;

public interface IGroupLoad {
    Map<String,Class<? extends IRouterLoad>> loadGroup();

}
