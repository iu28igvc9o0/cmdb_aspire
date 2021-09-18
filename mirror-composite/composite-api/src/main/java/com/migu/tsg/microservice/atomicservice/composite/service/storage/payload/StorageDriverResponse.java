package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* 类名称: StorageDriverResponse.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月11日下午4:51:21
* 版本: v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StorageDriverResponse {
    @JsonProperty("drivers")
    private List<Map<String , String >> storageDiver;


}
