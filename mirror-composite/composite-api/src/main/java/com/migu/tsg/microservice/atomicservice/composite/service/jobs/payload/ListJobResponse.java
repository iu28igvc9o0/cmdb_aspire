package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListJobResponse {
	
	@JsonProperty("page_size")
	private Integer pageSize;

	private Integer count;

	private Integer next;

	private Integer previous;

	private List<ListJobBase> results;
}
