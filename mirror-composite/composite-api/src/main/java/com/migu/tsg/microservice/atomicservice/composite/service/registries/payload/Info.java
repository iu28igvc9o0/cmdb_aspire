package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {
	@JsonProperty("registry_name")
	private String registryName;
	
	@JsonProperty("project_name")
	private String projectName;
	
	@JsonProperty("repository_name")
	private String repositoryName;
	
	@JsonProperty("is_public_registry")
	private Boolean isPublicRegistry = false;
}
