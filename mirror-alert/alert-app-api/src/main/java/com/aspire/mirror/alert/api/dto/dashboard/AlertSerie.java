package com.aspire.mirror.alert.api.dto.dashboard;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class AlertSerie   implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	//线性,柱行图为list, 饼图为map name:类别  value:值
	private List<Object> data;
	
}
