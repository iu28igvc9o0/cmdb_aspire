package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant;
/**
*  一句话描述类
* Project Name:log-service
* File Name:LogConstant.java
* Package Name:com.migu.tsg.microservice.monitor.log.constant
* ClassName: LogConstant <br/>
* date: 2017年9月4日 下午4:46:35 <br/>
*  详细描述这个类的功能等
* @author jiangfuyi
* @version 1.0
* @since JDK 1.8
 */
public class LogConstant {
	/**
	 * 默认时间
	 */
	public static final int DEFAULT_START_DATE = -7;
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
	public static final String COL_INSTANCE_ID8 = "instance_id";
	/**
     * 日志查询app_id列名
     */
    public static final String COL_APP_ID = "app_id";
    /**
     * 日志查询machine列名
     */
    public static final String COL_MACHINE = "machine";
    /**
     * 日志查询paths列名
     */
    public static final String COL_PATHS = "filename";
    /**
     * 日志查询biz_id列名
     */
    public static final String COL_BIZ_ID = "biz_id";
    /**
     * 日志查询log_level列名
     */
    public static final String COL_LOG_LEVEL = "biz_level";
    /**
     * 日志查询QueryString
     */
    public static final String COL_QUERY_STRING = "query_string";
    /**
     * 业务日志查询日志级别 info
     */
    public static final String LOG_LEVEL_INFO = "info";
    /**
     * 业务日志查询日志级别 error
     */
    public static final String LOG_LEVEL_ERROR = "error";

}
