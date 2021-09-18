package com.aspire.mirror.alert.api.dto.monitorHttp;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorHttpHisResponse {
	private Integer id;

	private Integer http_config_id;

	private String request_method;

	private String request_parm;

	private String request_http_addr;

	private Integer time_out;

	private String start_time;

	private String end_time;

	private String time_con;

	private String response_type;

	private Integer response_code;

	private String json_attribute;

	private String json_value;

	private String json_operator;

	private String regular_check;

	private String html_format;

	private String request_result;

	private String conclusion;

	private Integer normal;

	private String head_response;

	private Date create_time;
	
	private Integer result;
	
	private String http_content_type;
	
	private String response_status_code;

}
