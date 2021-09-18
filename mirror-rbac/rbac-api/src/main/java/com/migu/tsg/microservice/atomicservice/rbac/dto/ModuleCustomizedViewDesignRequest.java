package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedViewDesignRequest {
	
	private String id;
	
	private String content;

}
