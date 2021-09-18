package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CmdbAdvancedMaintance implements Serializable {

	/**
	 * 高级维保实体类
	 */
	private static final long serialVersionUID = 1L;
    
	//private int id;
	private String product;
	private String service_com;
	private String cooperate_com;
	private String content;
	private String num;
	private String unit;
	private String begin_date;
	private String end_date;
	private String remarks;
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}

}
