package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class Instance implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String moduleId;
	private Date insertTime;
	private Date updateTime;
	private Integer isDelete;
	
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

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

    @Override
	public String toString() {
		return "Instance [id=" + id + ", name=" + name + ", moduleId=" + moduleId + ", insertTime=" + insertTime + ", updateTime=" + updateTime
				+ ", isDelete=" + isDelete + "]";
	}

}
