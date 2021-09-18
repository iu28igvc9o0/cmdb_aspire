package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FormBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;

    private String moduleid;

    private String name;

    private String code;

    private String type;

    private String defaultvalue;

    private String required;

    private String builtin;

    private String hidden;

    private String keyattr;

    private String rate;

    private String unit;

    private Integer sortindex;

    private Date inserttime;

    private Date updatetime;

    private Integer isdelete;

    private String relformid;

    private FormScript formScript;
    
    private List<FormFields> formFields;
    
    private List<FormOptions> formOptions;
    
    private List<FormParam> formParams;
    
    public FormBean() {
		super();
	}
    
	public FormBean(String id, String moduleid, String name, String code,
			String type, String defaultvalue, String required, String builtin,
			String hidden, String keyattr, String rate, String unit,
			Integer sortindex, Date inserttime, Date updatetime,
			Integer isdelete, String relformid, FormScript formScript,
			List<FormFields> formFields, List<FormOptions> formOptions,
			List<FormParam> formParams) {
		super();
		this.id = id;
		this.moduleid = moduleid;
		this.name = name;
		this.code = code;
		this.type = type;
		this.defaultvalue = defaultvalue;
		this.required = required;
		this.builtin = builtin;
		this.hidden = hidden;
		this.keyattr = keyattr;
		this.rate = rate;
		this.unit = unit;
		this.sortindex = sortindex;
		this.inserttime = inserttime;
		this.updatetime = updatetime;
		this.isdelete = isdelete;
		this.relformid = relformid;
		this.formScript = formScript;
		this.formFields = formFields;
		this.formOptions = formOptions;
		this.formParams = formParams;
	}

    public String getRelformid() {
        return relformid;
    }

    public void setRelformid(String relformid) {
        this.relformid = relformid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue == null ? null : defaultvalue.trim();
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required == null ? null : required.trim();
    }

    public String getBuiltin() {
        return builtin;
    }

    public void setBuiltin(String builtin) {
        this.builtin = builtin == null ? null : builtin.trim();
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden == null ? null : hidden.trim();
    }

    public String getKeyattr() {
        return keyattr;
    }

    public void setKeyattr(String keyattr) {
        this.keyattr = keyattr == null ? null : keyattr.trim();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

	public List<FormFields> getFormFields() {
		return formFields;
	}



	public void setFormFields(List<FormFields> formFields) {
		this.formFields = formFields;
	}



	public List<FormOptions> getFormOptions() {
		return formOptions;
	}



	public void setFormOptions(List<FormOptions> formOptions) {
		this.formOptions = formOptions;
	}



	public List<FormParam> getFormParams() {
		return formParams;
	}



	public void setFormParams(List<FormParam> formParams) {
		this.formParams = formParams;
	}

	public FormScript getFormScript() {
		return formScript;
	}

	public void setFormScript(FormScript formScript) {
		this.formScript = formScript;
	}

	@Override
	public String toString() {
		return "FormBean [id=" + id + ", moduleid=" + moduleid + ", name="
				+ name + ", code=" + code + ", type=" + type
				+ ", defaultvalue=" + defaultvalue + ", required=" + required
				+ ", builtin=" + builtin + ", hidden=" + hidden + ", keyattr="
				+ keyattr + ", rate=" + rate + ", unit=" + unit
				+ ", sortindex=" + sortindex + ", inserttime=" + inserttime
				+ ", updatetime=" + updatetime + ", isdelete=" + isdelete
				+ ", formScript=" + formScript + ", formFields=" + formFields
				+ ", formOptions=" + formOptions + ", formParams=" + formParams
				+ "]";
	}
}