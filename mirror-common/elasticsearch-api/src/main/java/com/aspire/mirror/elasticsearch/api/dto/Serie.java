package com.aspire.mirror.elasticsearch.api.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class Serie   implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	//线性,柱行图为list, 饼图为map name:类别  value:值
	private List<Object> data;
	
	private Object max;
	
	private Object avg;
	
}
