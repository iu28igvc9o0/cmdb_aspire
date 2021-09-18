package com.aspire.mirror.composite.payload.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EmergencySubscribeRules {
    //当前用户name
    @JsonProperty("username")
    private String username;

    //告警id
    @JsonProperty("alertIds")
    private String alertIds;

    //邮箱模板
    @JsonProperty("emails")
    private String emails;

    //邮件主题
    @JsonProperty("subject")
    private String subject;

}
