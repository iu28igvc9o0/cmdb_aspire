package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class RegistryProjectRepoUpdateReponse {
	
	   private Result result;

	    // 镜像仓库uuid
	    private String uuid;

	    // 镜像仓库简要描述
	    private String description;

	    // 镜像仓库详细描述
	    @JsonProperty("full_description")
	    private String fullDescription;

	    // 下载次数
	    private Integer download;

	    // 上传次数
	    private Integer upload;

	    // 上传时间
	    @JsonProperty("pushed_at")
	    private Date pushedAt;

	    // 下载时间
	    @JsonProperty("pulled_at")
	    private Date pulledAt;

	    // 创建时间
	    @JsonProperty("created_at")
	    private Date createdAt;

	    // 更新时间
	    @JsonProperty("updated_at")
	    private Date updatedAt;

	    // 镜像源ID
	    @JsonProperty("registry")
	    private String registryId;

	    // 是否公有0:否,1:是
	    @JsonProperty("is_public")
	    private Boolean isPublic = false;

	    // 项目ID
	    @JsonProperty("project")
	    private String projectId;
	    
	    private String name;
	    
	    private String namespace;
	    
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
	    public Date getPulledAt() {
	        if (this.pulledAt == null) {
	            return null;
	        }
	        return (Date) this.pulledAt.clone();
	    }
	    public void setPulledAt(final Date pulledAt) {
	        if (pulledAt == null) {
	            this.pulledAt = null;
	        } else {
	            this.pulledAt = (Date) pulledAt.clone();
	        }
	    }
	    public Date getPushedAt() {
	        if (this.pushedAt == null) {
	            return null;
	        }
	        return (Date) this.pushedAt.clone();
	    }
	    public void setPushedAt(final Date pushedAt) {
	        if (pushedAt == null) {
	            this.pushedAt = null;
	        } else {
	            this.pushedAt = (Date) pushedAt.clone();
	        }
	    }
	
}
