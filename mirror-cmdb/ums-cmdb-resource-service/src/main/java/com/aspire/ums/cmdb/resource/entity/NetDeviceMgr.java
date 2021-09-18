package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NetDeviceMgr {
	/**
	 * 网络设备管理
	 */
	private String ip;
	private String deviceType;
	private String idc;

}
