package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class ModuleRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String sourceModuleId;
	private String targetModuleId;
	private String relationId;
	private String builtin;
	private String restriction;
	private Date insertTime;
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceModuleId() {
		return sourceModuleId;
	}

	public void setSourceModuleId(String sourceModuleId) {
		this.sourceModuleId = sourceModuleId;
	}

	public String getTargetModuleId() {
		return targetModuleId;
	}

	public void setTargetModuleId(String targetModuleId) {
		this.targetModuleId = targetModuleId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getBuiltin() {
		return builtin;
	}

	public void setBuiltin(String builtin) {
		this.builtin = builtin;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	@Override
	public String toString() {
		return "ModuleRelation [sourceModuleId=" + sourceModuleId + ", targetModuleId=" + targetModuleId + ", relationId=" + relationId
				+ ", builtin=" + builtin + ", restriction=" + restriction + ", insertTime=" + insertTime + ", updateTime=" + updateTime + "]";
	}

}