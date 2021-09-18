package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步配置历史信息
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.image.api.dto.vo
 * 类名称:    SyncConfigHisVO.java
 * 类描述:    同步配置历史信息
 * 创建人:    WuFan
 * 创建时间:  2017/07/31 14:09
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class SyncHistoryVODetail implements Serializable {


	private static final long serialVersionUID = 3809789822438045669L;

	// 历史ID
    @ApiModelProperty(value = "历史ID")
    @JsonProperty("history_id")
    private String historyId;

    // 配置ID
    @ApiModelProperty(value = "配置ID")
    @JsonProperty("config_id")
    private String configId;

    // 配置名称
    @ApiModelProperty(value = "配置名称")
    @JsonProperty("config_name")
    private String configName;

    // 命名空间
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    // 源仓库id
    @ApiModelProperty(value = "源仓库id")
    @JsonProperty("source_repo_id")
    private String sourceRepoId;

    // 源仓库服务器名称
    @ApiModelProperty(value = "源仓库服务器名称")
    @JsonProperty("source_registry_name")
    private String sourceRegistryName;

    // 源项目名称
    @ApiModelProperty(value = "源项目名称")
    @JsonProperty("source_project_name")
    private String sourceProjectName;

    // 源仓库名称
    @ApiModelProperty(value = "源仓库名称")
    @JsonProperty("source_repo_name")
    private String sourceRepoName;

    // 是否为http协议,0:否,1:是
    @ApiModelProperty(value = "是否为http协议,0:否,1:是")
    @JsonProperty("source_is_http")
    private Boolean sourceIsHttp = true;

    // 源仓库服务器端点
    @ApiModelProperty(value = "源仓库服务器端点")
    @JsonProperty("source_registry_endpoint")
    private String sourceRegistryEndpoint;

    // 源用户名
    @ApiModelProperty(value = "源用户名")
    @JsonProperty("source_username")
    private String sourceUsername;

    // 源用户密码
    @ApiModelProperty(value = "源用户密码")
    @JsonProperty("source_password")
    private String sourcePassword;

    // 集群ID
    @ApiModelProperty(value = "集群ID")
    @JsonProperty("region_id")
    private String regionId;

    // 标签
    @ApiModelProperty(value = "标签")
    private String tag;

    // 是否为http协议0:否,1:是
    @ApiModelProperty(value = "是否为http协议0:否,1:是")
    @JsonProperty("dest_is_http")
    private Boolean destIsHttp = true;

    // 目标端点
    @ApiModelProperty(value = "目标端点")
    @JsonProperty("dest_endpoint")
    private String destEndpoint;

    // 目标用户名
    @ApiModelProperty(value = "目标用户名")
    @JsonProperty("dest_username")
    private String destUsername;

    // 目标用户密码
    @ApiModelProperty(value = "目标用户密码")
    @JsonProperty("dest_password")
    private String destPassword;

    // 创建者
    @ApiModelProperty(value = "创建者")
    @JsonProperty("created_by")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("created_at")
    private Date createdAt;

    // 开始时间
    @ApiModelProperty(value = "开始时间")
    @JsonProperty("started_at")
    private Date startedAt;

    // 完成时间
    @ApiModelProperty(value = "完成时间")
    @JsonProperty("finished_at")
    private Date finishedAt;

    // 状态,B,D,I,F,S,W
    @ApiModelProperty(value = " 状态,B,D,I,F,S,W")
    private String status;

    // 是否公有,0:否,1:是
    @ApiModelProperty(value = "是否公有,0:否,1:是")
    @JsonProperty("source_is_public")
    private Boolean sourceIsPublic = false;

    // 目标id
    @ApiModelProperty(value = "目标id")
    @JsonProperty("dest_id")
    private String destId;
    
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
    public Date getStartedAt() {
        if (this.startedAt == null) {
            return null;
        }
        return (Date) this.startedAt.clone();
    }
    public void setStartedAt(final Date startedAt) {
        if (startedAt == null) {
            this.startedAt = null;
        } else {
            this.startedAt = (Date) startedAt.clone();
        }
    }
    public Date getFinishedAt() {
        if (this.finishedAt == null) {
            return null;
        }
        return (Date) this.finishedAt.clone();
    }
    public void setFinishedAt(final Date finishedAt) {
        if (finishedAt == null) {
            this.finishedAt = null;
        } else {
            this.finishedAt = (Date) finishedAt.clone();
        }
    }
}