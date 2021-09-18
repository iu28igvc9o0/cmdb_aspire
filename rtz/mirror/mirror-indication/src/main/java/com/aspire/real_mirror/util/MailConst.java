package com.aspire.real_mirror.util;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MailConst
 * Author:   zhu.juwang
 * Date:     2019/11/7 16:37
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class MailConst {
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
     * 日指标集团邮件标题模板
     */
    public static String REAL_MAIL_DAY_COUNTRY_TITLE_TEMPLATE = "软探针系统通知：%s检测到软探针双送平台%s相关日指标数据出现异常，请及时处理。";

    /**
     * 小时/实时指标集团邮件标题模板
     */
    public static String REAL_MAIL_COUNTRY_TITLE_TEMPLATE = "软探针系统通知：%s检测到软探针双送平台%s指标数据出现异常，请及时处理。";
    /**
     * 小时/实时指标集团邮件内容模板
     */
    public static String REAL_MAIL_COUNTRY_CONTENT_TEMPLATE = "集团领导：</p><p style='text-indent: 2em; margin-top: 20px;'> " +
            "您好! %s检测到软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";
    /**
     * 小时/实时指标省份邮件标题模板
     */
    public static String REAL_HOUR_MAIL_PROVINCE_TITLE_TEMPLATE = "软探针系统通知：%s公司%s检测到一级双送平台%s指标出现异常，请及时处理。";
    /**
     * 小时/实时指标省份邮件内容模板
     */
    public static String REAL_HOUR_MAIL_PROVINCE_CONTENT_TEMPLATE = "%s公司同事：</p>" +
            "<p style='text-indent: 2em; margin-top: 20px;'>您好！贵公司%s检测到软探针双送平台%s指标数据出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";

    //分钟级卡顿告警
    public static String  KADUN_MINUTE_COUNTRY_TITLE_TEMPLATE = "软探针系统通知，%s软探针双送平台机顶盒卡顿告警出现异常，请及时处理。";

    /**
     * 分钟级卡顿内容模板
     */
    public static String KADUN_MINUTE_COUNTRY_CONTENT_TEMPLATE = "集团领导： </p><p style='text-indent: 2em; margin-top: 20px;'>"
            + "您好! %s软探针双送平台机顶盒卡顿告警指标出现异常，请尽快登录软探针一级双送平台查看、处理。如邮件反馈，请群回邮件，谢谢！</p>%s";

    /**
     * 分钟级卡顿告警
     */
    public static String  KADUN_MINUTE_COUNTRY_RESTORE_TITLE_TEMPLATE = "软探针系统通知，%s软探针双送平台机顶盒卡顿告警异常已恢复";

    /**
     * 分钟级卡顿内容模板
     */
    public static String KADUN_MINUTE_COUNTRY_RESTORE_CONTENT_TEMPLATE = "集团领导： <p style='text-indent: 2em; margin-top: 20px;'>"
            + "您好! %s软探针双送平台机顶盒卡顿告警指标异常已恢复，请知悉，谢谢！";

    public static String  KADUN_HOUR_CALCULATE_TITLE_TEMPLATE = "软探针系统通知，%s年%s月%s日%s时获取%s指标数据异常，请尽快处理";

    public static String  KADUN_HOUR_CALCULATE_CONTENT_TEMPLATE = "集团领导： </p><p style='text-indent: 2em; margin-top: 20px;'>"
            + "您好! %s年%s月%s日%s时从%s获取数据为空，请尽快处理。</p>";
}
