package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.List;

public class CasoptionsBean implements Serializable{
	   
    private static final long serialVersionUID = 1L;
    //private String id;
	private String label;
	private String value;
	private List<CasoptionsBean> children;
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public List<CasoptionsBean> getChildren() {
        return children;
    }
    public void setChildren(List<CasoptionsBean> children) {
        this.children = children;
    }

	
}
