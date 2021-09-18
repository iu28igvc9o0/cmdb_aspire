package com.aspire.mirror.alert.server.dao.alert.po;

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
public class KpiListData {

	 
	private String idc_type;

	private String pod;
	private String roomId;
	private String deviceClass;
	
	private String deviceType;

	
	private String keys_value;

	private Integer id;
	
	private Integer kpi_id;
	
}