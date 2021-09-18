package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

/**
 * @author lupeng
 *
 */
public class FormValue  implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String formId;
	private String formCode;
    private String formValue;
    private String instanceId;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormValue() {
        return formValue;
    }

    public void setFormValue(String formValue) {
        this.formValue = formValue;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "FormValue [id=" + id + ", formId=" + formId + ", formCode=" + formCode + ", formValue=" + formValue
                + ", instanceId=" + instanceId +"]";
    }
 


}