/**
 * 
 */
package com.aspire.ums.cmdb.sync;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lupeng
 *
 */
public class Constants {
	
	public static final String DEVICE_TYPE_HOST = "1";	//大类：服务器
	
	public static final String DEVICE_TYPE_NETWORK = "3";	//大类：网络设备
	
	public static final String DEVICE_CATEGOTY_DICT_CODE = "cmdb.sv.sbtype"; //服务器小类：类型字典编号
	
	public static final String DEVICE_CATEGOTY_PM = "1";	//服务器小类：物理机
	
	public static final String DEVICE_CATEGOTY_VM = "2";	//服务器小类：虚拟机
	
	public static final String  SYNC_MODULE_PM = "m_pm";	//模型：物理机
	
	public static final String  SYNC_MODULE_VM = "m_vm";	//模型 ：虚拟机
	
	public static final String  SYNC_MODULE_NET = "m_nd";	//模型：网络设备
	
	public static final String IP_TYPE_BIZ = "bizIp";
	
	public static final String IP_TYPE_IPMI = "ipmiIp";
	
	public static final String IP_TYPE_MANAGE = "manageIp";
	
	public static final String DICT_CODE_NET_TYPE = "cmdb.net.sbtype";
	
	public static Map<String,String> DICT_MAP = new HashMap<String,String>();		//同步字典
	
	static {
		DICT_MAP.put("cmdb.hourse", "roomId");	//机房
		DICT_MAP.put("flow.sys", "bizSystem");	//业务系统
		DICT_MAP.put("cmdb.net.sbtype", "categoryId");	//网络设备类型
	}
}
