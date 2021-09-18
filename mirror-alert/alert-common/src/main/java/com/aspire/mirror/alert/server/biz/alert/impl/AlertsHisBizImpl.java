package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AlertsHisBiz;
import com.aspire.mirror.alert.server.dao.alert.AlertsDetailDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsHisDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 告警实现类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.biz.impl
 * 类名称:    AlertsHisBizImpl.java
 * 类描述:    告警实现类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Service
public class AlertsHisBizImpl implements AlertsHisBiz {
	
	Logger LOGGER = LoggerFactory.getLogger(AlertsHisBizImpl.class);

    @Autowired
    private AlertsHisDao alertHisDao;

    @Autowired
    private AlertsDetailDao alertsDetailDao;

    /**
     * 告警创建
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    @Override
    public String insert(AlertsHisVo alertsDTO) {

        if (null == alertsDTO) {
            LOGGER.error("method[insert] param[alertsDTO] is null");
            throw new RuntimeException("param[alertsDTO] is null");
        }
        AlertsHis alerts = TransformUtils.transform(AlertsHis.class, alertsDTO);
        if (alerts.getAlertId() == null) {
            String uuid = "";
            synchronized (uuid) {
                uuid = UUID.randomUUID().toString();
            }
            alerts.setAlertId(uuid);
        }
        alertHisDao.insert(alerts);
        return alertsDTO.getAlertId();
    }

    /**
     * @param alertId 告警ID
     * @return
     */
    @Override
    public AlertsHisVo selectByPrimaryKey(String alertId) {
        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[alertId] is null");
            return null;
        }
        AlertsHis alerts = alertHisDao.selectByPrimaryKey(alertId);
        if (alerts == null) {
            return null;
        }
        AlertsHisVo alertsDTO = TransformUtils.transform(AlertsHisVo.class, alerts);
        return alertsDTO;
    }

    
    /**历史告警上报记录分页
     * @param alertId 告警ID
     * @return
     */
    @Override
	public PageResponse<AlertsDetail> alertGenerateListByPage(String alertId, String pageNo, String pageSize) {
        PageResponse<AlertsDetail> pageResponse=new PageResponse<AlertsDetail>();

        Map<String, Object> hashMap=new HashMap<String, Object>();
        int pageSize1=Integer.valueOf(pageSize);
        int pageNo1=Integer.valueOf(pageNo);
        hashMap.put("pageNo", (pageNo1 - 1) * pageSize1);
        hashMap.put("pageSize", pageSize1);
        hashMap.put("alertId", alertId);

        int count=alertsDetailDao.countByAlertId(hashMap);
        List<AlertsDetail> alertDetailList = alertsDetailDao.selectByAlertId(hashMap);

//        List<AlertGenResp> alertGenRespList=new ArrayList<AlertGenResp>();
//        for ( AlertsDetail alertsDetail : alertDetailList ){
//
//            AlertGenResp alertGenResp=new AlertGenResp();
//
//            BeanUtils.copyProperties(alertsDetail, alertGenResp);
//
//            alertGenRespList.add(alertGenResp);
//        }
        pageResponse.setCount (count);
        pageResponse.setResult(alertDetailList);
        return pageResponse;
	}
    
 
    
    /**历史告警 相关告警
     * @param alertId 告警ID
     * @return
     */

	@Override
	public List<AlertsHisVo> selectAlertGenerateList(String alertId) {
		
		  AlertsHis alertsHis = alertHisDao.selectByPrimaryKey(alertId);
	  		
  		 String deviceIp=alertsHis.getDeviceIp();
         String moniObject=alertsHis.getMoniObject();
         String alertLevel=alertsHis.getAlertLevel();
	        
	        
        List<AlertsHis> alertHisList = alertHisDao.selectAllAlertGenerateList(deviceIp ,moniObject ,alertLevel );
        
        if (alertHisList == null) {
            return null;
        }
        
        List<AlertsHisVo> alertHisDtolist=new ArrayList<AlertsHisVo>();
        for (AlertsHis alerthis : alertHisList   ) {
        	AlertsHisVo alerthisDTO = TransformUtils.transform(AlertsHisVo.class, alerthis);
        	alertHisDtolist.add(alerthisDTO);
			
		  }  
        return alertHisDtolist;
	 
	}

    @Override
    public PageResponse<AlertsHisVo> getAlertHisList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertHisDao.getAlertHisCount(page);
        PageResponse<AlertsHisVo> pageResponse = new PageResponse<AlertsHisVo>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertsHis> listAlertsHis = alertHisDao.getAlertHisList(page);
            List<AlertsHisVo> listDTO = TransformUtils.transform(AlertsHisVo.class, listAlertsHis);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    /**
     * 批量新增历史告警数据
     *
     * @param alertsHisVoList 需新增历史告警列表数据
     * @return List<AlertsHisDTO>
     */
    @Override
    public List<AlertsHisVo> insertByBatch(List<AlertsHisVo> alertsHisVoList) {
        if (CollectionUtils.isEmpty(alertsHisVoList)) {
            LOGGER.error("method[insertByBatch] param[alertsHisDTOList] is null");
            throw new RuntimeException("param[alertsHisDTOList] is null");
        }
        List<AlertsHis> listAlert = TransformUtils.transform(AlertsHis.class, alertsHisVoList);
        alertHisDao.insertByBatch(listAlert);
        //插入同步数据
        return alertsHisVoList;
    }

    
    //根据主键修改备注
   	@Override
   	public void updateNote(String alertId, String note) {
   		 
   		AlertsHis alertsHis=alertHisDao.selectByPrimaryKey(alertId); 
   		 
   		alertsHis.setRemark(note);
   		
   		int index= alertHisDao.updateByPrimaryKey(alertsHis);
   			
   	}

    public List<AlertsHisVo> select(AlertsHis alertHisQuery) {
        List<AlertsHis> alertsHisList = alertHisDao.select(alertHisQuery);
        return TransformUtils.transform(AlertsHisVo.class, alertsHisList);
    }

    /**
     * @param alertsHisVo 告警修改对象
     * @return
     */
    public int updateByPrimaryKey(AlertsHisVo alertsHisVo) {
        if (null == alertsHisVo) {
            LOGGER.error("method[updateByPrimaryKey] param[alertsHisDTO] is null");
            throw new RuntimeException("param[alertsHisDTO] is null");
        }
        if (StringUtils.isEmpty(alertsHisVo.getAlertId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[alertId] is null");
            throw new RuntimeException("param[alertId] is null");
        }
        AlertsHis alertsHis = TransformUtils.transform(AlertsHis.class, alertsHisVo);
        int result = alertHisDao.updateByPrimaryKey(alertsHis);
        return result;
    }

	@Override
	public PageResponse<Map<String, Object>> alertRelateData(int alertType,String alertId,Integer pageSize,Integer pageNo) {
		PageResponse<Map<String, Object>> pageResponse=new PageResponse<Map<String, Object>>(); 
		int count = 0;
		List<Map<String, Object>> listAlertsHis = Lists.newArrayList();		
		if(alertType ==1) {
			count = alertHisDao.getRelateDataCount(alertId);
			if(count>0) {
				Integer begin = null;
				if(null!=pageNo && null!=pageSize) {
					if(pageNo==0) {
						pageNo = 1;
					}
					 begin = (pageNo - 1) * pageSize;
				}
			    
				 listAlertsHis = alertHisDao.getRelateData(alertId,pageSize,begin);
				
			}
		}else if(alertType==2) {
			count = alertHisDao.getRelateHisDataCount(alertId);
			if(count>0) {
				Integer begin = null;
				if(null!=pageNo && null!=pageSize) {
					if(pageNo==0) {
						pageNo = 1;
					}
					 begin = (pageNo - 1) * pageSize;
				}
			    
				 listAlertsHis = alertHisDao.getRelateHisData(alertId,pageSize,begin);
			}
		}
		
		pageResponse.setCount(count);
		pageResponse.setResult(listAlertsHis);
		return pageResponse;
	}
    
}
