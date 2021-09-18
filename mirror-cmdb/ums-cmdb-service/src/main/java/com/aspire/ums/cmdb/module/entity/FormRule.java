package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;

public class FormRule implements Serializable{
 
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String code;

    private String rule;

    public FormRule() {
		super();
	}

	public FormRule(String id, String name, String code, String rule) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.rule = rule;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

	@Override
	public String toString() {
		return "FormRule [id=" + id + ", name=" + name + ", code=" + code
				+ ", rule=" + rule + "]";
	}
    
}