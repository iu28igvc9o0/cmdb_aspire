package com.aspire.mirror.alert.server.biz.kgEnv;


import java.util.List;
import java.util.Map;

public interface KGMonitorIndexBiz {


    /**
     * 告警总览
     */
    List<Map<String, Object>> getAlertView(Map<String,Object> param);
}
