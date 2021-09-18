package com.aspire.webbas.epm.core.entity;

import java.util.Date;

import com.aspire.webbas.epm.common.entity.BaseEntity;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.entity]
 * 类名称:    [Task]
 * 类描述:    [一句话描述该类的功能]
 * 创建人:    [王磊]
 * 创建时间:  [2014年8月30日 下午6:43:16]
 */
public class EpmTask extends BaseEntity {
	private String taskId;
	private String processId;
	private String parentId;
	private String rootId;
	private String role;
	private String ownerType;
	private String ownerId;
	private String dealer;
	private String choice;
	private Date createTime;
	private Date dealTime;
	private String status;
	private String dealResult;
	private String fileGroupId;
	private String remark;
	/** 任务是否处理完成 1-完成 0-未完成 */
	private String isDeal;
	/** 任务所在环节的所有任务是否处理完成 1-完成 0-未完成 */
	private String isFinish;
	/** 待查看角色 */
	private String viewer;

	/** 处理完成告知人 */
	private String reviewer;
	private String extra1; //扩展字段1
	private String extra2; //扩展字段2
	private String extra3; //扩展字段3

	/**
	 * 处理模式：
	 * 第一位表示处理人数：多人1 单人0
	 * 第二位标识处理模式：或1 并0
	 * 00-单人 并处理
	 * 01-单人 或处理
	 * 10-多人 并处理
	 * 11-多人 或处理
	*/
	private String dealType;


	/** 额外字段 */
	private String createTimeStart;
	private String createTimeEnd;
	private String dealTimeStart;
	private String dealTimeEnd;


	/**
	 * @return .
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId .
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return .
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId .
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return .
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId .
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return .
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * @param rootId .
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	/**
	 * @return .
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role .
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return .
	 */
	public String getOwnerType() {
		return ownerType;
	}

	/**
	 * @param ownerType .
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * @return .
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId .
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return .
	 */
	public String getDealer() {
		return dealer;
	}

	/**
	 * @param dealer .
	 */
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return .
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * @param choice .
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * @return .
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime .
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return .
	 */
	public Date getDealTime() {
		return dealTime;
	}

	/**
	 * @param dealTime .
	 */
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	/**
	 * @return .
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status .
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return .
	 */
	public String getDealResult() {
		return dealResult;
	}

	/**
	 * @param dealResult .
	 */
	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	/**
	 * @return .
	 */
	public String getFileGroupId() {
		return fileGroupId;
	}

	/**
	 * @param fileGroupId .
	 */
	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	/**
	 * @return .
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark .
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return .
	 */
	public String getIsDeal() {
		return isDeal;
	}

	/**
	 * @param isDeal .
	 */
	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}

	/**
	 * @return .
	 */
	public String getIsFinish() {
		return isFinish;
	}

	/**
	 * @param isFinish .
	 */
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}

	/**
	 * @return .
	 */
	public String getCreateTimeStart() {
		return createTimeStart;
	}

	/**
	 * @param createTimeStart .
	 */
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	/**
	 * @return .
	 */
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	/**
	 * @param createTimeEnd .
	 */
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	/**
	 * @return .
	 */
	public String getDealTimeStart() {
		return dealTimeStart;
	}

	/**
	 * @param dealTimeStart .
	 */
	public void setDealTimeStart(String dealTimeStart) {
		this.dealTimeStart = dealTimeStart;
	}

	/**
	 * @return .
	 */
	public String getDealTimeEnd() {
		return dealTimeEnd;
	}

	/**
	 * @param dealTimeEnd .
	 */
	public void setDealTimeEnd(String dealTimeEnd) {
		this.dealTimeEnd = dealTimeEnd;
	}

	/**
	 * @return .
	 */
	public String getDealType() {
		return dealType;
	}

	/**
	 * @param dealType .
	 */
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	/**
	 * @return .
	 */
	public String getViewer() {
		return viewer;
	}

	/**
	 * @param viewer .
	 */
	public void setViewer(String viewer) {
		this.viewer = viewer;
	}

	/**
	 * @return .
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * @param reviewer .
	 */
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * @return .
	 */
	public String getExtra1() {
		return extra1;
	}

	/**
	 * @param extra1 .
	 */
	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}

	/**
	 * @return .
	 */
	public String getExtra2() {
		return extra2;
	}

	/**
	 * @param extra2 .
	 */
	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}

	/**
	 * @return .
	 */
	public String getExtra3() {
		return extra3;
	}

	/**
	 * @param extra3 .
	 */
	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}
}
