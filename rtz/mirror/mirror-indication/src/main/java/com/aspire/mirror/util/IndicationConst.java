package com.aspire.mirror.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.common.IndicationConst
 */
public class IndicationConst {
    public static final String PROCESS_STATUS_PROCESSING = "PROCESSING";
    public static final String PROCESS_STATUS_FINISH = "FINISH";
    public static final Integer RE_CALC_DAYS = 3;

    /**
     * 指标所属系统
     */
    public static final String INDICATION_OWNER_ALL = "全量";
    public static final String INDICATION_OWNER_REAL = "双送";

    /**
     * 指标分类
     */
    public static final String CATALOG_BOX = "机顶盒";
    public static final String CATALOG_GATEWAY = "网关";
    /**
     * 指标颗粒度
     */
    public static final String INDICATION_CYCLE_MONTH = "月";
    public static final String INDICATION_CYCLE_DAY = "日";
    public static final String INDICATION_CYCLE_HOUR = "小时";
    public static final String INDICATION_CYCLE_MINUTE = "分钟";

    /**
     * 指标计算频率
     */
    public static final String INDICATION_FREQUENCY_MONTH = "月";
    public static final String INDICATION_FREQUENCY_DAY = "日";
    public static final String INDICATION_FREQUENCY_HOUR = "小时";
    public static final String INDICATION_FREQUENCY_ACTUAL = "实时";
    public static final String INDICATION_FREQUENCY_MINUTE = "分钟";

    /**
     * 指标分组
     */
    public static final String INDICATION_GROUP_PROVINCE = "各省";
    public static final String INDICATION_GROUP_COUNTRY = "全国";

    /**
     * 指标项的计算单位
     */
    public static final String INDICATION_ITEM_UNIT_TEN_THOUSAND = "万";
    public static final String INDICATION_ITEM_UNIT_PERCENT = "%";
    public static final String INDICATION_ITEM_UNIT_S = "s";
    public static final String INDICATION_ITEM_UNIT_MS = "ms";

    /**
     * 固定的指标项名称
     */
    public static final String INDICATION_ITEM_NAME_PROVINCE = "省份";
    public static final String INDICATION_ITEM_NAME_CALC_DATE = "日期";
    public static final String INDICATION_ITEM_NAME_SEVEN_AVG = "7日均值";
    public static final String INDICATION_ITEM_NAME_PREV_VALUE = "前一天数据";
    public static final String INDICATION_ITEM_NAME_EVERY_CHANGE = "变动值";
    public static final String INDICATION_ITEM_NAME_EVERY_CHANGE_RATE = "变动率";
    public static final String INDICATION_ITEM_NAME_EXCEPTION_REASON = "异常原因";
    public static final String INDICATION_ITEM_NAME_PREV_DAY_HOUR_VALUE = "前一天此时间段数据";
    public static final String INDICATION_ITEM_NAME_EVERY_HOUR_CHANGE = "逐小时变动值";
    public static final String INDICATION_ITEM_NAME_EVERY_HOUR_CHANGE_RATE = "逐小时变动率";

    /**
     * 日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String QUERY_MONGODB_DATE_PATTERN = "yyyyMMdd";
    public static final String QUERY_MONGODB_DATE_PATTERN_0 = "yyyyMMddHH";
    public static final String QUERY_MONGODB_DATE_PATTERN_1 = "yyyyMMddHHmm";
    public static final String QUERY_MONGODB_DATE_PATTERN_2 = "yyyyMMddHHmmss";
    public static final String QUERY_MONGODB_DATE_PATTERN_3 = "yyyy-MM-dd HH:mm";
    public static final String DATE_PATTERN_00 = "yyyyMMddHHmm00";
    public static final String DATE_PATTERN_CN = "yyyy年MM月dd日HH时mm分";

    /**
     * 调用SDK接口 type类型
     */
    public static final String QUERY_MONGODB_TYPE_COUNTRY="datetime";
    public static final String QUERY_MONGODB_TYPE_PROVINCE="provinceCode";
    public static final String QUERY_MONGODB_TYPE_ALL="all";

    public static final String COUNTRY_PROVINCE_CODE = "000"; //全国 省份的编码值
    public static final String EXCEPTION_GROUP_PROVINCE_EXCEPTION = "分省异常";
    public static final String EXCEPTION_GROUP_PROVINCE_ALL = "分省全部";
    public static final String EXCEPTION_GROUP_PROVINCE_COUNTRY = "全国14天";

