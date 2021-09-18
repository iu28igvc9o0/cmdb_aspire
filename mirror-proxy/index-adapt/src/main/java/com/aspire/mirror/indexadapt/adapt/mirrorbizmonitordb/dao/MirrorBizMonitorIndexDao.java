package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.dao;

import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MirrorBizMonitorIndexDao {
	
	public List<Items> listActiveBizTriggers();

	Items selectByPrimaryKey(String itemId);
}
