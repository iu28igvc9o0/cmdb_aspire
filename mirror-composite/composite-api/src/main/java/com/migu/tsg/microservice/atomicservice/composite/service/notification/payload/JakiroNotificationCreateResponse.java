package com.migu.tsg.microservice.atomicservice.composite.service.notification.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.AbstractBasePrivilegeResource;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JakiroNotificationCreateResponse extends AbstractBasePrivilegeResource{
	
	@JsonProperty("name")
    private String name;
	
	@JsonProperty("created_by")
    private String created_by;
	
	@JsonProperty("created_at")
    private String created_at;
	
	@JsonProperty("uuid")
    private String uuid;
	
	@JsonProperty("subscriptions")
    private List<JakiroNotificationSubscriptionsDetail> listSubscriptions;
	
    /**
     * 项目名
     */
    private String project_name;
	
}