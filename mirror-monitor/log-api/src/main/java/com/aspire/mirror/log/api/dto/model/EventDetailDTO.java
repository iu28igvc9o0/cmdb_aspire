package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 事件明细信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: EventDetailDTO.java
 * 类描述: 事件明细信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:45:09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDetailDTO {

    private String actions;

    private String application;

    private String attribute;

    @JsonProperty(value = "attribute_type")
    @SerializedName("attribute_type")
    private String attributeType;

    @JsonProperty(value = "build_config_name")
    @SerializedName("build_config_name")
    private String buildConfigName;

    @JsonProperty(value = "build_config_uuid")
    @SerializedName("build_config_uuid")
    private String buildConfigUuid;

    private DetailConstraintsDTO constraints;

    private DetailContentDTO content;

    @JsonProperty(value = "created_at")
    @SerializedName("created_at")
    private String createdAt;

    @JsonProperty(value = "created_by")
    @SerializedName("created_by")
    private String createdBy;

    @JsonProperty(value = "current_version")
    @SerializedName("current_version")
    private String currentVersion;

    private String description;

    private String digest;

    private String host;

    private String id;

    @JsonProperty(value = "image_id")
    @SerializedName("image_id")
    private String imageId;

    @JsonProperty(value = "image_tag")
    @SerializedName("image_tag")
    private String imageTag;

    private String message;

    private String name;

    private String namespace;

    private String operation;

    private String operator;

    private DetailParentDTO parent;

    @JsonProperty(value = "permissionUuid")
    @SerializedName("permissionUuid")
    private String permissionUuid;

    private String port;

    private String region;

    @JsonProperty(value = "region_id")
    @SerializedName("region_id")
    private String regionId;

    @JsonProperty(value = "region_name")
    @SerializedName("region_name")
    private String regionName;

    @JsonProperty(value = "region_uuid")
    @SerializedName("region_uuid")
    private String regionUuid;

    private String registry;

    private String repository;

    private String resource;

    @JsonProperty(value = "resource_actions")
    @SerializedName("resource_actions")
    private String resourceActions;

    private DetailResourcesDTO resources;

    @JsonProperty(value = "role_name")
    @SerializedName("role_name")
    private String roleName;

    @JsonProperty(value = "role_uuid")
    @SerializedName("role_uuid")
    private String roleUuid;

    private DetailRolesDTO roles;

    private DetailServicesDTO services;

    @JsonProperty(value = "space_name")
    @SerializedName("space_name")
    private String spaceName;

    @JsonProperty(value = "space_uuid")
    @SerializedName("space_uuid")
    private String spaceUuid;

    private String status;

    private String template;

    @JsonProperty(value = "template_uuid")
    @SerializedName("template_uuid")
    private String templateUuid;

    private String token;

    @JsonProperty(value = "trigger_type")
    @SerializedName("trigger_type")
    private String triggerType;

    private String type;

    @JsonProperty(value = "unique_name")
    @SerializedName("unique_name")
    private String uniqueName;

    @JsonProperty(value = "updated_at")
    @SerializedName("updated_at")
    private String updatedAt;

    @JsonProperty(value = "project_name")
    @SerializedName("project_name")
    private String projectName;

    private String uuid;

}
