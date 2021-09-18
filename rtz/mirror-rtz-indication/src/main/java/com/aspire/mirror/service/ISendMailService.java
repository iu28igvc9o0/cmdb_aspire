package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationEntity;
import net.sf.json.JSONObject;

import java.util.List;

public interface ISendMailService {
    /**
     * 发送异常指标邮件
     */
    void sendIndicationEmail(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate);

    void sendHourIndicationEmail(List<IndicationEntity> indicationList, String calcDate);

    JSONObject getSendEmailData(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate);

    /**
     * 发送双送 分钟指标的邮件提醒
     * @param indicationOwner
     * @param catalogBox
     * @param indicationFrequency
     * @param date
     */
    void sendMinuteIndicationMail(String indicationOwner, String catalogBox, String indicationPosition, String indicationFrequency, String date);
}
