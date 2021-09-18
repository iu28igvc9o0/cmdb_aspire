package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.mirror.indexadapt.adapt.migubizmonitordb.model.BusinessTrigger;

@Mapper
public interface MiguBizMonitorIndexDao {
	
	public List<BusinessTrigger> listActiveBizTriggers();
	
}
