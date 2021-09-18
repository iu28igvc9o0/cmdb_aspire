package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.List;

public class DynamicInstanceColumn  implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String moduleId;
    private String insertTime;
    private String updateTime;
    private String moduleName;
    private String tagName;
    private String circleName;

    private List<FormValue> formValues;

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

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<FormValue> getFormValues() {
        return formValues;
    }

    public void setFormValues(List<FormValue> formValues) {
        this.formValues = formValues;
    }

    @Override
    public String toString() {
        return "DynamicInstanceColumn [name=" + name + ", moduleId=" + moduleId + ", insertTime=" + insertTime
                + ", updateTime=" + updateTime + ", moduleName=" + moduleName + ", tagName=" + tagName
                + ", circleName=" + circleName + ", formValues=" + formValues + "]";
    }

}