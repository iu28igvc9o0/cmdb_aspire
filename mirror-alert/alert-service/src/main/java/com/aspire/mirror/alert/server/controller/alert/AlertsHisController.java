package com.aspire.mirror.alert.server.controller.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.alert.api.dto.alert.AlertGenResp;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisPageRequset;
import com.aspire.mirror.alert.api.service.alert.AlertsHisService;
import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBiz;
import com.aspire.mirror.alert.server.biz.primarySecondary.IAlertPrimarySecondaryBiz;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 告警控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.controller
 * 类名称:    AlertsHisController.java
 * 类描述:    告警控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 17:51
 * 版本:      v1.0
 */
@RestController
public class AlertsHisController implements AlertsHisService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AlertsHisController.class);

    @Autowired
    private AlertsHisBiz alertsHisBiz;
   
    
    @Autowired
    private AlertsBiz alertsBiz;
    @Autowired
    private AlertCabinetColumnBiz alertCabinetColumnBiz;
    @Autowired
    private IAlertPrimarySecondaryBiz alertPrimarySecondaryBiz;

    /**
     * 告警上报分页
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
	public PageResponse<AlertGenResp> alertGenerateList( @RequestParam("alert_id") String alertId,
											             @RequestParam("page_no") String pageNo,
											             @RequestParam("page_size") String pageSize) {
    	
    	 LOGGER.info("alertId is {} ",alertId);
    	 LOGGER.info("alertId is {} ",pageNo);
    	 LOGGER.info("alertId is {} ",pageSize);

        PageResponse<AlertsDetail> alertsDetailPageResponse = alertsHisBiz.alertGenerateListByPage(alertId, pageNo, pageSize);

                List<AlertGenResp> alertGenRespList=new ArrayList<AlertGenResp>();
        for ( AlertsDetail alertsDetail : alertsDetailPageResponse.getResult() ){

            AlertGenResp alertGenResp=new AlertGenResp();

            BeanUtils.copyProperties(alertsDetail, alertGenResp);

            alertGenRespList.add(alertGenResp);
        }
        PageResponse<AlertGenResp> pageResponse = new PageResponse<>();
        pageResponse.setResult(alertGenRespList);
        pageResponse.setCount(alertsDetailPageResponse.getCount());
		return pageResponse;
	} 
    
    
    
  //告警上报记录excel 下载
    @Override
	public List<AlertGenResp> alertGenerateDownload( @RequestParam("alert_id") String alertId) {
		 
    	LOGGER.info("alertId is {} ",alertId);
    	
    	List<AlertsHisVo>  alertsHisList=alertsHisBiz.selectAlertGenerateList(alertId);
    	
    	List<AlertGenResp> alertGenRespList=new ArrayList<AlertGenResp>();
        for (AlertsHisVo alertsHisVo : alertsHisList) {
        	AlertGenResp alertGenResp =new AlertGenResp();
        	BeanUtils.copyProperties(alertsHisVo, alertGenResp);
        	 
        	alertGenRespList.add(alertGenResp);
		}
    	
		return alertGenRespList;
	}
    
    
    
    
  //修改告警备注
    @Override
	public ResponseEntity<String> updateNote(@PathVariable("alert_id") String alertId, @RequestParam("note") String note) {
		 
    	if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("updateNote param alertId is null");
            return null;
        }
    	if (StringUtils.isEmpty(note)) {
            LOGGER.warn("updateNote param note is null");
            return null;
        }
    	
    	alertsHisBiz.updateNote( alertId, note );
    	
    	return new ResponseEntity<String>("success", HttpStatus.OK);
	}

    @Override
    public PageResponse<AlertsHisDetailResponse> getAlertHisList(@RequestBody AlertsHisPageRequset pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("getAlertHisList param pageRequset is null");
            return null;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(pageRequset, page);
        Map<String, Object> map = FieldUtil.getFiledMap(pageRequset);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertsHisVo> pageResult = alertsHisBiz.getAlertHisList(page);
        List<AlertsHisDetailResponse> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (AlertsHisVo alertsDTO : pageResult.getResult()) {
                AlertsHisDetailResponse alertsDetailResponse = new AlertsHisDetailResponse();
                BeanUtils.copyProperties(alertsDTO, alertsDetailResponse);
                listAlert.add(alertsDetailResponse);
            }
        }
        PageResponse<AlertsHisDetailResponse> result = new PageResponse<AlertsHisDetailResponse>();
        result.setCount(pageResult.getCount());
        result.setResult(listAlert);
        return result;
    }
    
    //告警子收敛相关查询
    @Override
    public  PageResponse<Map<String,Object>> alertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source
    		,@RequestParam(value="pageSize" ,required =false )   Integer pageSize
    		,@RequestParam(value="pageNo" ,required =false ) Integer pageNo) {
    	LOGGER.info("alertId is {},alertType :{},source:{} ",alertId,alertType,source);
    	
		/*
		 * if(null==alertsHisDTO) { LOGGER.error("告警不存在"); return null; }
		 */
    	 PageResponse<Map<String,Object>> result = new PageResponse<Map<String,Object>>();
    	 List<Map<String,Object>> list = Lists.newArrayList();
    	 if(source==null) {
    		 source = "";
    	 }
    		 if(source.equals("衍生告警")) {
    			 result  = alertsHisBiz.alertRelateData(alertType,alertId,pageSize,pageNo);
    		 }else if(source.equals("下电告警")) {
    			 PageRequest page = new PageRequest();
    			 if(null==pageNo && null==pageSize) {
    				 page.getDynamicQueryFields().put("pageFlag", false);
    			 }else {
    				 page.getDynamicQueryFields().put("pageFlag", true);
    			 }
    			 if(null==pageNo) {
    				 pageNo = 1;
    			 }
    			 page.getDynamicQueryFields().put("alertId", alertId);
    			 page.getDynamicQueryFields().put("alertType", alertType);
    			 page.setPageNo(pageNo);
    			 page.setPageSize(pageSize);
    			 result = alertCabinetColumnBiz.queryRelateBizsystemList(page);
    		 }else {
    		 result.setCount(1);
    		 String bizSys = "";
    		 String rAlertid = "";
    		 if(alertType==1) {//活动告警
    			 AlertsVo alertsVo = alertsBiz.selectAlertByPrimaryKey(alertId);
    			 bizSys = alertsVo.getBizSys();
    			 rAlertid = alertsVo.getRAlertId();
    		 }else {
    			 AlertsHisVo alertsHisVo =  alertsHisBiz.selectByPrimaryKey(alertId);
    			 bizSys = alertsHisVo.getBizSys();
    			 rAlertid = alertsHisVo.getRAlertId();
    		 }
    		if(org.apache.commons.lang.StringUtils.isNotEmpty(rAlertid) 
    				&& rAlertid.indexOf(Constants.PREFIX_PRIMARY)>=0) {
    			String primaryId = rAlertid.substring(Constants.PREFIX_PRIMARY.length(),rAlertid.length());
    			boolean pageFlag = false;
    			if(null!=pageNo && null!=pageSize) {
    				pageFlag = true;
    			}
    			result = alertPrimarySecondaryBiz.listByConfigId(alertType, primaryId, pageFlag, pageNo, pageSize);
    			
    		}else {
    			Map<String,Object> m = Maps.newHashMap();
	       		 m.put("biz_sys",bizSys);
	       		 m.put("alertCount", 1);
	       		 m.put("deviceCount",1);
	       		 list.add(m);
	       		 result.setResult(list);
    		}
    		 
    	 }
		 
        return result;
    }
    
}
