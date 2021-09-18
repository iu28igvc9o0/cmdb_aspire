package com.aspire.mirror.indexadapt.adapt.inspectiondb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.indexadapt.adapt.inspectiondb.dao.InspectionIndexDao;
import com.aspire.mirror.indexadapt.adapt.inspectiondb.model.InpsectionRawIndex;

/**
* 巡检数据库适配指标数据业务类   <br/>
* Project Name:index-proxy
* File Name:InspectionIndexService.java
* Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb.service
* ClassName: InspectionIndexService <br/>
* date: 2018年8月6日 下午5:08:56 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Service
@Transactional("inspectionDb_TransactionManager")
@ConditionalOnProperty("indexAdapter.inspectionDb.switch")
public class InspectionIndexService {
	@Autowired
	private InspectionIndexDao indexDao;
	
	@Transactional(readOnly=true)
	public List<InpsectionRawIndex> fetchInspectionIndexList(int startIdxSeq) {
		return indexDao.fetchInspectionRawIndexList(startIdxSeq);
	}
}
