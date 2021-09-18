package com.aspire.mirror.log.server.constant;

/**
 * 错误码常量类 Project Name:log-service File Name:ErrorCodeConstant.java Package
 * Name:com.aspire.mirror.log.server.constant ClassName: ErrorCodeConstant <br/>
 * date: 2017年12月14日 下午5:08:22 <br/>
 * 错误码常量类
 * 
 * @author jiangfuyi
 * @version 1.0
 * @since JDK 1.8
 */

public class ErrorCodeConstant {
    /* 日志错误码 */
    // 日志统计数量错误码
    public static final String COUNT_ERROR_CODE = "1040003016";
    // 日志查询错误码
    public static final String LIST_ERROR_CODE = "1040003017";
    // 日志分组错误码
    public static final String GROUP_ERROR_CODE = "1040003018";
    // 日志直方图错误码
    public static final String HISTOGRAM_ERROR_CODE = "1040003029";

    /* 公用方法 */
    // 校验index是否存在错误码
    public static final String CHECK_INDEX_EXIST_ERROR_CODE = "1040003019";
    // 创建索引或备份索引错误码
    public static final String CREATEORBAK_INDEX_ERROR_CODE = "1040003013";
    // 解压错误码
    public static final String UNZIP_ERROR_CODE = "1040003011";
    // 运行错误码
    public static final String RUN_ERROR_CODE = "1040003012";

    /* 事件方法 */
    // 事件统计数量错误码
    public static final String COUNT_EVENT_ERROR_CODE = "1040002032";
    // 事件查询错误码
    public static final String LIST_EVENT_ERROR_CODE = "1040002033";
    // 新增事件错误
    public static final String ADD_ERROR_CODE = "1040002011";
    // 创建索引错误
    public static final String CREATE_INDEX_ERROR_CODE = "1040002021";
    // 查询出错
    public static final String QUERY_ERROR_CODE = "1040002031";

    /* 消费者错误码 */
    // 关闭消费者线程池错误
    public static final String SHUTDOWM_POOL_ERROR_CODE = "1040003015";
    //创建线程池错误
    public static final String CREATE_POOL_ERROR_CODE = "1040003020";

    /* 消费者错误码 */
    // 业务日志保存错误代码
    public static final String BIZ_BUILD_ERROR_CODE = "1040003014";
    // 创建dsl错误码
    public static final String LOG_CREATE_DSL_ERROR_CODE = "1040003010";
    //业务日志过长错误码
    public static final String BIZ_LOG_TOO_LARGE_ERROR = "1040003011";
    public static final String LOG_INSERT_ES_INFO = "1040003021";
    public static final String BIZ_LOG_SAVE_ERROR = "1040003022";
}
