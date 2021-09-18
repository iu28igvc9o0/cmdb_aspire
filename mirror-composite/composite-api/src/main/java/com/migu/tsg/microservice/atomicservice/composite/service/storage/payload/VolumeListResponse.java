package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage
* 类名称: VolumeListResponse.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月14日上午9:59:08
* 版本: v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VolumeListResponse {
    private Integer total_items;
    
    private int total_pages;
   
    private Boolean success;
	
	private String knamespace;

    @JsonProperty("volumes")
    private List<VolumesDetailResponse> volumes;

    @JsonProperty("action")
    private List<String> resource_action; 
}
