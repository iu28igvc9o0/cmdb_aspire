package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRepoTagsResponse {
	
	@JsonProperty("page_size")
	private Integer pageSize;
	@JsonProperty("num_pages")
	private Integer numPages;
	private Integer page;
	private Integer count;
	private JSONArray results;
}