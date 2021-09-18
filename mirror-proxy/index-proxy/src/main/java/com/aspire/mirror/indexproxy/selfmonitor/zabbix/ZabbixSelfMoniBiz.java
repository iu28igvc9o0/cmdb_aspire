package com.aspire.mirror.indexproxy.selfmonitor.zabbix;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.indexproxy.selfmonitor.zabbix.dao.ZabbixCollectDao;
import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxHistoryQueryParam;
import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxItemHistoryRecord;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ZabbixSelfMoniBiz
 * <p/>
 *
 * 类功能描述: ZABBIX自采集业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Service
@Transactional(transactionManager="zabbixDb_TransactionManager")
public class ZabbixSelfMoniBiz {
	@Autowired
	private ZabbixCollectDao 		zabbixDao;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Long queryHistoryLatestSequenceNo() {
		return zabbixDao.queryHistoryLatestSequenceNo();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<ZbxItemHistoryRecord> queryHistoryUintBySequenceNo(ZbxHistoryQueryParam queryParam) {
		return zabbixDao.queryHistoryUintBySequenceNo(queryParam);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<ZbxItemHistoryRecord> queryHistoryBySequenceNo(ZbxHistoryQueryParam queryParam) {
		return zabbixDao.queryHistoryBySequenceNo(queryParam);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Long queryHistoryUintLatestSequenceNo() {
		return zabbixDao.queryHistoryUintLatestSequenceNo();
	}
}
