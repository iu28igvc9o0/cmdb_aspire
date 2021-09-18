package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 镜像同步历史查询结果
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.image.api.dto
 * 类名称:    SyncConfigHisPageResponse.java
 * 类描述:    镜像同步历史查询结果
 * 创建人:    WuFan
 * 创建时间:  2017/08/23 22:11
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class ListHistoryResponse implements Serializable {
	private static final long serialVersionUID = -6602469657357802469L;
	
	@JsonProperty("result_list")
	private List<SyncHistoryVO> listSyncConfigHisVO;

	@JsonProperty("num_pages")
    private int pageNo;

	@JsonProperty("page_size")
    private int pageSize;
	
	private int total = 0;
}