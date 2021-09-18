package com.aspire.mirror.elasticsearch.api.dto;

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
public class KpiData {

	
	private String idc_type;
	//多个逗号分隔
	private String pod;
	//多个逗号分隔
	private String roomId;
	//多个,,/;分隔
	private String keys_value;

	
}