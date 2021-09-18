package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ThirdPartyMaintenance implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;//id
	private String type;//类型
	private String deviceName;//设备名称
	private String deviceVender;//设备厂商
	private String systemName;//系统名称
	private String deviceType;//设备型号
	private String deviceSerialNumber;//设备序列号
	private String networkTime;//入网时间
	private String dateOfWarranty;//出保时间01
	private String dateOfWarranty02;//出保时间02
	private String dateOfWarranty03;//出保时间03
	private Integer deviceNumber;//CPU、内存、内置硬盘、网络接口类型及数量
	private String configurationSoftware;//是否配置双机热备软件和其它随机软件
	private String osNameVersion;//操作系统名称及版本
	private String systemInergrationName;//系统集成商名称
	private String serviceLevelPurchase;//拟购买的服务级别
	private String computerRoom;//机房
	private String cabinet;//机柜
	private String remark;//备注
	private String thirdpartServiceChoice;//第三方服务的选择原则
	private String maintenanceServicePurchase;//拟购买的维保服务时限
	
	private String service1;//一级业务
	private String service2;//二级业务
		
	public String getDateOfWarranty02() {
		return dateOfWarranty02;
	}
	public void setDateOfWarranty02(String dateOfWarranty02) {
		this.dateOfWarranty02 = dateOfWarranty02;
	}
	public String getDateOfWarranty03() {
		return dateOfWarranty03;
	}
	public void setDateOfWarranty03(String dateOfWarranty03) {
		this.dateOfWarranty03 = dateOfWarranty03;
	}
	public String getService1() {
		return service1;
	}
	public void setService1(String service1) {
		this.service1 = service1;
	}
	public String getService2() {
		return service2;
	}
	public void setService2(String service2) {
		this.service2 = service2;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceVender() {
		return deviceVender;
	}
	public void setDeviceVender(String deviceVender) {
		this.deviceVender = deviceVender;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	public String getNetworkTime() {
		return networkTime;
	}
	public void setNetworkTime(String networkTime) {
		this.networkTime = networkTime;
	}
	public String getDateOfWarranty() {
		return dateOfWarranty;
	}
	public void setDateOfWarranty(String dateOfWarranty) {
		this.dateOfWarranty = dateOfWarranty;
	}
	public Integer getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(Integer deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getConfigurationSoftware() {
		return configurationSoftware;
	}
	public void setConfigurationSoftware(String configurationSoftware) {
		this.configurationSoftware = configurationSoftware;
	}
	public String getOsNameVersion() {
		return osNameVersion;
	}
	public void setOsNameVersion(String osNameVersion) {
		this.osNameVersion = osNameVersion;
	}
	public String getSystemInergrationName() {
		return systemInergrationName;
	}
	public void setSystemInergrationName(String systemInergrationName) {
		this.systemInergrationName = systemInergrationName;
	}
	public String getServiceLevelPurchase() {
		return serviceLevelPurchase;
	}
	public void setServiceLevelPurchase(String serviceLevelPurchase) {
		this.serviceLevelPurchase = serviceLevelPurchase;
	}
	public String getComputerRoom() {
		return computerRoom;
	}
	public void setComputerRoom(String computerRoom) {
		this.computerRoom = computerRoom;
	}
	public String getCabinet() {
		return cabinet;
	}
	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getThirdpartServiceChoice() {
		return thirdpartServiceChoice;
	}
	public void setThirdpartServiceChoice(String thirdpartServiceChoice) {
		this.thirdpartServiceChoice = thirdpartServiceChoice;
	}
	public String getMaintenanceServicePurchase() {
		return maintenanceServicePurchase;
	}
	public void setMaintenanceServicePurchase(String maintenanceServicePurchase) {
		this.maintenanceServicePurchase = maintenanceServicePurchase;
	}
	
	
	
}
