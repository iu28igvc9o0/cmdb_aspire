package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.LogInfoDTOJakiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogListResponseJakiro {

	// 返回日志总记录数
	@SerializedName("total_items")
	private String totalItems;

	// 返回日志总页数
	@SerializedName("total_page")
	private String totalPage;

	private List<LogInfoDTOJakiro> logs;

}