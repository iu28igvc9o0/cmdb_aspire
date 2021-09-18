package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class RegistryProjectRepoCreateResponse {
	private String name;
	private String type;
	private String uuid;
	private String namespace;
	
	@JsonProperty("created_by")
	private String createdBy;
	
	@JsonProperty("project_uuid")
	private String projectUuid;
	
	@JsonProperty("space_uuid")
	private String spaceUuid;
	private String description;
	
	@JsonProperty("full_description")
	private String fullDescription;
	private String download;
	private String upload;
	
	@JsonProperty("pushed_at")
	private Date pushedAt;
	
	@JsonProperty("pulled_at")
	private Date pulledAt;
	
	@JsonProperty("created_at")
	private Date createdAt;
	
	@JsonProperty("updated_at")
	private Date updatedAt;
	 
	 @JsonProperty("registry")
	private String registryId;
	 
	 @JsonProperty("project")
	private String projectId;
	
	 @JsonProperty("is_public")
	private Boolean isPublic = false;
	 
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
