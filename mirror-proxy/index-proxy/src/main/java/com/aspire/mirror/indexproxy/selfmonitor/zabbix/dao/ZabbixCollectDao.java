package com.aspire.mirror.indexproxy.selfmonitor.zabbix.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxHistoryQueryParam;
import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxItemHistoryRecord;

@Mapper
public interface ZabbixCollectDao {
	
	public Long queryHistoryLatestSequenceNo();
	
	public List<ZbxItemHistoryRecord> queryHistoryBySequenceNo(ZbxHistoryQueryParam queryParam);
	
	public Long queryHistoryUintLatestSequenceNo();
	
	public List<ZbxItemHistoryRecord> queryHistoryUintBySequenceNo(ZbxHistoryQueryParam queryParam);
}
