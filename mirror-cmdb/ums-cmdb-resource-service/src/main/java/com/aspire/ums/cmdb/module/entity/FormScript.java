package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;

public class FormScript implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;

    private String formid;

    private String tag;

    private String language;

    private String script;

    public FormScript() {
		super();
	}

	public FormScript(String id, String formid, String tag, String language,
			String script) {
		super();
		this.id = id;
		this.formid = formid;
		this.tag = tag;
		this.language = language;
		this.script = script;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script == null ? null : script.trim();
    }

	@Override
	public String toString() {
		return "FormScript [id=" + id + ", formid=" + formid + ", tag=" + tag
				+ ", language=" + language + ", script=" + script + "]";
	}
    
}