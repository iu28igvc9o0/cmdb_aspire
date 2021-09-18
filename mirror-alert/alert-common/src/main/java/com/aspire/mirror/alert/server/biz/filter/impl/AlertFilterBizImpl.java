package com.aspire.mirror.alert.server.biz.filter.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.aspire.mirror.alert.server.biz.filter.AlertFilterBiz;
import com.aspire.mirror.alert.server.dao.filter.AlertFilterDao;
import com.aspire.mirror.alert.server.dao.filter.AlertFilterSceneDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsStatisticDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsTransferDao;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilter;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterScene;
import com.aspire.mirror.alert.server.dao.alert.po.AlertReportOperateRecord;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsTransfer;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertFilterBizImpl implements AlertFilterBiz{
	Logger LOGGER = LoggerFactory.getLogger(AlertFilterBizImpl.class);
	@Autowired
    private AlertsStatisticDao alertsStatisticDao;

    @Autowired
    private AlertFilterDao alertFilterDao; 
    
    @Autowired
    AlertFilterSceneDao alertFilterSceneDao;
    
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    
    @Autowired
    private AlertsTransferDao alertsTransferDao;
    
	@Override
	public void insert(AlertFilter filter) {
		alertFilterDao.insert(filter);
	}

	@Override
	public AlertFilter selectByPrimaryKey(String id) {
		return alertFilterDao.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public PageResponse<AlertFilter> pageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
        int count = alertFilterDao.pageListCount(page);
        PageResponse<AlertFilter> pageResponse = new PageResponse<AlertFilter>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertFilter> listAlertFilter = alertFilterDao.pageList(page);
            pageResponse.setResult(listAlertFilter);
        }
        return pageResponse;
	}


	@Override
	public void update(AlertFilter filter) {
		alertFilterDao.update(filter);
		
	}

	@Override
	public void delete(String id) {
		alertFilterDao.deleteByPrimaryKey(Integer.parseInt(id));
		
	}

	@Override
	public AlertFilter getByName(String name) {
		return alertFilterDao.getByName(name);
	}

	@Override
	public List<AlertFilter> getAllFilter(boolean filterFlag,String operateUser) {
		if (filterFlag) {
			return alertFilterDao.selectFilterAll(operateUser);
		}else {
			return alertFilterDao.selectAll();
		}
	}
	
	/**
     * 警报看板筛选
     * @param pageRequest
     * @return
     */
   @Override
    public PageResponse<AlertsVo> filterSelect(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = 0;
        if (null == pageRequest.getDynamicQueryFields().get("pageFlag")) {
        	count = alertsStatisticDao.filterPageListCount(page);
        }else {
        	page.setPageSize(null);
        }
        PageResponse<AlertsVo> pageResponse = new PageResponse<AlertsVo>();
        pageResponse.setCount(count);
        if (count > 0 || page.getPageSize()== null) {
            List<Alerts> listAlerts = alertsStatisticDao.filterPageList(page);
            List<AlertsVo> listDTO = TransformUtils.transform(AlertsVo.class, listAlerts);
            for (AlertsVo alertHis: listDTO) {
              	 String alertId = alertHis.getAlertId();
                   List<AlertsRecord> recordList = alertsRecordDao.selectRecordByPrimaryKey(alertId);
                   
                   List<AlertReportOperateRecord> reportRecord = alertsRecordDao.selectReportOperationRecordByAlertKey(alertId);
                   if (!CollectionUtils.isEmpty(reportRecord)) {
                   	   alertHis.setReportTime(reportRecord.get(0).getCreateTime());
                       alertHis.setReportStatus(reportRecord.get(0).getStatus());
                       alertHis.setReportType(reportRecord.get(0).getReportType());
                   }
                   
                   if (!CollectionUtils.isEmpty(recordList)) {
                       for (AlertsRecord record: recordList) {
                           String operationType = record.getOperationType();
                           switch (operationType) {
                               case "0":
                                   if (alertHis.getTransTime() == null || alertHis.getTransTime().before(record.getOperationTime())) {
                                       alertHis.setTransUser(record.getUserName());
                                       alertHis.setTransStatus(Integer.parseInt(record.getOperationStatus()));
                                       alertHis.setTransTime(record.getOperationTime());
                                   }
                                   break;
                               case "1":
                                   if (alertHis.getConfirmedTime() == null || alertHis.getConfirmedTime().before(record.getOperationTime())) {
                                       alertHis.setConfirmedUser(record.getUserName());
                                       alertHis.setConfirmedTime(record.getOperationTime());
                                       alertHis.setConfirmedContent(record.getContent());
                                   }
                                   break;
                               case "2":
                                   if (alertHis.getDeliverTime() == null || alertHis.getDeliverTime().before(record.getOperationTime())) {
                                       alertHis.setDeliverTime(record.getOperationTime());
                                       alertHis.setDeliverStatus(Integer.parseInt(record.getOperationStatus()));
                                   }
                                   break;
                               default:
                                   break;
                           }
                       }
                   }
                   if (org.apache.commons.lang.StringUtils.isEmpty(alertHis.getConfirmedUser())) {
                       List<AlertsTransfer> transferList = alertsTransferDao.selectByAlertId(alertHis.getAlertId());
                       Set<String> nameSet = new HashSet<>();
                       for (AlertsTransfer transfer: transferList) {
                           nameSet.add(transfer.getConfirmUserName());
                       }
                       StringBuilder username = new StringBuilder("");
                       for (String name: nameSet) {
                           username.append(name).append(",");
                       }
                       if (username.length() > 0) {
                           username.substring(0, username.length()-1);
                       }
                       alertHis.setToConfirmUser(username.toString());
                   }
              }
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }
   
   @Override
    public int filterSelectCount(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertsStatisticDao.filterPageListCount(page);
        return count;
    }

   @Override
   public List<AlertFilterScene> getSceneByid(int filterId, String operateUser) {
       return alertFilterSceneDao.getByfilterId(filterId, operateUser);
   }

	@Override
	@Transactional(rollbackFor = { Exception.class }) 
	public void copy(AlertFilter filter) {
		int id = filter.getId();
		filter.setId(null);
		try {
			alertFilterDao.insert(filter);
			List<AlertFilterScene>  scenes = getSceneByid(id,null);
			if(scenes.size()>0) {
				for(AlertFilterScene s:scenes) {
					s.setId(null);
					s.setFilterId(filter.getId());
					s.setCreater(filter.getCreater());
					s.setEditer("");
					s.setCreated_at(filter.getCreated_at());
				}
				alertFilterSceneDao.batchInsert(scenes);
			}
		}catch (Exception e) {  
            //就是这一句了, 加上之后抛了异常就能回滚（有这句代码就不需要再手动抛出运行时异常了）
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  
       }  
		
	}
   
}
