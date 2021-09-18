package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class NetSetDto {
	private Integer size;
	private String startTime;
	private String endTime;
	private List<String> ips;
	private String granularity;
	private String idcType;
}
