package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryProjectRepoCreatRequest {
	
	private String description;
	
	@JsonProperty("full_description")
	private String fullDescription;
	private String name;
	
	@JsonProperty("is_public")
	private Boolean isPublic = false;
	
	@JsonProperty("registry")
	private String registryId;
	
	@JsonProperty("project")
	private String projectId;
	
	@JsonProperty("repo_name")
	private String repoName;
}
