package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant;

/**
 * 项目名称: 咪咕微服务运营平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant
 * 类名称: SmsConstant.java 类描述: 创建人: GaoYang 创建时间: 2017/12/18 18:41 版本: v1.0
 */
public class SmsConstant {

    /**
     * 默认查询页
     */
    public static final int DEFAULT_PAGE = 1;
    /**
     * 默认每页条数
     */
    public static final int DEFAULT_SIZE = 10;
    /**
     * 默认时间
     */
    public static final int DEFAULT_START_DATE = -7;

    /**
     * 手机号
     */
    public static final String COL_ADDRESSES = "addresses";
    /**
     * 短信业务类型
     */
    public static final String COL_BIZ_TYPE = "bizType";
    /**
     * 一级系统
     */
    public static final String COL_FIRST_SYS = "firstSys";
    /**
     * 二级系统
     */
    public static final String COL_SECOND_SYS = "secondSys";
    /**
     * 时间
     */
    public static final String COL_LOG_TIME = "time";
    /**
     * 短信内容
     */
    public static final String COL_MESSAGE = "message";
    /**
     * 开始时间
     */
    public static final String COL_START_TIME = "startTime";
    /**
     * 结束时间
     */
    public static final String COL_END_TIME = "endTime";
}