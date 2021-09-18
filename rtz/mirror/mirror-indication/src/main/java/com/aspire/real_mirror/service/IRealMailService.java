package com.aspire.real_mirror.service;

import com.aspire.mirror.entity.IndicationEntity;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IRealMailService
 * Author:   zhu.juwang
 * Date:     2019/11/7 14:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IRealMailService {
    /**
     * 发送异常指标邮件
     */
    void sendDayIndicationEmail(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate);

    void sendHourIndicationEmail(List<IndicationEntity> indicationList, String calcDate);

    /**
     * 发送双送 分钟指标的邮件提醒
     * @param indicationOwner
     * @param catalogBox
     * @param indicationFrequency
     * @param date
     */
    void sendMinuteIndicationMail(String indicationOwner, String catalogBox, String indicationPosition, String indicationFrequency, String date);
}
