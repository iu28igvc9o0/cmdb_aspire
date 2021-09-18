package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 *
 * 项目名称: 微服务运维平台
 * 包:      com.migu.tsg.msp.microservice.atomicservice.ci.api.dto
 * 类名称:   BuildArtifactResponse.java
 * 类描述:   构建产物对象
 * 创建人:   JinSu
 * 创建时间: 2017-08-13 17:25:19
 */
@Data
public class TagArtifactResponse {
	@JsonProperty("tag_name")
	private String  tagName;

	@JsonProperty("build_id")
	private String buildId;

	@JsonProperty("build_at")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
	private Date buildAt;

	@JsonProperty("artifacts_count")
	private Integer artifactsCount;

	@JsonProperty("artifact_download_url")
	private String artifactDownloadUrl;

	@JsonProperty("artifacts")
    private List<TagArtifactDetailResponse> artifactDetailResponse;
	
	public Date getBuildAt() {
        if (this.buildAt == null) {
            return null;
        }
        return (Date) this.buildAt.clone();
    }
    public void setBuildAt(final Date buildAt) {
        if (buildAt == null) {
            this.buildAt = null;
        } else {
            this.buildAt = (Date) buildAt.clone();
        }
    }
}
