package com.aspire.mirror.httpMonitor.dao.po;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorHttpConfig {

	private Integer id;

	private String monitor_name;

	private String biz_system_id;

	private Integer alert_level;

	private Integer test_period;

	private String idcType;

	private Integer isIntranet;

	private String intranet_idcType;

	private String extranet;

	private String create_staff;

	private String update_staff;

	private Date create_time;

	private Date update_time;

	private String request_http_addr;

	private String request_method;

	private String response_type;

	private String regular_check;

	private Integer response_code;

	private String request_parm;

	private String json_attribute;

	private String json_value;

	private String json_operator;

	private Integer time_out;

	private String html_format;
	private String http_content_type;
	private Integer status;
}
