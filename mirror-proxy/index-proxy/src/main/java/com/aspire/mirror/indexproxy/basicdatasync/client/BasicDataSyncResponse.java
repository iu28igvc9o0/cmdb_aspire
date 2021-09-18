package com.aspire.mirror.indexproxy.basicdatasync.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BasicDataSyncResponse<T> {
	
	@JsonProperty("lastSyncSeq")
	private Integer lastSyncSeq;
	
	@JsonProperty("dataList")
	private List<T> itemDataList;
}
