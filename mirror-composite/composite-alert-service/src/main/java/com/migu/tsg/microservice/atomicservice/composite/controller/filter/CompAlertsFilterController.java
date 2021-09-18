package com.migu.tsg.microservice.atomicservice.composite.controller.filter;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.aspire.mirror.alert.api.dto.AlertsExportDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.filter.*;
import com.aspire.mirror.composite.payload.alert.CompAlertsDetailResp;
import com.aspire.mirror.composite.payload.filter.*;
import com.aspire.mirror.composite.service.filter.ICompAlertsFilterService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.filter.AlertsFilterServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

/**
 * 历史告警
 * <p>
 * 项目名称: mirror平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.controller.alert 类名称:
 * CompAlertsHisController.java 类描述: 历史告警控制层 创建人: JinSu 创建时间: 2018/9/19 20:10
 * 版本: v1.0
 */
@RestController
public class CompAlertsFilterController extends CommonResourceV2Controller implements ICompAlertsFilterService {
	private Logger logger = LoggerFactory.getLogger(CompAlertsFilterController.class);
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//	private static final SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String CMDB_HELPER_KEY_IDCTYPE = "idcType";
	
	@Autowired
	private CmdbV2Helper cmdbHelper;

	@Autowired
	private AlertsFilterServiceClient alertsFilterService;


