package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigCreateRequest {
	 // 同步配置名称
	@JsonProperty("config_name")
    private String configName;

    // 命名空间
    private String namespace;

    // 创建者
    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("source")
    private SyncConfigSourceVOCopy syncConfigSourceCreateRequest;

    @JsonProperty("dest")
    private List<SyncConfigDestVOCopy> listSyncConfigDestCreateRequest;
}
