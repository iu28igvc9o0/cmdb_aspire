package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncHistoryCreateRequest {
	
	@JsonProperty("config_name")
    private String configName;
	private String tag;
	
	@JsonProperty("dest_id_list")
	private List<String> destIdList;
}
