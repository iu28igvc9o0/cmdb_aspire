/**
 * 
 */
package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

/**
 * @author lupeng
 *
 */
public class SyncEntity implements Serializable {

	private static final long serialVersionUID = 2776794523049304657L;

	private String id;

	private String hostName;

	private String bizIp;

	private String ipmiIp;

	private String manageIp;

	private String roomId;

	private String bizSystem;

	private String deviceType; // 服务器，网络设备

	private String deviceCatgory; // 物理机，虚拟机，不同的网络设备

	private String systemVersion;

	private String brand;

	private String hostEnv;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getBizSystem() {
		return bizSystem;
	}

	public void setBizSystem(String bizSystem) {
		this.bizSystem = bizSystem;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceCatgory() {
		return deviceCatgory;
	}

	public void setDeviceCatgory(String deviceCatgory) {
		this.deviceCatgory = deviceCatgory;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getHostEnv() {
		return hostEnv;
	}

	public void setHostEnv(String hostEnv) {
		this.hostEnv = hostEnv;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getBizIp() {
		return bizIp;
	}

	public void setBizIp(String bizIp) {
		this.bizIp = bizIp;
	}

	public String getIpmiIp() {
		return ipmiIp;
	}

	public void setIpmiIp(String ipmiIp) {
		this.ipmiIp = ipmiIp;
	}

	public String getManageIp() {
		return manageIp;
	}

	public void setManageIp(String manageIp) {
		this.manageIp = manageIp;
	}
	
}
