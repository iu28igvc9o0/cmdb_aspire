package com.aspire.mirror.log.server.constant;

/**
* 日志公用类
* Project Name:log-service
* File Name:LogConstant.java
* Package Name:com.aspire.mirror.log.server.constant
* ClassName: LogConstant <br/>
* date: 2017年9月4日 下午4:46:35 <br/>
* @author jiangfuyi
* @version 1.0
* @since JDK 1.8
 */
public class LogConstant {
    /**
     * 默认时间
     */
    public static final int DEFAULT_START_DATE = -6;
    /**
     *  日志直方图柱子个数
     */
    public static final int HISTOGRAM_COUNT = 30;
    /**
     * 日志查询log_type列名
     */
    public static final String COL_LOG_TYPE = "log_type";
    /**
     * 日志查询time列名
     */
    public static final String COL_LOG_TIME = "time";
    /**
     * 日志查询namespace列名
     */
    public static final String COL_LOG_NAMESPACE = "namespace";
    /**
     * 日志查询region_name列名
     */
    public static final String COL_REGION_NAME = "region_name";
    /**
     * 日志查询service_name列名
     */
    public static final String COL_SERVICE_NAME = "service_name";
    /**
     * 日志查询instance_id8列名
     */
    public static final String COL_INSTANCE_ID8 = "instance_id8";

}
