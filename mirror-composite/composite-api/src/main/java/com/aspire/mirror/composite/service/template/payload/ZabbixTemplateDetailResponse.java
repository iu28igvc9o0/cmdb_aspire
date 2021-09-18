package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZabbixTemplateDetailResponse {
	
	@JsonProperty("templateid")
	private Integer templateId;
	
	@JsonProperty("name")
	private String templateName;
}
