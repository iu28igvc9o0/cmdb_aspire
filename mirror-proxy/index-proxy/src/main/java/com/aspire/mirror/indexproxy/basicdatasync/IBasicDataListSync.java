package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
* 基础数据同步接口    <br/>
* Project Name:index-proxy
* File Name:IBasicDataListSync.java
* Package Name:com.aspire.mirror.indexproxy.basicdatasync
* ClassName: IBasicDataListSync <br/>
* date: 2018年8月14日 下午4:08:14 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IBasicDataListSync<T> {
	
	/**
	* 当前同步项的身份. 如同步的的数据库表的表名 <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public String getSyncItemIdentity();
	
	/**
	* 获取当前同步序列记号. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public int getStartSyncSeq();
	
	/**
	* 获取同步数据. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public Pair<Integer, List<T>> fetchSyncItemDataList();
	
	/**
	* 处理同步数据, 比如：记录同步数据到数据库中、更新同步序列号等. <br/>
	*
	* 作者： pengguihua
	*/  
	public void processSyncDataList();
}
