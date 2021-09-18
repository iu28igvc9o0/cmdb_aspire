package com.aspire.mirror.alert.server.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: AlertCommonConstant
 * @projectName mirror-alert
 * @description: TODO
 * @author baiwp
 * @date 2019/5/1611:05
 */
public class AlertCommonConstant {
    
    public final static class NUM {
        public final static String ZERO = "0";
        public final static String ONE = "1";
        public final static String TWO = "2";
    }
    
    public final static String MONITER_OBJECT_TYPE = "moniter_object";
    
    public final static String MONITER_OBJECT_TYPE_CPU = "cpuMoniterItem";
    
    public final static String MONITER_OBJECT_TYPE_MEMORY = "memoryMoniterItem";
    
    //public final static String[] IDCTYPE_STATISTICS = {"东莞资源池"};
    
    
    public final static String DICTYPE_DEFAULT = "dicType_default";
    //网络设备不统计的监控对象
    public final static String NETWORK_DEVICETYPE = "network_moniter_object";
    //物理设备device_type
    public final static String PHYSICSERVER_DEVICETYPE = "physicServer_moniter_object";
    //物理设备统计的 告警来源是zabbix的监控对象
    public final static String PHYSICSERVER_ZABBIX = "physicServer_zabbix";
    
    public final static String PHYSICSERVER_PROMETHEUS = "physicServer_prometheus";
    
    public final static String OTHER_DEVICETYPE = "other";
    
    
    public final static String PHYSICSERVER_SOURCE_ZABBIX = "physicServer_source_zabbix";
    
    public final static String PHYSICSERVER_SOURCE_PROMETHEUS = "physicServer_source_prometheus";
    
    public final static String[] ALERT_LEVELS = {"重大告警","高告警","中告警","低告警"};
    
    public final static String[] DEVICE_TYPE_CONFIGS = {"SDN控制器","磁带库","磁盘阵列","防火墙","X86服务器","负载均衡","交换机","路由器","分布式块存储"};

    public final static String CLEAR_ALERT_CONTENT_BYORDER = "工单已关闭，系统自动清除告警";

    //public static String RG_ONG_URL = "http://%s:28177/restconf/operations/platnotify:%s";
    
    public static final String RG_ONC_SOURCE = "RG_ONC"; 
    public final static class DEVICE_TYPE_CONFIG {
        public final static String physicalMachine = "物理机";
        public final static String firewall = "防火墙";
        public final static String router = "路由器";
        public final static String switchNode = "交换机";
        public final static String diskArray = "磁盘阵列";
        public final static String tapeLibrary = "磁带库";
        public final static String cloudStorage = "云存储";
        public final static String SDNController = "SDN控制器";
    }

    public final static Map<String, String> alertLevelMap = new HashMap<String, String>(){{
//        put("1","提示");
        put("2","低");
        put("3","中");
        put("4","高");
        put("5","重大");
    }};

    public final static String CABINET_COLUMN_SOURCE = "下电告警";
    public final static String CABINET_ALERT_TITLE = "疑似机柜下电告警";
    public final static String CABINETCOLUMN_ALERT_TITLE = "疑似列头柜下电告警";
    public final static String CABINET_POWER_ITEM = "cabinet_column_power_item";
    public final static String CABINET_ITEM = "cabinet_alert_info";
    public final static String CABINET_COLUMN_ITEM = "cabinet_column_alert_info";
    public final static String CABINET_COLUMN_MONIOBJECT = "电源传感器";
    

}
