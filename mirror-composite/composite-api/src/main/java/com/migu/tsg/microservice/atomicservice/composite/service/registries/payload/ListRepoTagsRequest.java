package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRepoTagsRequest {
	@JsonProperty("view_type")
	private String viewType;
	
	@JsonProperty("scan_results")
	private String scanResults;
	private String artifacts;
	
	@JsonProperty("page_size")
	private Integer pageSize;
	private Integer page;
}
