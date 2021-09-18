package com.migu.tsg.microservice.atomicservice.composite.service.notification.payload;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JakiroSubscriptionVO implements Serializable {

    private static final long serialVersionUID = -5133400446503684772L;

    // email/webhook/sms 三种
    private String method;

    // 收件人
    private String recipient;

    // 备注
    private String remark;

    // 密码webhook 有时会需要
    private String secret;

}