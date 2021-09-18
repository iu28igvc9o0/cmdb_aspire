package com.aspire.cmdb.agent.collect;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectConst
 * Author:   zhu.juwang
 * Date:     2019/3/14 11:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CollectConst {
    /**
     * 采集状态
     */
    public final static String COLLECT_STATUS_SUCCESS = "成功";
    public final static String COLLECT_STATUS_FAIL = "失败";
    public final static String COLLECT_STATUS_EXECUTE = "采集中";

    /**
     * 采集周期单位
     */
    public final static String TIME_HOUR = "小时";
    public final static String TIME_DAY = "天";


    /**
     * 定时扫描模型表周期
     */
    public final static Integer DEFAUL_SCAN_TIME = 2;
    //模型表名称前缀
    public static final String CMDB_MODULE_TABLE_PREFIX = "cmdb_instance_";

    public static final String ZABBIX = "zabbix";

    public static final String ZBX_KEY = "key_";

    public static final String PROMETHEUS = "promethus";

    public static final String PRO_KEY = "__name__";

    public static final String PRO_METRIC = "metric";

    public static final String COLLECT_SOURCE = "source";

    public static final String VM_MODULE_ID = "vm_module_id";

    public static final String PHY_MODULE_ID = "phy_module_id";
    public static final String INS_MODULE_ID = "cmdb_instance_module_id";

    public static final String COLLECT_DEVICE_CLASS = "device_class";

    public static final String COLLECT_DEVICE_TYPE = "device_type";

    public static final int COLLECT_SUYAN_CHANGTYPE_ADD = 1;

    public static final int COLLECT_SUYAN_CHANGTYPE_UPDATE = 2;

    public static final int COLLECT_SUYAN_CHANGTYPE_DELETE = 3;

    public static final List<Integer> COLLECT_CHANGETYPE_LIST = new ArrayList<>(Arrays.asList(COLLECT_SUYAN_CHANGTYPE_ADD, COLLECT_SUYAN_CHANGTYPE_DELETE, COLLECT_SUYAN_CHANGTYPE_UPDATE));




    /**
     * 定义一个20个线程的处理线程池
     */
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(20);
}
