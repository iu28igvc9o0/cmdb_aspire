package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 镜像同步配置历史创建源信息
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.image.api.dto
 * 类名称:    SyncConfigSourceHisCreateRequest.java
 * 类描述:    镜像同步配置创建源信息
 * 创建人:    WuFan
 * 创建时间:  2017/07/30 23:09
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class SyncHistorySourceCreateRequest implements Serializable {

    private static final long serialVersionUID = -5587183595911861516L;

    // 源仓库id
    @JsonProperty("repo_uuid")
    private String sourceRepoId;

    @JsonProperty("registry_uuid")
    private String sourceRegistryId;

    // 源仓库服务器名称
    @JsonProperty("registry_name")
    private String sourceRegistryName;

    // 源项目名称
    @JsonProperty("project_name")
    private String sourceProjectName;

    // 源仓库名称
    @JsonProperty("repo_name")
    private String sourceRepoName;

    // 是否为http协议,0:否,1:是
    @JsonProperty("source_is_http")
    private Integer sourceIsHttp = 1;

    // 源仓库服务器端点
    @JsonProperty("source_registry_endpoint")
    private String sourceRegistryEndpoint;

    // 源用户名
    @JsonProperty("source_username")
    private String sourceUsername;

    // 源用户密码
    @JsonProperty("source_password")
    private String sourcePassword;
}