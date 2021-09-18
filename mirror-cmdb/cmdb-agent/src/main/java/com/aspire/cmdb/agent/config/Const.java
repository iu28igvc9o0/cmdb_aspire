package com.aspire.cmdb.agent.config;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Const
 * Author:   zhu.juwang
 * Date:     2019/4/24 18:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class Const {
    /**
     * 发现访问方式 Ping
     */
    public static final String DISCOVERY_TYPE_PING = "ICMP Ping";
    /**
     * 发现访问方式 SNMP
     */
    public static final String DISCOVERY_TYPE_SNMP = "SNMP";

    /**
     * 次数(一次ping,对方返回的ping的结果的次数)
     */
    public static final Integer PING_COUNT = 5;

    /**
     * 超时时间 单位ms(ping不通,设置的此次ping结束时间)
     */
    public static final Integer PING_TIME_OUT = 5;
}
