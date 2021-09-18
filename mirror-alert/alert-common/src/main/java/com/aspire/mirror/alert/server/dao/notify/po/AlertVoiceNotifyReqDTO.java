package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertVoiceNotifyReqDTO {

    private String uuid;
    // 语言类型
    private String language;
    // 语音内容
    private String voiceContent;
    // 是否启用
    private int isOpen;
    // 过滤器id
    private int alertFilterId;
    // 过滤场景id
    private int alertFilterSceneId;
    // 播报的内容字段
    private String content;
    // 告警存在的时间
    private int alertExistTime;
    // 创建人
    private String creator;
    // 已播报的告警id
    private String voiceAlertId;

}
