package com.aspire.app.ums.zabbixApi.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.app.ums.util.XstreamUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ZabbixApiServerInfos")
public class ZbxApiSvrInfoHolder {
	private static final Logger logger = LoggerFactory.getLogger(ZbxApiSvrInfoHolder.class);
	
	@XStreamImplicit(itemFieldName="ApiServerInfo")
	private List<ZbxApiSvrInfo> 	serverInfoList;
	
	// 缓存分组名 与  ZABBIX主机信息映射 
	private static ConcurrentHashMap<String, List<ZbxApiSvrInfo>> GROUP_MAP_ZBXSVR = new ConcurrentHashMap<String, List<ZbxApiSvrInfo>>();
	
	/** 
	 * 功能描述: 获取指定分组下的所有主机信息， 使用clazz指定的 ZbxApiSvrInfo 子类对象
	 * <p>
	 * @param groupName
	 * @param clazz 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ZbxApiSvrInfo> List<T> getGroupSvrInfoList(String groupName, Class<T> clazz) {
		List<ZbxApiSvrInfo> groupList = getGroupSvrInfoList(groupName);
		List<T> parseList = new ArrayList<T>();
		for (ZbxApiSvrInfo item : groupList) {
			parseList.add((T)item);
		}
		return parseList;
	}
	
	/** 
	 * 功能描述: 获取指定分组下的所有主机信息  
	 * <p>
	 * @param groupName
	 * @return
	 */
	public static List<ZbxApiSvrInfo> getGroupSvrInfoList(String groupName) {
		List<ZbxApiSvrInfo> groupList = GROUP_MAP_ZBXSVR.get(groupName);
		if (groupList == null) {
			groupList = new ArrayList<ZbxApiSvrInfo>();
		}
		return groupList;
	}
	
	/** 
	 * 功能描述: 添加  groupList 指定的主机信息   到   groupName 分组, 多次加载时，后面的配置覆盖以前的配置
	 * <p>
	 * @param groupName
	 * @param svrList
	 * @throws Exception
	 */
	public static <T extends ZbxApiSvrInfo> void loadZbxServerInfos(String groupName, List<T> svrList) throws Exception {
		if (svrList == null) {
			return;
		}
		
		List<ZbxApiSvrInfo> toCacheList = new ArrayList<ZbxApiSvrInfo>(svrList);
		if (GROUP_MAP_ZBXSVR.get(groupName) == null) {
			GROUP_MAP_ZBXSVR.put(groupName, toCacheList);
		} else {
			GROUP_MAP_ZBXSVR.replace(groupName, toCacheList);
		}
	}
	
	/** 
	 * 功能描述: 从配置文件加载   ZABBIX Server 配置信息,  放入 groupName 指定的分组中
	 * <p>
	 * @param groupName
	 * @param filePath
	 */
	public static void loadZbxServerInfos(String groupName, String filePath) throws Exception {
		InputStream is = new FileInputStream(filePath);
		loadZbxServerInfos(groupName, is, true);
	}
	
	/** 
	 * 功能描述: 从流读入 ZABBIX Server 配置信息,  放入 groupName 指定的分组中,  默认关闭流
	 * <p>
	 * @param groupName	配置归类的组名
	 * @param is		XML配置输入流
	 */
	public static void loadZbxServerInfos(String groupName, InputStream is) throws Exception {
		loadZbxServerInfos(groupName, is, true);
	}
	
	/** 
	 * 功能描述: 从流读入 ZABBIX Server 配置信息,  放入 groupName 指定的分组中,  根据autoClose决定是否关闭流  
	 * <p>
	 * @param groupName
	 * @param is
	 * @param autoClose
	 * @throws Exception
	 */
	public static void loadZbxServerInfos(String groupName, InputStream is, boolean autoClose) throws Exception {
		ZbxApiSvrInfoHolder loadConfig = loadFromConfig(is, autoClose);
		if (loadConfig == null) {
			return;
		}
		loadZbxServerInfos(groupName, loadConfig.serverInfoList);
	}
	
	/** 
	 * 功能描述: 读取配置  
	 * <p>
	 */
	private static ZbxApiSvrInfoHolder loadFromConfig(InputStream is, boolean autoClose) {
		ZbxApiSvrInfoHolder readObj = null;
		try {
			readObj = XstreamUtils.xml2Object(is, ZbxApiSvrInfoHolder.class);
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			if (autoClose) {
				IOUtils.closeQuietly(is);
			}
		}
		return readObj;
	}
	
	public static void main(String[] args) throws Exception {
		String groupName = "test";
		InputStream is = ZbxApiSvrInfoHolder.class.getResourceAsStream("/ZabbixServersConfig.xml");
		ZbxApiSvrInfoHolder.loadZbxServerInfos(groupName, is);
		List<ZbxApiSvrInfo> groupList = ZbxApiSvrInfoHolder.getGroupSvrInfoList(groupName);
		System.out.println(groupList.size());
		System.out.println(groupList.get(0).getUrl());
	}
}