    /**
     * 省份邮件标题模板
     */
    public static String MAIL_PROVINCE_TITLE_TEMPLATE = "软探针系统通知：%s公司%s一级全量平台%s指标出现异常，请及时处理。";
    /**
     * 省份邮件内容模板
     */
    public static String MAIL_PROVINCE_CONTENT_TEMPLATE = "<p>%s公司同事：</p>" +
            "<p style='text-indent: 2em; margin-top: 20px;'>您好！贵公司%s软探针全量采集平台%s指标数据出现异常，请尽快登录软探针一级全量平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";
    /**
     * 集团邮件标题模板
     */
    public static String MAIL_COUNTRY_TITLE_TEMPLATE = "软探针系统通知：%s软探针全量采集平台%s指标数据出现异常，请及时处理。";
    /**
     * 集团邮件内容模板
     */
    public static String MAIL_COUNTRY_CONTENT_TEMPLATE = "<p style='text-indent: 2em; margin-top: 20px;'> " +
            "您好! %s软探针全量采集平台%s指标数据出现异常，请尽快登录软探针一级全量平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";

    /**
     * 省份邮件标题模板
     */
    public static String REAL_MAIL_PROVINCE_TITLE_TEMPLATE = "软探针系统通知：%s公司%s一级双送平台%s指标出现异常，请及时处理。";
    /**
     * 省份邮件内容模板
     */
    public static String REAL_MAIL_PROVINCE_CONTENT_TEMPLATE = "%s公司同事：</p>" +
            "<p style='text-indent: 2em; margin-top: 20px;'>您好！贵公司%s软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";


    /**
     * 小时/实时指标集团邮件标题模板
     */
    public static String REAL_HOUR_MAIL_COUNTRY_TITLE_TEMPLATE = "软探针系统通知：%s检测到软探针双送平台%s指标数据出现异常，请及时处理。";
    /**
     * 小时/实时指标集团邮件内容模板
     */
    public static String REAL_HOUR_MAIL_COUNTRY_CONTENT_TEMPLATE = "集团领导：</p><p style='text-indent: 2em; margin-top: 20px;'> " +
            "您好! %s检测到软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";

    /**
     * 小时/实时指标省份邮件标题模板
     */
    public static String REAL_HOUR_MAIL_PROVINCE_TITLE_TEMPLATE = "%s公司%s检测到一级双送平台%s指标出现异常，请及时处理。";
    /**
     * 小时/实时指标省份邮件内容模板
     */
    public static String REAL_HOUR_MAIL_PROVINCE_CONTENT_TEMPLATE = "%s公司同事：</p>" +
            "<p style='text-indent: 2em; margin-top: 20px;'>您好！贵公司%s检测到软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";


    /**
     * 集团邮件标题模板
     */
    public static String REAL_MAIL_COUNTRY_TITLE_TEMPLATE = "软探针系统通知：%s软探针双送平台%s指标数据出现异常，请及时处理。";
    /**
     * 集团邮件内容模板
     */
    public static String REAL_MAIL_COUNTRY_CONTENT_TEMPLATE = "集团领导：</p><p style='text-indent: 2em; margin-top: 20px;'> " +
            "您好! %s软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";
    
    //分钟级卡顿告警
    public static String  KADUN_MINUTE_COUNTRY_TITLE_TEMPLATE = "软探针系统通知，%s年%s月%s日%s时%s分软探针双送平台机顶盒卡顿告警出现异常，请及时处理。";
    
    /**
     * 分钟级卡顿内容模板
     */
    public static String KADUN_MINUTE_COUNTRY_CONTENT_TEMPLATE = "集团领导： </p><p style='text-indent: 2em; margin-top: 20px;'>"
    		+ "您好! %s年%s月%s日%s时%s分软探针双送平台机顶盒卡顿告警指标出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";
    
    //分钟级卡顿告警
    public static String  KADUN_MINUTE_COUNTRY_RESTORE_TITLE_TEMPLATE = "软探针系统通知，%s年%s月%s日%s时%s分软探针双送平台机顶盒卡顿告警异常已恢复";
    
    /**
     * 分钟级卡顿内容模板
     */
    public static String KADUN_MINUTE_COUNTRY_RESTORE_CONTENT_TEMPLATE = "集团领导： <p style='text-indent: 2em; margin-top: 20px;'>"
    		+ "您好! %s年%s月%s日%s时%s分软探针双送平台机顶盒卡顿告警指标异常已恢复，请知悉，谢谢！";
    
    
    public static String  KADUN_HOUR_CALCULATE_TITLE_TEMPLATE = "软探针系统通知，%s年%s月%s日%s时获取%s指标数据异常，请尽快处理";
    
    public static String  KADUN_HOUR_CALCULATE_CONTENT_TEMPLATE = "集团领导： </p><p style='text-indent: 2em; margin-top: 20px;'>"
            + "您好! %s年%s月%s日%s时从%s获取数据为空，请尽快处理。</p>";
    
    public static String  HOURVIEWER_CALCULATE_TITLE_TEMPLATE = "软探针系统通知，%s年%s月%s日%s时全量平台%s%s活跃用户数为0，请尽快处理";
    public static String  HOURVIEWER_CALCULATE_CONTENT_TEMPLATE = "集团领导： </p><p style='text-indent: 2em; margin-top: 20px;'>"
            + "您好! %s年%s月%s日%s时全量平台%s%s活跃用户数为0。</p>";

}
