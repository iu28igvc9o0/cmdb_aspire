package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

public class FormOptions implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String id;

    private String code;

    private String name;
    
    private String value;

    private String isdefault;

    private String color;

    private String parentid;

    private String formid;
    
    public FormOptions() {
		super();
	}

	public FormOptions(String id, String code, String name, String value,
			String isdefault, String color, String parentid, String formid) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.value = value;
		this.isdefault = isdefault;
		this.color = color;
		this.parentid = parentid;
		this.formid = formid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    
    public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	
	public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

	@Override
	public String toString() {
		return "FormOptions [id=" + id + ", code=" + code + ", name=" + name
				+ ", value=" + value + ", isdefault=" + isdefault + ", color="
				+ color + ", parentid=" + parentid + ", formid=" + formid + "]";
	}
	
}