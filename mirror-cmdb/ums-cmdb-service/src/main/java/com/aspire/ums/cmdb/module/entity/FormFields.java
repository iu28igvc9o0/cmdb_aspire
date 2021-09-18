package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;

public class FormFields implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;

    private String key;

    private String name;

    private Integer sortindex;

    private String formid;
    
    public FormFields() {
		super();
	}

	public FormFields(String id, String key, String name, Integer sortindex,
			String formid) {
		super();
		this.id = id;
		this.key = key;
		this.name = name;
		this.sortindex = sortindex;
		this.formid = formid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

	@Override
	public String toString() {
		return "FormFields [id=" + id + ", key=" + key + ", name=" + name
				+ ", sortindex=" + sortindex + ", formid=" + formid + "]";
	}
    
}