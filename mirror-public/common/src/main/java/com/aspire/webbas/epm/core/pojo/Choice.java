package com.aspire.webbas.epm.core.pojo;

/**
 * 项目名称: [webbas-component-epm] 包名: [com.aspire.webbas.epm.core.pojo] 类名称:
 * [Choice] 类描述: [环节选择对象] 创建人: [王磊] 创建时间: [2014年8月29日 上午11:07:50]
 */
public class Choice {
	/**
	 * 选择值，通过该值流转到指定流程
	 */
	private String value;

	/**
	 * 选择名字
	 */
	private String name;

	/**
	 * 目标流程节点
	 */
	private String to;

	/**
	 * 处理类型dealType（针对下一环节与当前环节无关） or-多处理人情况只要有1人处理完成则流程继续流转
	 * and-多处理人情况所有人处理完成则流程继续流转
	 */
	private String dealType;

	/**
	 * 流程类型 分为fork和join，当类型为fork时每个并发流程会根据choice分叉到不同的环节
	 */
	private String processType;

	/**
	 * 下一步处理人（多个，以逗号分隔，可以是角色、人、部门，开发人员自行配置）
	 */
	private String role;

	/**
	 * 抄送查看者（多个，以逗号分隔，可以是角色、人、部门，开发人员自行配置）
	 */
	private String viewer;

	/**
	 * 流程回退（如果该节点有多人处理只要一人回退则流程全部回退）
	 */
	private String isBack;

	/**
	 * 流程回退的层级（针对分支流程）（root表示回退到根流程，parent表示回退到父流程）
	 */
	private String backLevel;

	/**
	 * @return .
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value .
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return .
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to .
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return .
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @param processType .
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
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
	public String getIsBack() {
		return isBack;
	}

	/**
	 * @param isBack .
	 */
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	/**
	 * @return .
	 */
	public String getBackLevel() {
		return backLevel;
	}

	/**
	 * @param backLevel .
	 */
	public void setBackLevel(String backLevel) {
		this.backLevel = backLevel;
	}

	/**
	 * @return .
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name .
	 */
	public void setName(String name) {
		this.name = name;
	}

}
