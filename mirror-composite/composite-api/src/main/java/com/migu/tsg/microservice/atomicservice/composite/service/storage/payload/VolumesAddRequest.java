package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage.payload
* 类名称: VolumesDetailResponse.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月14日上午10:26:30
* 版本: v1.0
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
public class VolumesAddRequest {

    private String region_id;

    private String driver_name;

    private String name;
    
    private int size;
    
    private String knamespace;
    @JsonProperty("space_name")
    private String spaceName;
    
    private String snapshot_id;
    
}
