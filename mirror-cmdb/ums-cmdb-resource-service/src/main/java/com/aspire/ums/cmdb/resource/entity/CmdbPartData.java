package com.aspire.ums.cmdb.resource.entity;
/**
 * 
 * @author zhangwangping
 * @version 1.0
 * 局数据对象
 */
public class CmdbPartData {
	private String id;
	private String code;  //编号
	private String updateFlag;//变更标志
	private String updateDescription;//变更说明
	private String effectiveDate;//生效时间
	private String disableDate;//失效时间
	private String makeRange;//制作范围
	private String businessCode;//业务编码
	private String businessName;//业务名称
	private String businessDenominate;//业务命名
	private String usageWay;//使用方式
	private String joinAPN;//接入APN
	private String ipAddress;//三层ip
	private String protocolNumber;//协议号
	private String portNumber;//四层端口
	private String urlName;//七层URL
	private String fluxExpenses;//流量费
	private String descriptionIpFunction;//备注说明（ip的作用）
	private String descriptionDialandTestNum;//备注说明2（如何拨测到）
	private String checkRemark;//检查说明
	private String remark;//备注
	private String province;//省份
	private String resource;//来源
	private String createTime;//创建日期
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	public String getUpdateDescription() {
		return updateDescription;
	}
	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(String disableDate) {
		this.disableDate = disableDate;
	}
	public String getDescriptionIpFunction() {
		return descriptionIpFunction;
	}
	public void setDescriptionIpFunction(String descriptionIpFunction) {
		this.descriptionIpFunction = descriptionIpFunction;
	}
	public String getDescriptionDialandTestNum() {
		return descriptionDialandTestNum;
	}
	public void setDescriptionDialandTestNum(String descriptionDialandTestNum) {
		this.descriptionDialandTestNum = descriptionDialandTestNum;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getMakeRange() {
		return makeRange;
	}
	public void setMakeRange(String makeRange) {
		this.makeRange = makeRange;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessDenominate() {
		return businessDenominate;
	}
	public void setBusinessDenominate(String businessDenominate) {
		this.businessDenominate = businessDenominate;
	}
	public String getUsageWay() {
		return usageWay;
	}
	public void setUsageWay(String usageWay) {
		this.usageWay = usageWay;
	}
	public String getJoinAPN() {
		return joinAPN;
	}
	public void setJoinAPN(String joinAPN) {
		this.joinAPN = joinAPN;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getProtocolNumber() {
		return protocolNumber;
	}
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
	public String getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public String getFluxExpenses() {
		return fluxExpenses;
	}
	public void setFluxExpenses(String fluxExpenses) {
		this.fluxExpenses = fluxExpenses;
	}		
}
