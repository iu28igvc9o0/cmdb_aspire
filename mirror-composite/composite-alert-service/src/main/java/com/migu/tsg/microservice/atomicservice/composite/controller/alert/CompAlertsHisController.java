package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertGenResp;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisPageRequset;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.composite.payload.alert.CompAlertGenResp;
import com.aspire.mirror.composite.payload.alert.CompAlertsHisDetailResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsHisPageRequset;
import com.aspire.mirror.composite.service.alert.ICompAlertsHisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsHisServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.constant.CmdbQueryNameConstants;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 历史告警
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.alert
 * 类名称:    CompAlertsHisController.java
 * 类描述:    历史告警控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 20:10
 * 版本:      v1.0
 */
@RestController
public class CompAlertsHisController extends CommonResourceV2Controller implements ICompAlertsHisService {
    private Logger logger = LoggerFactory.getLogger(CompAlertsHisController.class);

    @Autowired
    private AlertsHisServiceClient alertsHisService;

    @Autowired
    private CmdbV2Helper cmdbHelper;
    
    /**
     * 历史告警上报记录分页
     *
     * @param alertId 告警Id
     * @return  
     */
    @Override
	public PageResponse<CompAlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                            @RequestParam("page_no") String pageNo,
                                                            @RequestParam("page_size") String pageSize) {
    	
    	
    	PageResponse<AlertGenResp> pageResponse = alertsHisService.alertGenerateList(alertId, pageNo, pageSize);
       
    	PageResponse<CompAlertGenResp> response = new PageResponse<CompAlertGenResp>();
        response.setCount(pageResponse.getCount()); 
        
        List<CompAlertGenResp> compAlertGenRespList = new ArrayList<CompAlertGenResp>();
        
        for (AlertGenResp alertGenResp : pageResponse.getResult()) {
        	
        	CompAlertGenResp compAlertGenResp=new CompAlertGenResp(); 
        	
        	BeanUtils.copyProperties(alertGenResp, compAlertGenResp);
        	
        	compAlertGenRespList.add(compAlertGenResp);
        	
        }
        
        response.setResult(compAlertGenRespList);
        
		return response;
	}
    
    
   
    
    //上报记录下载
    @Override
	public void alertGenerateDownload(String alertId) {
    	
    	logger.info("method[alertDetailDownload] alertId is {}", alertId);	
    	 
    	
    	ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
    	
    	
    	List<AlertGenResp>   alertGenList = alertsHisService.alertGenerateDownload(alertId );
     
    	String[] headerList = {"上报时间","监控内容","监控值"};
        String[] keyList = {"alertStartTime","moniIndex","curMoniValue"};
        String title = "告警上报记录";
        String fileName = title+".xlsx";
        
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (AlertGenResp alertGenResp : alertGenList) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(alertGenResp);
           	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           	 map.put("alertStartTime",  sdf.format(alertGenResp.getCurMoniTime()));
                dataLists.add(map);
             // dataLists.add(ExportExcelUtil.objectToMap(alertGenResp));
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
       	 logger.error("导出Excel数据失败!", e);
        } 
		
	}
    
   
    
    //修改备注
   	@Override
   	public ResponseEntity<String> updateNote(@PathVariable("alert_id") String alertId, @RequestParam("note") String note ) {
   		
   		logger.info("method[updateNote] alertId is {}", alertId);
       	logger.info("method[updateNote] note is {}", note);
   		
       	alertsHisService.updateNote(  alertId, note );
   		
   	    return new ResponseEntity<String>("success", HttpStatus.OK);
   	}


    @Override
    public PageResponse<CompAlertsHisDetailResponse> getAlertHisList(@RequestBody CompAlertsHisPageRequset pageRequset) {
        logger.info("method[getAlertHisList] request body is {}", pageRequset);
        AlertsHisPageRequset alertsPageRequset = jacksonBaseParse(AlertsHisPageRequset.class, pageRequset);
        PageResponse<AlertsHisDetailResponse> pageResponse = alertsHisService.getAlertHisList(alertsPageRequset);
        PageResponse<CompAlertsHisDetailResponse> response = new PageResponse<CompAlertsHisDetailResponse>();
        response.setCount(pageResponse.getCount());
        List<CompAlertsHisDetailResponse> alertList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (AlertsHisDetailResponse detailResponse : pageResponse.getResult()) {
                CompAlertsHisDetailResponse competailResponse = jacksonBaseParse(CompAlertsHisDetailResponse
                        .class, detailResponse);
                competailResponse.setAlertTime(DateUtil.getDatePoor(competailResponse.getAlertEndTime(),
                        competailResponse.getCurMoniTime()));
//                competailResponse.setBizSysName(cmdbHelper.getBizSysName(competailResponse.getBizSys()));
//                competailResponse.setSourceRoomName(cmdbHelper.getRoomName(competailResponse.getSourceRoom()));
                competailResponse.setIdcTypeName(cmdbHelper.getCodeName("idcType", competailResponse.getIdcType()));
                alertList.add(competailResponse);
            }
        }
        response.setResult(alertList);
        return response;
    }
    
    @Override
    public PageResponse<Map<String,Object>> alertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source
    		,@RequestParam(value="pageSize" ,required =false )   Integer pageSize
    		,@RequestParam(value="pageNo" ,required =false ) Integer pageNo){
    	logger.info("method[alertRelateData] alertId is {},alertType :{},source:{} ",alertId,alertType,source);
    	PageResponse<Map<String,Object>> pageList = alertsHisService.alertRelateData(alertId,alertType,source
    			,pageSize,pageNo);
    	
    	List<Map<String,Object>> list = pageList.getResult();
    	
    	if(pageList.getCount()==0) {
    		return pageList;
    	}
    	List<String> bizList = Lists.newArrayList();
    	Map<String, Map<String,Object>> bizMap = Maps.newHashMap();
   		for(Map<String,Object> map:list) {
   			String biz = MapUtils.getString(map, "biz_sys");
   			if(!StringUtils.isEmpty(biz)) {
   				bizList.add(biz);
   				bizMap.put(biz, map);
   			}
   			
   		}
   		if(!bizList.isEmpty()) {
   			Map<String, Object> params = Maps.newHashMap();
   			params.put("bizSystem", bizList);
   			Object value = cmdbHelper.getCmdbData(params, CmdbQueryNameConstants.QUERY_BIZSYSTEM_INFO_BY_NAME, "list");
   			if(value!=null) {
   				List<Map<String,Object>> cmdblist = (List<Map<String,Object>>)value;
   				for(Map<String,Object> m:cmdblist) {
   					String biz = MapUtils.getString(m, "bizSystem");
   					if(bizMap.containsKey(biz)) {
   						String concat = MapUtils.getString(m, "business_concat");
   	   					String phone = MapUtils.getString(m, "business_concat_phone");
   	   				    Map<String,Object> valueMap = bizMap.get(biz);
   	   				    valueMap.put("concat", concat);
   	   				    valueMap.put("phone", phone);
   					}
   				}
   			}
   		}
   		bizMap.clear();
   	    return pageList;
    }
    
    @Override
    public void exportAlertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source){
    	logger.info("method[alertRelateData] alertId is {}", alertId);
   		
    	PageResponse<Map<String,Object>> pageList  = alertRelateData(alertId,alertType,source,null,null);
    	
    	List<Map<String,Object>> list = pageList.getResult();
    	String[] headerList = {"影响业务系统","业务联系人姓名","业务联系人手机号码","影响设备数","影响上报告警数量"};
        String[] keyList = {"biz_sys","moniIndex","curMoniValue","deviceCount","alertCount"};
        String title = "影响范围";
        String fileName = title+".xlsx";
    	ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
           
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, list, keyList);
            book.write(os);
        } catch (Exception e) {
       	 logger.error("导出Excel数据失败!", e);
        } 
    }
}
