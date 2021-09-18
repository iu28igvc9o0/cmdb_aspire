package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 构建代码配置新增对象类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.migu.tsg.msp.microservice.atomicservice.ci.api.dto
 * 类名称:   BuildCodeHistoryCreateRequest.java
 * 类描述:   构建代码配置创建请求对象
 * 创建人:   WuFan
 * 创建时间: 2017-08-13 17:25:19
 */
@Data
@NoArgsConstructor
public class TagArtifactDetailResponse implements Serializable {

    private static final long serialVersionUID = -5616452291955033352L;

    /** 文件名称 */
    @JsonProperty("key")
    private String name;

    /** 文件大小 */
    private Integer size;

    /** 最后修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("last_modified")
    private Date lastModified;

    public Date getLastModified() {
        if (this.lastModified == null) {
            return null;
        }
        return (Date) this.lastModified.clone();
    }
    public void setLastModified(final Date lastModified) {
        if (lastModified == null) {
            this.lastModified = null;
        } else {
            this.lastModified = (Date) lastModified.clone();
        }
    }
}
