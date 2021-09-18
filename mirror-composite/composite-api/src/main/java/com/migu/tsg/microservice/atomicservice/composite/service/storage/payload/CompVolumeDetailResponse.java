package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.List;
import java.util.Map;

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
public class CompVolumeDetailResponse {
    private List<Map<String, String>> stats;
    
    @JsonProperty("resource_actions")
    private List<String> resourceActions;

    private String name;

    private String backup_url;

    private String bricks;
    
    private String driver_volume_id;
    
    private String created_at;
    
    private String snapshot_id;
    
    private String namespace;
    
    private String updated_at;
    
    private String volume_type;
    
    private String driver_name;
    
    private String state;
    
    private Integer iops;
    
    private String created_by;
    
    private String synced_at;
    
    private String privilege;
    
    private Boolean success;
    
    private Integer size;
    
    private String id;
    
    private String region_id;
    
    /** 挂载计数*/
    private String mountCnt ;
    
    /** 错误信息*/
    private String errormsg ;
}
