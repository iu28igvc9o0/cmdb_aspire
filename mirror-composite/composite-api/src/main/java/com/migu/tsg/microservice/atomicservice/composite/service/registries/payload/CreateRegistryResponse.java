package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@AllArgsConstructor
public class CreateRegistryResponse {
	 // 镜像源uuid
	@JsonProperty("uuid")
    private String registryId;

    // 镜像源简要描述
    private String description;

    // 端点地址
    private String endpoint;

    // 创建时间
    @JsonProperty("created_at")
    private Date createdAt;

    // 更新时间
    @JsonProperty("updated_at")
    private Date updatedAt;

    // 协议,http|https
    private String protocol;

    // 公有云上隧道地址
    private String channel;

    // registry auth issuer
    private String issuer;

    // registry auth audience
    private String audience;

    // 集群ID
    @JsonProperty("region_id")
    private String regionId;

    // 显示名称
    @JsonProperty("display_name")
    private String displayName;

    // 是否公用0:否,1:是
    @JsonProperty("is_public")
    private Boolean isPublic = false;
    
    private String namespace;
    
    private String name;
    
    @JsonProperty("created_by")
    private String createdBy;
    
    public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return (Date) this.createdAt.clone();
    }
    public void setCreatedAt(final Date createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Date) createdAt.clone();
        }
    }
    public Date getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return (Date) this.updatedAt.clone();
    }
    public void setUpdatedAt(final Date updatedAt) {
        if (updatedAt == null) {
            this.updatedAt = null;
        } else {
            this.updatedAt = (Date) updatedAt.clone();
        }
    }
}
