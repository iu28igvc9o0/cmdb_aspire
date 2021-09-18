package com.aspire.mirror.service;

public interface SendEmailService {

    String autoSendEmail(String indicationOwner, String indicationCatalog, String indicationCycle,
                         String indicationFrequency, String dateTime);

    /**
     * 发送双送平台实时指标邮件提醒
     * @param catalogBox 指标分类
     * @param indicationFrequency 指标更新频率
     * @param calcDate 计算时间
     */
    void sendRealActualEmail(String catalogBox, String indicationFrequency, String calcDate);
}
