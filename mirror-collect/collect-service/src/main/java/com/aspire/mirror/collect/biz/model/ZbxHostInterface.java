package com.aspire.mirror.collect.biz.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ZbxHostInterface {
	@JsonProperty("hostid")
	private Integer hostId;
	@JsonProperty
	private String ip;
}
