package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ListRegistryProjectBaseResponse {
	@JsonProperty("project_name")
	private  String projectName;
	private  String name;
	private  String namespace;
	
	@JsonProperty("created_by")
	private  String createdBy;
	private  String privilege;
	private  String repoCount;
	
	@JsonProperty("project_id")
	private  String projectId;
	
	@JsonProperty("resource_actions")
	private List<String> resourceActions;
	
	
	@JsonProperty("created_at")
	private Date createdAt;

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
}
