package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertTotal {
	@JsonProperty("idc_type")
	private String idcType;

	@JsonProperty("sum")
	private Integer sum;

	@JsonProperty("serious_count")
	private Integer seriousCount;

	@JsonProperty("important_count")
	private Integer importantCount;

	@JsonProperty("secondary_count")
	private Integer secondaryCount;

	@JsonProperty("tips_count")
	private Integer tipsCount;
	
	private String month;
	
	public void setSum() {
		this.sum = (seriousCount == null ? 0 : seriousCount) 
				+ (importantCount == null ? 0 : importantCount)
				+ (secondaryCount == null ? 0 :secondaryCount)
				+ (tipsCount == null ? 0 : tipsCount);
	}
}
