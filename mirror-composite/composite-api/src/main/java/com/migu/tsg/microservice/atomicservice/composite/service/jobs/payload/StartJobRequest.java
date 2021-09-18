package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartJobRequest {
	
	@JsonProperty("config_name")
	private String configName ;
	
	@JsonProperty("config_uuid")
	private String configUuid ;
	
	@JsonProperty("created_by")
	private String createdBy ;
	
	@JsonProperty("user_token")
	private String userToken ;
	private String namespace ;
}
