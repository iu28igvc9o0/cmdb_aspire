package com.aspire.mirror.elasticsearch.api.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class DashboardResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Serie> series;
	
	private List<String>  xAxis;
	
	private List<String> legend;
	
	
	private String defaultTime;
	
}
