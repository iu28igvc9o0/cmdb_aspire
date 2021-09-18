package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MailEntity {
    //主题编码
    private String themeCode;
    //所属系统 全量/双送
    private String indicationOwner;
    //指标分类 机顶盒/智能网关
    private String indicationCatalog;
    //计算频率 月/日/小时/实时/分钟
    private String indicationFrequency;
    //指标显示位置 概览/非概览
    private String indicationPosition;
    //邮件发送时间
    private Date sendTime;
    //邮件发送服务器
    private String mailServer;
    //邮件标题
    private String mailTitle;
    //邮件内容
    private String mailContent;
}
