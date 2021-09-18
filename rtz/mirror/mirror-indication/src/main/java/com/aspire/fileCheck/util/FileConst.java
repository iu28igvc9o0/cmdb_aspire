package com.aspire.fileCheck.util;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/27
 * 软探针异常指标监控系统
 * com.aspire.fileCheck.common.FileConst
 */
public class FileConst {
    //时间段格式化格式
    public static final String PERIOD_PATTERN = "HH:00";
    //文件上传正常延迟时间(单位小时)
    public static final Integer PERIOD_LAZY_HOUR = 2;
    //计算延迟时间(单位小时)
    public static final Integer CALC_LAZY_HOUR = 1;
    //文件上传延迟
    public static final String JOB_NAME_FILE_UPLOAD_LAZY = "FileUploadLazy";
    //文件缺失及完整性
    public static final String JOB_NAME_FILE_UPLOAD_MISSING = "FileMissingAndIntegrity";
    //非当日文件检查
    public static final String JOB_NAME_OTHER_DAYS_FILE = "FileOtherDayFile";
    public static final String JOB_NAME_SEND_MAIL = "FileExceptionSendEmail";
    //最后一次运行
    public static final Integer JOB_LATEST_RUNNING_0 = 0; //否
    public static final Integer JOB_LATEST_RUNNING_1 = 1; //是
    //JOB运行状态
    public static final String JOB_STATUS_RUNNING= "运行";
    public static final String JOB_STATUS_FINISH= "完成";

    /**
     * 省份邮件标题模板
     */
    public static String MAIL_FILE_CHECK_PROVINCE_TITLE_TEMPLATE = "软探针系统通知：%s日 %s%s软探针%s文件上传%s出现异常，请及时处理";

    public static String MAIL_FILE_CHECK_PROVINCE_CONTENT_TEMPLATE = "<p>%s公司同事：</p>" +
            "<p style='text-indent: 2em; margin-top: 20px;'>您好! %s日%s软探针%s文件上传%s出现异常，请尽快补传相关缺失数据。如邮件反馈，请群回邮件，谢谢！</p>%s";
}
