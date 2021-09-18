package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryProjectRepoUpdateRequest {
	
	private String description;
	
	@JsonProperty("full_description")
	private String fullDescription;
	
	@JsonProperty("is_public")
	private Boolean isPublic = false;
}
