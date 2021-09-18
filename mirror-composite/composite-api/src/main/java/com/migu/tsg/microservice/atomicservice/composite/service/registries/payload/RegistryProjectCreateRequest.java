package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryProjectCreateRequest {
	
	private String name;
	
	@JsonProperty("project_name")
	private String projectName;
	
	@JsonProperty("registry")
	private String  registryId;
}
