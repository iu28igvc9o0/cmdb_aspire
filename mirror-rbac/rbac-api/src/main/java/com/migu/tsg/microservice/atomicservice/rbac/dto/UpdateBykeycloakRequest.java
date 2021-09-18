package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateBykeycloakRequest {

	@JsonProperty("username")
	private String username;
    
	@JsonProperty("enable")
    private Boolean enable;
}
