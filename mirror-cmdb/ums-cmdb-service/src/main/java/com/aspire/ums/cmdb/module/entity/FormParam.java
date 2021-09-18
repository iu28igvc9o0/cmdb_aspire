package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;

public class FormParam implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;

    private String key;

    private String value;

    private String formid;

    private Integer sortindex;
    
    public FormParam() {
		super();
	}

	public FormParam(String id, String key, String value, String formid,
			Integer sortindex) {
		super();
		this.id = id;
		this.key = key;
		this.value = value;
		this.formid = formid;
		this.sortindex = sortindex;
	}
    
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

	@Override
	public String toString() {
		return "FormParam [id=" + id + ", key=" + key + ", value=" + value
				+ ", formid=" + formid + ", sortindex=" + sortindex + "]";
	}
    
}