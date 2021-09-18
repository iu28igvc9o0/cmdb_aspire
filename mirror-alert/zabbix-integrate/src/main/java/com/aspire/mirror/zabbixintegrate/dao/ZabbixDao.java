package com.aspire.mirror.zabbixintegrate.dao;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.zabbixintegrate.domain.AlertProxyIdc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.zabbixintegrate.domain.ItemIdMapMoniObj;
import com.aspire.mirror.zabbixintegrate.domain.RawAlert;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;

/**
* DAO接口    <br/>
* Project Name:zabbix-integrate
* File Name:ZabbixDao.java
* Package Name:com.aspire.mirror.zabbixintegrate.dao
* ClassName: ZabbixDao <br/>
* date: 2018年10月19日 上午10:59:00 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Mapper
public interface ZabbixDao {
	
	public ZabbixAlertScanIndex fetchScanIndex(@Param(value = "id") Integer id);
	
	public void insertScanIndex(ZabbixAlertScanIndex initScanIndex);
	
	public void updateScanIndex(ZabbixAlertScanIndex updateData);
	
	public List<RawAlert> fetchAlertEventList(Map<String, Object> params);
	
	public List<ItemIdMapMoniObj> fetchMoniObjByItemIdArr(List<Integer> itemIdList);

	public List<AlertProxyIdc> selectAllProxyIdc();
}
