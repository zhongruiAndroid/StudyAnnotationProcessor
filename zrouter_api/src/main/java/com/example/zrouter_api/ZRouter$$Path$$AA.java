package com.example.zrouter_api;

import com.example.zrouter_annotation.RouterBean;

import java.util.HashMap;
import java.util.Map;

public class ZRouter$$Path$$AA implements IRouterLoad{
    @Override
    public Map<String, RouterBean> loadRouter() {
        Map<String,RouterBean>map=new HashMap<String,RouterBean>();
        map.put("order/OrderActivity",new RouterBean(null,"order/OrderActivity","order"));
        map.put("order/OrderActivity1",new RouterBean(null,"order/OrderActivity1","order"));
        return map;
    }
}
