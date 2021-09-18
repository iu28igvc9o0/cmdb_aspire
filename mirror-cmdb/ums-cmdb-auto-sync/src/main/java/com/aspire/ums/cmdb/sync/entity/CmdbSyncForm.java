package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

/**
 * @author lupeng
 *
 */
public class CmdbSyncForm implements Serializable{
	
	private static final long serialVersionUID = 8209804545644066200L;

	private String id;
	
	private String moduleCode;
	
	private String formCode;
	
	private String syncCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getSyncCode() {
		return syncCode;
	}

	public void setSyncCode(String syncCode) {
		this.syncCode = syncCode;
	}
	
}
