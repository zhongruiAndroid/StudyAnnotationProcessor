package com.example.zrouter_api;

import java.util.HashMap;
import java.util.Map;

public class ZRouter$$Group$$AA implements IGroupLoad {
    @Override
    public Map<String, Class<? extends IRouterLoad>> loadGroup() {
        Map<String, Class<? extends IRouterLoad>> map=new HashMap<>();
        map.put("order",ZRouter$$Path$$AA.class);
        return map;
    }
}
