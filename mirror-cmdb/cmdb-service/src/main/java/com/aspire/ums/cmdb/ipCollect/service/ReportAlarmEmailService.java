package com.aspire.ums.cmdb.ipCollect.service;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-21 15:14
 * @description 报表数据告警邮件业务逻辑接口 - 存活IP,稽核报表的数据
 */
public interface ReportAlarmEmailService {

    /**
     * 报表查询并发送邮件给接收人
     */
    Map<String,String> sendAndGetReportAlarmContent(String type);

    /**
     * 发送告警邮件
     */
    Map<String,String> sendAlarmEmail(Map<String, String> param);
}
