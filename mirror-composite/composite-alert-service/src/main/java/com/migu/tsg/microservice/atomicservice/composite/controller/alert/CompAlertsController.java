package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.*;
import com.aspire.mirror.alert.api.dto.notify.AlertNotifyResp;
import com.aspire.mirror.alert.api.dto.notify.NotifyPageResponse;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.composite.payload.alert.*;
import com.aspire.mirror.composite.service.alert.ICompAlertsService;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.helper.UserHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
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
import org.springframework.validation.annotation.Validated;
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
import java.util.*;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 告警控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.alert
 * 类名称:    CompAlertsController.java
 * 类描述:    告警控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@RestController
public class CompAlertsController extends CommonResourceV2Controller implements ICompAlertsService {
    private Logger logger = LoggerFactory.getLogger(CompAlertsController.class);

    @Autowired
    private AlertsServiceClient alertsService;

    @Autowired
    private CmdbV2Helper cmdbHelper;
    @Autowired
    private UserHelper userHelper;

    /**
     * 查询告警列表
     *
     * @param pageRequset 查询page对象
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "alert")
    public PageResponse<CompAlertsDetailResponse> pageList(@RequestBody @Validated CompAlertsPageRequset pageRequset) {
        logger.info("method[pageList] request body is {}", pageRequset);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[pageList]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = jacksonBaseParse(RbacResource.class, pageRequset);
        //查询鉴权
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        // 非管理人员, 应用通用权限
        if (!authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());

            if (!super.applyGeneralAuthConstraints(totalConstraints, pageRequset)) {
                return new PageResponse<CompAlertsDetailResponse>();
            }
        }

        //调原子层
        AlertsPageRequset alertsPageRequset = jacksonBaseParse(AlertsPageRequset.class, pageRequset);
        PageResponse<AlertsDetailResponse> pageResponse = alertsService.pageList(alertsPageRequset);
        PageResponse<CompAlertsDetailResponse> response = new PageResponse<CompAlertsDetailResponse>();
        response.setCount(pageResponse.getCount());
        List<CompAlertsDetailResponse> alertList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (AlertsDetailResponse alertsDetailResponse : pageResponse.getResult()) {
                CompAlertsDetailResponse compAlertsDetailResponse = jacksonBaseParse(CompAlertsDetailResponse.class,
                        alertsDetailResponse);
//                compAlertsDetailResponse.setBizSysName(cmdbHelper.getBizSysName(compAlertsDetailResponse.getBizSys()));
//                compAlertsDetailResponse.setSourceRoomName(cmdbHelper.getRoomName(compAlertsDetailResponse
//                        .getSourceRoom()));
                compAlertsDetailResponse.setIdcTypeName(cmdbHelper.getCodeName("idcType", compAlertsDetailResponse.getIdcType()));
                compAlertsDetailResponse.setAlertTime(DateUtil.getDatePoor(compAlertsDetailResponse.getAlertEndTime()
                        , compAlertsDetailResponse.getCurMoniTime()));
                alertList.add(compAlertsDetailResponse);
            }
        }
        response.setResult(alertList);
        return response;
    }


    /**
     * 告警详情
     *
     * @param alertId 告警Id
     * @return CompAlertsDetailResponse 告警详情
     */
    @Override
    @ResAction(action = "view", resType = "alert")
    public CompAlertsSecondDetailResp findAlertByPrimaryKey(@PathVariable("alert_id") String alertId) {
        logger.info("method[findByPrimaryKey] alert_id is {}", alertId);
        /*logger.info("[findByPrimaryKey]Username is {}.", authCtx.getUser().getUsername());
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        RbacResource rbacData = new RbacResource();
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[findByPrimaryKey]The rbacResource is {}.", rbacData);
        resAuthHelper.resourceActionVerify(
                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());*/
        //调原子层
        if (StringUtils.isEmpty(alertId)) {
            logger.warn("CompAlertsController method[findAlertByPrimaryKey] param alertId is empty");
            return null;
        }
        
        
        AlertSecondDetailResp alertsSecDetailResponse = alertsService.findAlertByPrimaryKey(alertId);
        CompAlertsSecondDetailResp compAlertSecondDetailResponse = jacksonBaseParse(CompAlertsSecondDetailResp.class,
        		alertsSecDetailResponse);
            
//        compAlertSecondDetailResponse.setBizSysName(cmdbHelper.getBizSysName(compAlertSecondDetailResponse.getBizSys()));
//        compAlertSecondDetailResponse.setSourceRoomName(cmdbHelper.getRoomName(compAlertSecondDetailResponse.getSourceRoom()));
        compAlertSecondDetailResponse.setIdcTypeName(cmdbHelper.getCodeName("idcType", compAlertSecondDetailResponse.getIdcType()));
        compAlertSecondDetailResponse.setAlertTime(DateUtil.getDatePoor(compAlertSecondDetailResponse.getAlertEndTime(),
        		compAlertSecondDetailResponse.getCurMoniTime()));
        return compAlertSecondDetailResponse;
    }
    
