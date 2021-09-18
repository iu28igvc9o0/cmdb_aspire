package com.aspire.ums.cdn.dao.zabbix;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ZabbixDao {
	public List<String> listItemVal();
}
