package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class SyncConfigSourceVOCopy{
	/**
	 * 
	 */
	//private static final long serialVersionUID = -7018603856085195194L;

	// 源ID
	@JsonProperty("source_id")
    private String sourceId;

    // 类型,REPOSITORY
    private String type;

    // 信息
    private Info info;

    // 配置ID
    @JsonProperty("config_id")
    private String configId;
    
}
