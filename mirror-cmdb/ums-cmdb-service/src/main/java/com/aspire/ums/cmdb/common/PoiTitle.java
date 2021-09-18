package com.aspire.ums.cmdb.common;

import java.util.ArrayList;
import java.util.List;

import com.aspire.ums.cmdb.maintain.entity.CasoptionsBean;
import com.aspire.ums.cmdb.module.entity.FormOptions;

/**
 * excel表头列
 * 
 * @author lupeng
 * 
 */
public class PoiTitle implements Cloneable {

	private Integer rowNum; // 行编号

	private Integer columnNum; // 列编号

	private String field; // 字段名

	private String fieldValue; // 字段值

	private String fieldType; // 字段类别+source,+target
	
	private String formId; 

	private String comment; // 字段注释

	private String format; // 字段格式

	private String def; // 默认值

	private String restriction; // 关系类型

	private Integer minLength; // 最小长度

	private Integer maxLength; // 最大长度

	private String validation; // 格式验证

	private Integer min; // 最小值

	private Integer max; // 最大值

	private Integer precision; // 小数位数

	private Boolean formatDate; // 日期格式化

	private List<String> options; // 可选项
	
	private List<FormOptions> formOptions; // 可选项
	
	private List<CasoptionsBean> casoptionsBeans; // 级联项
	
	private String formCode;
	
	private String moduleId;
	
	private String formName;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Boolean getFormatDate() {
		return formatDate;
	}

	public void setFormatDate(Boolean formatDate) {
		this.formatDate = formatDate;
	}
	
	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<FormOptions> getFormOptions() {
        return formOptions;
    }

    public void setFormOptions(List<FormOptions> formOptions) {
        this.formOptions = formOptions;
    }

    public List<CasoptionsBean> getCasoptionsBeans() {
        return casoptionsBeans;
    }

    public void setCasoptionsBeans(List<CasoptionsBean> casoptionsBeans) {
        this.casoptionsBeans = casoptionsBeans;
    }
    
    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }
    
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    @Override
	public Object clone() {
		PoiTitle pt = null;
		try {
			pt = (PoiTitle) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if(formOptions!=null){
		pt.formOptions=new ArrayList<FormOptions>(formOptions);    
		}
		if(casoptionsBeans!=null){
		pt.casoptionsBeans=new ArrayList<CasoptionsBean>(casoptionsBeans);   
		}
		return pt;
	}

}
