package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 镜像同步配置历史创建请求对象
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.image.api.dto
 * 类名称:    SyncConfigHisCreateRequest.java
 * 类描述:    镜像同步配置历史创建请求对象，包括基本信息、源信息，目标信息
 * 创建人:    WuFan
 * 创建时间:  2017/07/30 23:26
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SyncHistoryCreateRequestBean extends SyncConfigBaseVOCopy implements Serializable {

    private static final long serialVersionUID = -7808941119230418493L;

    private String tag;

    @JSONField(name = "source")
    private SourceBean source;

    @JSONField(name = "dest")
    private List<SyncConfigDestVOCopy> dest;

    /**
     * SourceBean
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SourceBean implements Serializable {

        private static final long serialVersionUID = 8812623490605328772L;

        @JsonProperty("info")
        private SyncHistorySourceCreateRequest source;
    }
}
