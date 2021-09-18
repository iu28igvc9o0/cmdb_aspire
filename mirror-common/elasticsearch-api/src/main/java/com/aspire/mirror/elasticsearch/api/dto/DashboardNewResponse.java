package com.aspire.mirror.elasticsearch.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class DashboardNewResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<String>  xAxis;
	
	private List<String> legend;
	
	private List<Serie> series;
	
	//private Map<String, List<Object>> data;
	
	private String defaultTime;
	
}
