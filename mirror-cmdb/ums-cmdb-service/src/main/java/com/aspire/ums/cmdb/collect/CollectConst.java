package com.aspire.ums.cmdb.collect;

import java.util.HashMap;
import java.util.Map;

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
     * 固定采集的字段
     */
    //主机ID
    public final static String IP_FILED = "ip";
    //设备ID
    public final static String DEVICE_IP_FILED = "DEVICE_IP";
    //资源池名称
    public final static String IDC_FILED = "idcType";
    //所属机房
    public final static String ROOM_ID_FILED = "roomId";
    //所属域
    public final static String DEVICE_REGION_FILED = "DEVICE_REGION";
    //业务系统
    public final static String BIZ_SYSTEM_FILED = "bizSystem";

    /**
     * 采集频率
     */
    public final static String COLLECT_FREQUENCY_HIGH = "高";
    public final static String COLLECT_FREQUENCY_MID = "中";
    public final static String COLLECT_FREQUENCY_LOW = "低";

    /**
     * 采集状态
     */
    public final static String COLLECT_STATUS_SUCCESS = "成功";
    public final static String COLLECT_STATUS_FAIL = "失败";
    public final static String COLLECT_STATUS_EXECUTE = "采集中";

    public final static String TIME_MINUTE = "分钟";
    public final static String TIME_HOUR = "小时";
    public final static String TIME_DAY = "天";

    /**
     * 采集方式
     */
    public final static String COLLECT_TYPE_AUTO = "自动采集";
    public final static String COLLECT_TYPE_BY_USER = "手工采集";

    public final static String WHETHER_YES = "是";
    public final static String WHETHER_NO = "否";

    /**
     * 新发现数据状态
     */
    public final static String PENDING = "待处理"; // 待处理
    public final static String BIND = "已绑定"; // 已绑定
    public final static String MAINTAINED = "已维护";// 已维护
    public final static String SHIELD = "已屏蔽"; // 已屏蔽

    public static Map<String, String> EMPLOYEE = new HashMap<String, String>() {
        {
            put("系统维护人员", "wang@osa.com");
            put("设备管理员", "wang@osa.com");
            put("系统使用人员", "wang@osa.com");
        }
    };

    public static Map<String, String> ALARMLAVEL = new HashMap<String, String>() {
        {
            put("1", "提示");
            put("2", "低");
            put("3", "中");
            put("4", "高");
            put("5", "严重");
        }
    };
}