    /**
     * 告警上报记录
     *
     * @param alertId 告警Id
     * @return  
     */
    @Override
	public PageResponse<CompAlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                            @RequestParam("page_no") String pageNo,
                                                            @RequestParam("page_size") String pageSize) {
		 
    	PageResponse<AlertGenResp> pageResponse = alertsService.alertGenerateList(alertId, pageNo, pageSize);
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

    /**
     * 告警操作记录
     *
     * @param alertId 告警Id
     * @return  
     */
	@Override
	public PageResponse<CompAlertRecordResp> alertRecordList(@RequestParam("alert_id") String alertId,
                                                             @RequestParam("page_no") String pageNo,
                                                             @RequestParam("page_size") String pageSize) {
		 
		PageResponse<AlertRecordResp> pageResponse = alertsService.alertRecordList(alertId, pageNo, pageSize);
        PageResponse<CompAlertRecordResp> response = new PageResponse<CompAlertRecordResp>();
        response.setCount(pageResponse.getCount()); 
        
        List<CompAlertRecordResp> compAlertRecordRespList = new ArrayList<CompAlertRecordResp>();
        
        for (AlertRecordResp alertRecordResp : pageResponse.getResult()) {
        	
        	CompAlertRecordResp compAlertRecordResp=new CompAlertRecordResp(); 
        	
        	BeanUtils.copyProperties(alertRecordResp, compAlertRecordResp);
        	
        	compAlertRecordRespList.add(compAlertRecordResp);
        	
        }
        
        
        response.setResult(compAlertRecordRespList);
        
        
		return response;
	}


	/**
     * 告警通知记录
     *
     * @param alertId 告警Id
     * @return  
     */
	@Override
	public CompNotifyPageResponse<CompAlertNotifyResp> alertNotifyList(@RequestParam("alert_id") String alertId,
															            @RequestParam("page_no") String pageNo,
															            @RequestParam("page_size") String pageSize,
															            @RequestParam("report_type") String reportType) {
		 
		NotifyPageResponse<AlertNotifyResp> pageResponse = alertsService.alertNotifyList(alertId, pageNo, pageSize,reportType);
        
		CompNotifyPageResponse<CompAlertNotifyResp> response = new CompNotifyPageResponse<CompAlertNotifyResp>();
        
		response.setCount(pageResponse.getCount());
        response.setSuccessCount(pageResponse.getSuccessCount());
        response.setFallCount(pageResponse.getFallCount());
        List<CompAlertNotifyResp> compAlertNotifyRespList = new ArrayList<CompAlertNotifyResp>();
        
        for (AlertNotifyResp alertNotifyResp : pageResponse.getResult()) {
        	CompAlertNotifyResp compAlertNotifyResp=new CompAlertNotifyResp();
        	BeanUtils.copyProperties(alertNotifyResp, compAlertNotifyResp);
            PageResult<CompUserVo> page = userHelper.pageList(1, 1, alertNotifyResp.getDestination());
            compAlertNotifyResp.setName(page.getResult().get(0).getName());
            compAlertNotifyRespList.add(compAlertNotifyResp);
        	
        }
        
        response.setResult(compAlertNotifyRespList);
        
        
		return response;
	}

    
    
    //上报记录下载
    @Override
	public void alertGenerateDownload(String alertId) {
    	
    	logger.info("method[alertDetailDownload] alertId is {}", alertId);	
    	 
    	
    	ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
    	
    	
    	List<AlertGenResp>   alertGenList = alertsService.alertGenerateDownload(alertId );
     
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


  //操作记录下载
	public void alertRecordDownload(String alertId) {
		
		logger.info("method[alertDetailDownload] alertId is {}", alertId);
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
    	
        
    	List<AlertRecordResp>   alertRecordList = alertsService.alertRecordDownload(alertId );
		
    	
		String[] headerList = {"操作类型","操作人","操作时间","操作状态","操作内容"};
        String[] keyList = {"operationType","userName","operationTime","operationStatus","content"};
        String title = "告警操作记录";
        String fileName = title+".xlsx";
        
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (AlertRecordResp  alertRecordResp : alertRecordList) {
           	 Map<String, Object>  map=ExportExcelUtil.objectToMap(alertRecordResp);
           	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           	 map.put("operationTime",  sdf.format(alertRecordResp.getOperationTime()));
           	 if (alertRecordResp.getOperationStatus().equals("1")) {
           		 map.put("operationStatus", "成功");
				 }else {
					 map.put("operationStatus", "失败");
				 }
           	 if (alertRecordResp.getOperationType().equals("0")) {
           		 map.put("operationType", "转派");
				 }else  if (alertRecordResp.getOperationType().equals("1"))  {
					 map.put("operationType", "确认");
				 }else  if (alertRecordResp.getOperationType().equals("2"))  {
					 map.put("operationType", "派发工单");
				 }else  if (alertRecordResp.getOperationType().equals("3"))  {
					 map.put("operationType", "清除");
				 }else  if (alertRecordResp.getOperationType().equals("4"))  {
					 map.put("operationType", "通知");
				 }
           	  
                dataLists.add(map);
               
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

	
	//通知记录下载
	@Override
	public void alertNotifyDownload(String alertId) {
		
		logger.info("method[alertDetailDownload] alertId is {}", alertId);
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
    	
        
    	List<AlertNotifyResp>   alertNotifyList = alertsService.alertNotifyDownload(alertId);
    	
    	String[] headerList = { "通知方式","通知对象","发送时间","通知状态"};
        String[] keyList = {"reportType","destination","createTime","status"};
        String title = "告警通知记录";
        String fileName = title+".xlsx";
        
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (AlertNotifyResp alertNotifyResp : alertNotifyList) {
           	 Map<String, Object>  map=ExportExcelUtil.objectToMap(alertNotifyResp);
           	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           	 map.put("createTime",  sdf.format(alertNotifyResp.getCreateTime()));
           	 if (alertNotifyResp.getStatus().equals("1")) {
           		 map.put("status", "成功");
				 }else {
					 map.put("status", "失败");
				 }
           	 if (alertNotifyResp.getReportType().equals("1")) {  
           		 map.put("reportType", "邮件");
				 }else {
					 map.put("reportType", "短信");
				 }
                dataLists.add(map);
                //dataLists.add(ExportExcelUtil.objectToMap(alertNotifyResp));
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
		
		alertsService.updateNote(  alertId, note );
		
	    return new ResponseEntity<String>("success", HttpStatus.OK);
	}
     
    /**
     * 修改告警
     *
     * @param orderId 告警ID
     * @return BaseResponse 修改返回
     */
    @Override
    @ResAction(action = "update", resType = "alert")
    public ResponseEntity<String> bpmCallBack(@PathVariable("order_id") String orderId) {
        logger.info("method[modifyByPrimaryKey] order_id is {}", orderId);
       /* RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[modifyByPrimaryKey]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = new RbacResource();
        //鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[modifyByPrimaryKey]The rbacResource is {}.", rbacData);
        resAuthHelper.resourceActionVerify(
                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());*/
        alertsService.modOrderStatusByOrderId(orderId, Constant.ORDER_END);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警相关设备信息
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
    @ResAction(action = "alertDeviceInformation", resType = "alert")
    public ResponseEntity<AlertDeviceInformationResponse> alertDeviceInformation(String alertId) {
        AlertDeviceInformation alertInfor = alertsService.alertDeviceInformation(alertId);
        AlertDeviceInformationResponse information = new AlertDeviceInformationResponse();
        BeanUtils.copyProperties(alertInfor,information);
        return new ResponseEntity<>(information,HttpStatus.OK);
    }

    /**
     * 租户告警短信通知
     * @param request
     * @return
     */
    @Override
    @ResAction(action = "smsTenantNotify", resType = "alert")
    public ResponseEntity<Map> smsTenantNotify(@RequestBody Map<String, Object> request) {
        Boolean flag=alertsService.smsTenantNotify(request);
        Map<String ,Object> map = new HashMap<>();
        map.put("status", flag);
        if (flag) {
            map.put("message","sms sent successfully");
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        map.put("message","sms sent failed");
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

}
