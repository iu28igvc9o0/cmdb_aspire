package com.aspire.mirror.composite.payload.dashboard;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class ComAlertDashboardResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<ComAlertSerie> series;
	
	private List<String>  xAxis;
	
	private List<String> legend;
	
	
	private String defaultTime;
	
}
