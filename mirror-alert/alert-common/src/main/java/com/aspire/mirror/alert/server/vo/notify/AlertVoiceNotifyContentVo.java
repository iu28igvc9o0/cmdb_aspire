package com.aspire.mirror.alert.server.vo.notify;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AlertVoiceNotifyContentVo {

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
    // 告警场景详情
    private Map<String, Object> alertFilterSceneInfo;
    // 播报的内容字段
    private String content;
    // 告警存在的时间
    private int alertExistTime;
    // 创建人
    private String creator;
    // 创建时间
    private String createTime;
    // 已播报的告警id
    private String voiceAlertId;
}
