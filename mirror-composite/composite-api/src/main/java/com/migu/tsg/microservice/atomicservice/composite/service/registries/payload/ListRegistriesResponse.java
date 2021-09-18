package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ListRegistriesResponse {
		 private String endpoint;
		 
		 private String protocol;
		 
		 private String description;
		 
		 @JsonProperty("created_at")
		 private Date createdAt;
		 
		 private String namespace;
		 
		 @JsonProperty("updated_at")
		 private Date updatedAt;
		 
		 @JsonProperty("created_by")
		 private String createdBy;
		 
		 private String name;
		 
		 private String privilege;
		 
		 private String audience;
		 
		 @JsonProperty("is_public")
		 private Boolean isPublic = false;
		 
		 @JsonProperty("region_id")
		 private String regionId;
		 
		 @JsonProperty("display_name")
		 private String displayName;
		 
		 private String issuer;
		 
		 private String channel;
		 private String uuid;

		 @JsonProperty("integration_uuid")
		 private String integrationUuid;
		 
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
}
