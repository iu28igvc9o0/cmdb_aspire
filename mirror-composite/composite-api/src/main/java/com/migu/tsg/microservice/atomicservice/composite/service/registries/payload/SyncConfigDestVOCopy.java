package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncConfigDestVOCopy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5352842944230443037L;

	// 目标ID
    @ApiModelProperty(value = "目标ID")
    @JsonProperty("dest_id")
    private String destId;

    // 是否为http协议,0:否,1:是
    @ApiModelProperty(value = "是否为http协议,0:否,1:是")
    @JsonProperty("is_http")
    private Boolean isHttp = false;

    // 端点
    @ApiModelProperty(value = "端点")
    private String endpoint;

    // 用户名
    @ApiModelProperty(value = "用户名")
    private String username;

    // 用户密码
    @ApiModelProperty(value = "用户密码")
    private String password;

    // 配置
    @ApiModelProperty(value = "配置")
    @JsonProperty("config_id")
    private String configId;

    // 目标类型,内部:INTERNAL_REGISTRY,外部:EXTERNAL_REGISTRY
    @ApiModelProperty(value = "目标类型,内部:INTERNAL_REGISTRY,外部:EXTERNAL_REGISTRY")
    @JsonProperty("dest_type")
    private String destType;

    // 内部镜像源ID
    @ApiModelProperty(value = "内部镜像源ID")
    @JsonProperty("internal_id")
    private String internalId;
}
