package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiQueryRequest {

	
	private List<KpiData> dataList;
	
	private String startTime;
	
	private String endTime;
}