package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormFields;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.FormParam;

public class FormValue  implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String formId;
	private String formCode;
    private String formValue;
    private String instanceId;
    
    private Object transforDate;
    
    private Form form;
    
    private List<FormOptions> formOptions;
    
    private List<FormFields> formFields;
    
    private List<FormParam> formParams;
    
    private Map<String,Object> casoptions;

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


    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<FormOptions> getFormOptions() {
        return formOptions;
    }

    public void setFormOptions(List<FormOptions> formOptions) {
        this.formOptions = formOptions;
    }

    public List<FormFields> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormFields> formFields) {
        this.formFields = formFields;
    }

    public List<FormParam> getFormParams() {
        return formParams;
    }

    public void setFormParams(List<FormParam> formParams) {
        this.formParams = formParams;
    }

    
    
    public Object getTransforDate() {
        return transforDate;
    }

    public void setTransforDate(Object transforDate) {
        this.transforDate = transforDate;
    }
    
    public Map<String, Object> getCasoptions() {
        return casoptions;
    }

    public void setCasoptions(Map<String, Object> casoptions) {
        this.casoptions = casoptions;
    }

    @Override
    public String toString() {
        return "FormValue [id=" + id + ", formId=" + formId + ", formCode=" + formCode + ", formValue=" + formValue
                + ", instanceId=" + instanceId + ", form=" + form + "]";
    }
 


}