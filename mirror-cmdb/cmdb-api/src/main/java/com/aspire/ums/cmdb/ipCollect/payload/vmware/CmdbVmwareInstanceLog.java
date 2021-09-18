package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CmdbVmwareInstanceLog implements Serializable {

	/**
	 * 
	 */
	private String id;
	/**
	 * 实例id
	 */
	private String instanceId;
	/**
	 * 
	 */
	private String eventType;
	/**
	 * 
	 */
	private String eventId;
	/**
	 * 
	 */
	private String system;
	/**
	 * 
	 */
	private String objectId;
	/**
	 * 
	 */
	private String objectName;
	/**
	 * 
	 */
	private Integer objectVersion;
	/**
	 * 
	 */
	private Integer version;
	/**
	 * 
	 */
	private Long optTimestamp;
	/**
	 * 
	 */
	private String operator;
	/**
	 * 
	 */
	private String optDesc;
	/**
	 * 
	 */
	private String targetCategory;
	/**
	 * 
	 */
	private String targetId;
	/**
	 * 
	 */
	private String targetName;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String optContent;
	
	
	/**
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 实例id
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * 实例id
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	/**
	 * 
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * 
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	/**
	 * 
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * 
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * 
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * 
	 */
	public void setSystem(String system) {
		this.system = system;
	}
	/**
	 * 
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * 
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	/**
	 * 
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * 
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	/**
	 * 
	 */
	public Integer getObjectVersion() {
		return objectVersion;
	}

	/**
	 * 
	 */
	public void setObjectVersion(Integer objectVersion) {
		this.objectVersion = objectVersion;
	}
	/**
	 * 
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 
	 */
	public Long getOptTimestamp() {
		return optTimestamp;
	}

	/**
	 * 
	 */
	public void setOptTimestamp(Long optTimestamp) {
		this.optTimestamp = optTimestamp;
	}
	/**
	 * 
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * 
	 */
	public String getOptDesc() {
		return optDesc;
	}

	/**
	 * 
	 */
	public void setOptDesc(String optDesc) {
		this.optDesc = optDesc;
	}
	/**
	 * 
	 */
	public String getTargetCategory() {
		return targetCategory;
	}

	/**
	 * 
	 */
	public void setTargetCategory(String targetCategory) {
		this.targetCategory = targetCategory;
	}
	/**
	 * 
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	/**
	 * 
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * 
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	/**
	 * 
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 
	 */
	public String getOptContent() {
		return optContent;
	}

	/**
	 * 
	 */
	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}
}