	@Override
    @ResAction(action = "view", resType = "alert_filter")
	public PageResponse<ComAlertFilter> pageList(@RequestBody ComAlertFilterRequest pageRequset) {
		logger.info("method[pageList] request body is {}", pageRequset);
		// 调原子层
		AlertFilterResponse alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertFilterResponse.class, pageRequset);
		PageResponse<AlertFilterDTO> pageResponse = alertsFilterService.pageList(alertsPageRequset);
		PageResponse<ComAlertFilter> response = new PageResponse<ComAlertFilter>();
		response.setCount(pageResponse.getCount());
		List<ComAlertFilter> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
			for (AlertFilterDTO detailResponse : pageResponse.getResult()) {
				ComAlertFilter competailResponse = PayloadParseUtil.jacksonBaseParse(ComAlertFilter.class, detailResponse);
				alertList.add(competailResponse);
			}
		}
		response.setResult(alertList);
		return response;
	}


	@Override
	@ResAction(action = "view", resType = "alert_filter")
	public ComAlertFilter findByPrimaryKey(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			logger.warn("findByPrimaryKey param filterId is null");
			return null;
		}
		AlertFilterDTO filter = alertsFilterService.findByPrimaryKey(filterId);
		if (null == filter) {
			return null;
		}
		return PayloadParseUtil.jacksonBaseParse(ComAlertFilter.class, filter);
	}

	@Override
	@ResAction(action = "create", resType = "alert_filter")
	public ResponseEntity<String> create(@RequestBody ComAlertFilter createRequest) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
       logger.info("[create]Username is {}.", authCtx.getUser().getUsername());
		if (createRequest == null) {
			logger.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		AlertFilterDTO obj = alertsFilterService.findByName(createRequest.getName());
		if (null != obj) {
			throw new RuntimeException("告警名称已经存在,请重新命名");
		}
		AlertFilterDTO competailResponse = PayloadParseUtil.jacksonBaseParse(AlertFilterDTO.class, createRequest);
		competailResponse.setCreater(authCtx.getUser().getUsername());
		return alertsFilterService.create(competailResponse);
	}

	@Override
	@ResAction(action = "update", resType = "alert_filter")
	public ResponseEntity<String> update(@PathVariable("filter_id") String filterId,
			@RequestBody ComAlertFilter updateRequest) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (StringUtils.isEmpty(filterId)) {
			logger.warn("update param filterId is null");
			return null;
		}
		if (updateRequest == null) {
			logger.warn("update updateRequest note is null");
			return null;
		}
		updateRequest.setEditer(authCtx.getUser().getUsername());
		AlertFilterDTO filter = PayloadParseUtil.jacksonBaseParse(AlertFilterDTO.class, updateRequest);
		return alertsFilterService.update(filterId, filter);
	}

	@Override
	@ResAction(action = "delete", resType = "alert_filter")
	public ResponseEntity<String> delete(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			logger.error("delete filterId is null");
			throw new RuntimeException("delete filterId is null");
		}
		return alertsFilterService.delete(filterId);
	}

	@Override
	@ResAction(action = "view", resType = "alert_filter")
	public ComAlertFilter findByName(@PathVariable("filter_name") String filterName) {
		if (StringUtils.isEmpty(filterName)) {
			logger.error("findByName filterName is null");
			throw new RuntimeException("findByName filterName is null");
		}
		AlertFilterDTO filter = alertsFilterService.findByName(filterName);
		if (null == filter) {
			return null;
		}
		ComAlertFilter comFilter = PayloadParseUtil.jacksonBaseParse(ComAlertFilter.class, filter);
		return comFilter;
	}


	@Override
	@ResAction(action = "query", resType = "alert_filter")
	public List<ComAlertFilter> getAllFilter(@RequestParam(value="filterFlag" ,required =false ) boolean filterFlag) {
		String userName = null;
		if (filterFlag) {
			RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
			if (!(authCtx.getUser().isAdmin() ||
					authCtx.getUser().getNamespace().equals(authCtx.getUser().getUsername()))) {
						userName = authCtx.getUser().getUsername();
					}else {
						filterFlag = false;
					}
		}
		List<ComAlertFilter> alertList = Lists.newArrayList();
		List<AlertFilterDTO> filters = alertsFilterService.getAllFilter(filterFlag,userName);
		if(null == filters) {
			return alertList;
		}
		alertList = PayloadParseUtil.jacksonBaseParse(ComAlertFilter.class, filters);
		return alertList;
	}


	@Override
	@ResAction(action = "query", resType = "alert_filter")
	public PageResponse<CompAlertsDetailResp> queryAlertData(@RequestBody ComAlertFilterDataRequest pageRequset) {
		logger.info("method[queryAlertData] request body is {}", pageRequset);
		
		AlertFilterDataRequest alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertFilterDataRequest.class, pageRequset);
		PageResponse<AlertsDetailResponse> pageResponse = alertsFilterService.queryAlertData(alertsPageRequset);
		PageResponse<CompAlertsDetailResp> response = new PageResponse<CompAlertsDetailResp>();
		response.setCount(pageResponse.getCount());
		List<CompAlertsDetailResp> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
			alertList = PayloadParseUtil.jacksonBaseParse(CompAlertsDetailResp.class, pageResponse.getResult());
		}
		response.setResult(alertList);
		return response;
	}


	@Override
	@ResAction(action = "query", resType = "alert_filter")
	public List<ComAlertFilterDataResponse> queryAlertCount(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			logger.error("queryAlertCount filterId is null");
			throw new RuntimeException("queryAlertCount filterId is null");
		}
		String userName = null;
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (!(authCtx.getUser().isAdmin() ||
		authCtx.getUser().getNamespace().equals(authCtx.getUser().getUsername()))) {
			userName = authCtx.getUser().getUsername();
		}
		List<AlertFilterDataResponse> response = alertsFilterService.queryAlertCount(filterId, userName);
		List<ComAlertFilterDataResponse> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(response)) {
			alertList = PayloadParseUtil.jacksonBaseParse(ComAlertFilterDataResponse.class, response);
		}
		return alertList;
	}


	@Override
	public ResponseEntity<String> copy(@RequestBody ComAlertFilter createRequest) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
	       logger.info("[copy]Username is {}.", authCtx.getUser().getUsername());
			if (createRequest == null) {
				logger.error("copy createRequest is null");
				throw new RuntimeException("copy param is null");
			}
			AlertFilterDTO obj = alertsFilterService.findByName(createRequest.getName());
			if (null != obj) {
				throw new RuntimeException("告警名称已经存在,请重新命名");
			}
			createRequest.setEditer("");
			AlertFilterDTO competailResponse = PayloadParseUtil.jacksonBaseParse(AlertFilterDTO.class, createRequest);
			competailResponse.setCreater(authCtx.getUser().getUsername());
			return alertsFilterService.copy(competailResponse);
	}


	@Override
	public List<ComAlertFilterScene> querySceneByFilterId(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			logger.error("querySceneByFilterId filterId is null");
			throw new RuntimeException("querySceneByFilterId filterId is null");
		}
		List<AlertFilterSceneDTO> filterScenes= alertsFilterService.querySceneByFilterId(filterId);
		List<ComAlertFilterScene> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(filterScenes)) {
			alertList = PayloadParseUtil.jacksonBaseParse(ComAlertFilterScene.class, filterScenes);
		}
		return alertList;
	}

	 /**
     * 导出警报列表数据
     *
     * @param pageRequset
     * @param response
     */
	@Override
    public void exportFilterAlerts(@RequestBody ComAlertFilterDataRequest pageRequset, HttpServletResponse response) throws Exception {
    	logger.info("method[exportFilterAlerts] request body is {}", pageRequset);
        String[] headerList = {"级别","设备IP","监控对象","监控值","告警内容","开始时间","当前时间","资源池","所属位置","机房位置","业务线","告警类型","状态","通知方式","通知状态","通知时间","转派人","转派状态","转派时间","待确认人","确认人","确认时间","确认内容","派单状态","派单时间","派单类型","告警次数"};
        String[] keyList = {"alertLevel","deviceIp","moniObject","curMoniValue","moniIndex","alertStartTime","curMoniTime","idcType","source","sourceRoom","idcType","objectType","orderStatus","reportType","reportStatus","reportTime","transUser","transStatus","transTime","toConfirmUser","confirmedUser","confirmedTime","confirmedContent","deliverStatus","deliverTime","orderType","alertCount"};
        String title = "告警导出列表_" + sDateFormat.format(new Date());
        String fileName = title+".xlsx";
        AlertFilterDataRequest alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertFilterDataRequest.class, pageRequset);
        // Integer 默认创建对象就是null
        // alertsPageRequset.setPageSize(null);
        alertsPageRequset.setPageFlag("true");
		PageResponse<AlertsDetailResponse> pageResponse = alertsFilterService.queryAlertData(alertsPageRequset);
		
		List<Map<String, Object>> dataLists = Lists.newArrayList();
		for (AlertsDetailResponse alertsDTO : pageResponse.getResult()) {
            SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            AlertsExportDTO exportDTO = new AlertsExportDTO();
            exportDTO.setAlertId(alertsDTO.getAlertId());
            exportDTO.setAlertLevel(transAlertLevel(alertsDTO.getAlertLevel()));
            exportDTO.setOrderStatus(transOrderStatus(alertsDTO.getOrderStatus()));
            exportDTO.setObjectType(transAlertObjectType(alertsDTO.getObjectType()));
            exportDTO.setDeviceIp(alertsDTO.getDeviceIp());
            exportDTO.setDeviceId(alertsDTO.getDeviceId());
            exportDTO.setMoniObject(alertsDTO.getMoniObject());
            exportDTO.setCurMoniValue(alertsDTO.getCurMoniValue());
            exportDTO.setMoniIndex(alertsDTO.getMoniIndex());
            exportDTO.setAlertStartTime(alertsDTO.getAlertStartTime() == null ? "" : f_sdf.format(alertsDTO.getAlertStartTime()));
            exportDTO.setCurMoniTime(alertsDTO.getCurMoniTime() == null ? "" : f_sdf.format(alertsDTO.getCurMoniTime()));
            exportDTO.setIdcType(alertsDTO.getIdcType());
            exportDTO.setSourceRoom(alertsDTO.getSourceRoom());
            exportDTO.setSource(alertsDTO.getSource());
            exportDTO.setReportType(transAlertReportType(alertsDTO.getReportType()));
            exportDTO.setReportStatus(transOperateStatus(alertsDTO.getReportStatus()));
            exportDTO.setReportTime(alertsDTO.getReportTime() == null ? "" : f_sdf.format(alertsDTO.getReportTime()));
            exportDTO.setTransUser(alertsDTO.getTransUser());
            exportDTO.setTransStatus(transOperateStatus(alertsDTO.getTransStatus()));
            exportDTO.setTransTime(alertsDTO.getTransTime() == null ? "" : f_sdf.format(alertsDTO.getTransTime()));
            exportDTO.setToConfirmUser(alertsDTO.getToConfirmUser());
            exportDTO.setConfirmedUser(alertsDTO.getConfirmedUser());
            exportDTO.setConfirmedTime(alertsDTO.getConfirmedTime() == null ? "" : f_sdf.format(alertsDTO.getConfirmedTime()));
            exportDTO.setConfirmedContent(alertsDTO.getConfirmedContent());
            exportDTO.setDeliverStatus(transOperateStatus(alertsDTO.getDeliverStatus()));
            exportDTO.setDeliverTime(alertsDTO.getDeliverTime() == null ? "" : f_sdf.format(alertsDTO.getDeliverTime()));
            exportDTO.setOrderType(transOrderType(alertsDTO.getOrderType()));
            exportDTO.setAlertCount(alertsDTO.getAlertCount() == null ? 0 : alertsDTO.getAlertCount());
            dataLists.add(objectToMap(exportDTO));
        }
		
		for (Map<String, Object> alert : dataLists) {
//            if (alert.get("bizSys") != null) {
//                String bizSys = cmdbHelper.getBizSysName(String.valueOf(alert.get("bizSys")));
//                alert.put("bizSys", StringUtils.isEmpty(bizSys) ? alert.get("bizSys") : bizSys);
//            }
//            if (alert.get("sourceRoom") != null) {
//                String sourceRoom = cmdbHelper.getRoomName(String.valueOf(alert.get("sourceRoom")));
//                alert.put("sourceRoom", StringUtils.isEmpty(sourceRoom) ? alert.get("sourceRoom") : sourceRoom);
//            }
            if (alert.get("idcType") != null) {
                String idcType = cmdbHelper.getCodeName(CMDB_HELPER_KEY_IDCTYPE, String.valueOf(alert.get("idcType")));
                alert.put("idcType", StringUtils.isEmpty(idcType) ? alert.get("idcType") : idcType);
            }
            if (alert.get("deviceType") != null) {
                String deviceType = cmdbHelper.getCodeName("device_class", String.valueOf(alert.get("deviceType")));
                alert.put("deviceType", StringUtils.isEmpty(deviceType) ? alert.get("deviceType") : deviceType);
            }
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
    }
    
    private Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
    
    private String transAlertLevel(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String alertLevel;
        switch (StringUtils.trimWhitespace(origin)) {
            case "1":
                alertLevel = "提示";
                break;
            case "2":
                alertLevel = "低";
                break;
            case "3":
                alertLevel = "中";
                break;
            case "4":
                alertLevel = "高";
                break;
            case "5":
                alertLevel = "重大";
                break;
            default:
                alertLevel = origin;
                break;
        }
        return alertLevel;
    }

    private String transOrderStatus(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String orderStatus;
        switch (org.apache.commons.lang.StringUtils.trim(origin)) {
            case "1":
                orderStatus = "未派单";
                break;
            case "2":
                orderStatus = "处理中";
                break;
            case "3":
                orderStatus = "已完成";
                break;
            default:
                orderStatus = origin;
                break;
        }
        return orderStatus;
    }

    private String transAlertObjectType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String objectType;
        switch (StringUtils.trimWhitespace(origin)) {
            case "1":
                objectType = "设备";
                break;
            case "2":
                objectType = "业务系统";
                break;
            default:
                objectType = origin;
                break;
        }
        return objectType;
    }

    private String transAlertReportType(Integer reportType) {
        if (reportType == null) {
            return "";
        }
        String type;
        switch (reportType) {
            case 0:
                type = "短信";
                break;
            case 1:
                type = "邮件";
                break;
            default:
                type = String.valueOf(reportType);
                break;
        }
        return type;
    }

    private String transOperateStatus(Integer reportStaus) {
        if (reportStaus == null) {
            return "";
        }
        String status;
        switch (reportStaus) {
            case 0:
                status = "失败";
                break;
            case 1:
                status = "成功";
                break;
            default:
                status = String.valueOf(reportStaus);
                break;
        }
        return status;
    }

    private String transOrderType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String type;
        switch (origin) {
            case "1":
                type = "告警工单";
                break;
            case "2":
                type = "故障工单";
                break;
            default:
                type = origin;
                break;
        }
        return type;
    }


}
