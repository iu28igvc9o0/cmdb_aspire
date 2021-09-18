package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 同步配置历史详情信息
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.image.api.dto
 * 类名称:    SyncConfigHisDetailResponse.java
 * 类描述:    同步配置历史详情信息
 * 创建人:    WuFan
 * 创建时间:  2017/08/24 15:21
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SyncHistoryDetailResponse extends SyncHistoryVODetail {
	private static final long serialVersionUID = -8898882371071172990L;

	@JsonProperty("can_removed")
	private boolean canRemoved = false;
    

}