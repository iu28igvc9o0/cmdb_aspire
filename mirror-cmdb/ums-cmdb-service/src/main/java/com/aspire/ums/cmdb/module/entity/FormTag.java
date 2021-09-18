package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;

public class FormTag implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String id;

    private String moduleid;

    private String tag;

    public FormTag() {
		super();
	}

	public FormTag(String id, String moduleid, String tag) {
		super();
		this.id = id;
		this.moduleid = moduleid;
		this.tag = tag;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid == null ? null : moduleid.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

	@Override
	public String toString() {
		return "FormTag [id=" + id + ", moduleid=" + moduleid + ", tag=" + tag
				+ "]";
	}
    
}