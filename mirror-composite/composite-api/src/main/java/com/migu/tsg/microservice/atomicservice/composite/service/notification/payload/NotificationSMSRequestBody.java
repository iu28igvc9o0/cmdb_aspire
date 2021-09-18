package com.migu.tsg.microservice.atomicservice.composite.service.notification.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.monitor.notice.api.dto.model.SendInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSMSRequestBody {
    // 模板类型
    @JsonProperty(value = "template_type")
    private String templateType;
    // 发送内容
    private SendInfoDTO data;
}
