package com.aspire.mirror.scada.api.dto.model;

/**
* 页面类型枚举    <br/>
* Project Name:alert-api
* File Name:AlertStatusWrap.java
* Package Name:com.aspire.mirror.alert.api.dto.model
* ClassName: AlertStatusWrap <br/>
* date: 2019年6月11日 下午8:36:19 <br/>
* @author JinSu
* @version 
* @since JDK 1.8
*/ 
public enum PictureTypeEnum {
	// 普通背景图
	ORDINARY_BACKGROUND_PICTURE(0,"普通背景图"),
	// 拓扑图背景图
	TOPOLOGY_BACKGROUND_PICTURE(1,"拓扑图背景图");
	private Integer code;
	private String name;

	private PictureTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
			this.name = name;
		}
}