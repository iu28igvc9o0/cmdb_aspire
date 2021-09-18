package com.migu.tsg.microservice.atomicservice.composite.service.notification.payload;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JakiroNotificationCreateRequest implements Serializable {

    private static final long serialVersionUID = -4867001484651717871L;

    //namespace
    @JsonProperty("namespace")
    private String namespace;
    
    //name
    @JsonProperty("name")
    private String name;
    
    //space_name
    @JsonProperty("space_name")
    private String space_name;
    
    // 接收方信息
    @JsonProperty("subscriptions")
    private List<JakiroSubscriptionVO> subscriptions;

}