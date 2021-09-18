package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ListProjectRepoResponse {
	@JsonProperty("full_description")
	private String fullDescription;

	private String description;
	private String name;

	@JsonProperty("created_at")
	private Date createdAt;
	
	@JsonProperty("pulled_at")
	private Date pulledAt;
	
	private String namespace;
	private String upload;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("updated_at")
	private Date updatedAt;
	
	@JsonProperty("pushed_at")
	private Date pushedAt;
	
	private Project project;
	private String privilege;
	private Registry registry;
	private String download;

	@JsonProperty("is_public")
	private Boolean isPublic;
	private String uuid;
	@JsonProperty("resource_filter_params")
	private JSONObject resourceFilterParams;

	@JsonProperty("resource_actions")
	private List<String> resourceActions;
	
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

	@Data
	public static class Registry {
		@JsonProperty("is_public")
		private Boolean isPublic = false;
		private String endpoint;
		@JsonProperty("display_name")
		private String displayName;
		private String uuid;
		private String name;
	}

	@Data
	public static class Project {
		@JsonProperty("project_name")
		private String projectName;
		@JsonProperty("project_id")
		private String projectId;
	}
}
