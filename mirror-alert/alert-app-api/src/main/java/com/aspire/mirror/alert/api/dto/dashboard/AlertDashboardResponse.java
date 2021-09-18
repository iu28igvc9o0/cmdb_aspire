package com.aspire.mirror.alert.api.dto.dashboard;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class AlertDashboardResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<AlertSerie> series;
	
	private List<String>  xAxis;
	
	private List<String> legend;
	
	
	private String defaultTime;
	
}
