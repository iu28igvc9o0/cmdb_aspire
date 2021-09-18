package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;
/**
 * 
 * @author: lupeng
 *
 */
public class Module implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String name;

    private String code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
	
}