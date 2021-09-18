package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterCreateRequest {
	
	private String description;
	
	private String endpoint;
	
	private String protocol;
	
	private String channel;
	
	private String issuer;
	
	private String audience;
	
	@JsonProperty("display_name")
	private String displayName;
	
	@JsonProperty("region_id")
	private String regionId;
	
	@JsonProperty("is_public")
	private Boolean isPublic = false;
}
