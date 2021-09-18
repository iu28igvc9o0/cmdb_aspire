package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class RegistryProjectCreateResponse {
	// 项目ID
	@JsonProperty("project_id")
    private String projectId;
    // 项目名称
	@JsonProperty("project_name")
    private String projectName;
    // 创建时间
	@JsonProperty("created_at")
    private Date createdAt;
    // 镜像源ID
	@JsonProperty("registry")
    private String registryId;
    private String type;
    private String uuid;
    private String namespace;
    private String name;
    
    @JsonProperty("region_id")
    private String regionId    = "";
    
    @JsonProperty("space_uuid")
    private String spaceUuid   = "";
    
    @JsonProperty("project_uuid")
    private String projectUuid = "";
    
    @JsonProperty("created_by")
    private String createdBy;
    
    @JsonProperty("region_name")
    private String regionName;
    
    @JsonProperty("space_name")
    private String spaceName;

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
