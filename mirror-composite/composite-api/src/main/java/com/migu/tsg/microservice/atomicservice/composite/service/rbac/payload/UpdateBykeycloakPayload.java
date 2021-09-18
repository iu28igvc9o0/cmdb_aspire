package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateBykeycloakPayload {
	
	@JsonProperty("username")
	private String username;
    
	@JsonProperty("enable")
    private Boolean enable;
}
