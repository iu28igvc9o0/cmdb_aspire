package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.List;


public class InstanceModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String moduleId;
	private String circleId;
	private List<FormValue> FormValues;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

    public List<FormValue> getFormValues() {
        return FormValues;
    }

    public void setFormValues(List<FormValue> formValues) {
        FormValues = formValues;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    @Override
    public String toString() {
        return "InstanceModel [name=" + name + ", moduleId=" + moduleId + ", FormValues=" + FormValues + "]";
    }

	
}
