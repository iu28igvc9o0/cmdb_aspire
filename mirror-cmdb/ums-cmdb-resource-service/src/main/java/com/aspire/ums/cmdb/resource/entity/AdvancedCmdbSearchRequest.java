package com.aspire.ums.cmdb.resource.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class AdvancedCmdbSearchRequest {
	@JsonProperty("mainParams")
	private Map<String, Object> mainParams;
}
