package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;
import java.util.List;

public class OptionBean implements Serializable{

    private static final long serialVersionUID = 1L;
    //private String id;
	private String name;
	private String value;
	private List<OptionBean> children;
	
	public OptionBean(String name, String value, List<OptionBean> children) {
		super();
		this.name = name;
		this.value = value;
		this.children = children;
	}

	public OptionBean() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OptionBean> getChildren() {
		return children;
	}
	public void setChildren(List<OptionBean> children) {
		this.children = children;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "OptionBean [name=" + name + ", value=" + value + ", children="
				+ children + "]";
	}
	
}
