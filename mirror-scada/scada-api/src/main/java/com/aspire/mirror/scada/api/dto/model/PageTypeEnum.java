package com.aspire.mirror.scada.api.dto.model;

/**
* 图片类型枚举    <br/>
* Project Name:alert-api
* File Name:AlertStatusWrap.java
* Package Name:com.aspire.mirror.alert.api.dto.model
* ClassName: AlertStatusWrap <br/>
* date: 2019年6月11日 下午8:36:19 <br/>
* @author JinSu
* @version 
* @since JDK 1.8
*/ 
public enum PageTypeEnum {
	// 普通页面ordinary
	ORDINARY_PAGE(0,"普通页面"),
	// 模板Template
	TEMPLATE(1,"模板");
	private Integer code;
	private String name;

	private PageTypeEnum(Integer code, String name) {
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