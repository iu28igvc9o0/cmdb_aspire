package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 同步配置基本信息属性
 * @author lf
 *
 */
@Data
@NoArgsConstructor
@ApiModel
public class SyncConfigBaseVOCopy {
	//private static final long serialVersionUID = 3721375941590104175L;
	@JsonProperty("config_id")
    private String configId;

    // 同步配置名称
	@JsonProperty("config_name")
    private String configName;

    // 命名空间
    private String namespace;

    // 创建者
    @JsonProperty("created_by")
    private String createdBy;
    
    private String cpu;

    private String memory;
    
    @JsonProperty("region_id")
    private String regionId    = "";
    
    @JsonProperty("space_uuid")
    private String spaceUuid   = "";
    
    @JsonProperty("project_uuid")
    private String projectUuid = "";
    
    @JsonProperty("project_name")
    private String projectName;
    
    @JsonProperty("region_name")
    private String regionName;
    
    @JsonProperty("space_name")
    private String spaceName;
    // 创建时间
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("created_at")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("updated_at")
    private Date updatedAt;
    
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
