package com.aspire.ums.cmdb.repertory.entity;

import com.aspire.ums.cmdb.maintain.entity.FormValue;

import java.util.List;
import java.util.Map;

public class BatchUpdateData {

    private static final long serialVersionUID = 1L;

    private String moduleId;

    private Map<String, FormValue> formValues;

    private List<String> InstanceNames;

    public Map<String, FormValue> getFormValues() {
        return formValues;
    }

    public void setFormValues(Map<String, FormValue> formValues) {
        this.formValues = formValues;
    }

    public List<String> getInstanceNames() {
        return InstanceNames;
    }

    public void setInstanceNames(List<String> instanceNames) {
        InstanceNames = instanceNames;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return "BatchUpdateData{" +
                "moduleId='" + moduleId + '\'' +
                ", formValues=" + formValues +
                ", InstanceNames=" + InstanceNames +
                '}';
    }
}
