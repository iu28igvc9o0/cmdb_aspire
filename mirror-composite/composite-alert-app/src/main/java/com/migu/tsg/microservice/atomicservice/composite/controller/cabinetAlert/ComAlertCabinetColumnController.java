package com.migu.tsg.microservice.atomicservice.composite.controller.cabinetAlert;

import java.beans.IntrospectionException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDataDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnReq;
import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnConfig;
import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnConfigData;
import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnReq;
import com.aspire.mirror.composite.service.cabinetAlert.IComAlertCabinetColumnService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cabinetAlert.AlertCabinetColumnClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JavaBeanUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.server.controller 类名称:
 * AlertsHisController.java 类描述: 告警控制层 创建人: JinSu 创建时间: 2018/9/14 17:51 版本: v1.0
 */
@RestController
public class ComAlertCabinetColumnController implements IComAlertCabinetColumnService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComAlertCabinetColumnController.class);
	@Autowired
	private AlertCabinetColumnClient alertCabinetColumnClient;

	
	//初始化列头柜告警配置
	@Override
	public ResponseEntity<String> manageConfig(@RequestBody List<ComAlertCabinetColumnConfig> configList) {
		if (configList == null) {
			LOGGER.error("manageConfig configList is null");
			throw new RuntimeException("manageConfig configList is null");
		}
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		for(ComAlertCabinetColumnConfig c:configList) {
			c.setEditor(authCtx.getUser().getUsername());
			
		}
		
		List<AlertCabinetColumnConfigDTO> list = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnConfigDTO.class, configList);
		
		return alertCabinetColumnClient.manageConfig(list);
	}
	
	
	//查询列头柜告警配置
	@Override
	public ComAlertCabinetColumnConfig getConfig(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("getConfig req is null");
			throw new RuntimeException("getConfig req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		AlertCabinetColumnConfigDTO config = alertCabinetColumnClient.getConfig(reqNew);
		
		ComAlertCabinetColumnConfig dto =PayloadParseUtil.jacksonBaseParse(ComAlertCabinetColumnConfig.class, config);
		return dto;
	}
	 /**
	  * 更新列头柜配置状态
	  */
	@Override
	public ResponseEntity<String> updateStatus(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("updateStatus req is null");
			throw new RuntimeException("updateStatus req is null");
		}
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		reqNew.setEditor(authCtx.getUser().getUsername());
		return alertCabinetColumnClient.updateStatus(reqNew);
	
	}
	/**
	 * 查询列头柜告警
	 */
	@Override
	public PageResponse<ComAlertCabinetColumnConfigData> queryCabinetColumnAlertPageList(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		
		PageResponse<AlertCabinetColumnConfigDataDTO> pageResult =  alertCabinetColumnClient.queryCabinetColumnAlertPageList(reqNew);

		PageResponse<ComAlertCabinetColumnConfigData> result = new PageResponse<ComAlertCabinetColumnConfigData>();
		result.setCount(pageResult.getCount());
		result.setResult(PayloadParseUtil.jacksonBaseParse(ComAlertCabinetColumnConfigData.class, pageResult.getResult()));
		return result;
	}
	
	
	@Override
	public void exportCabinetColumnAlert(@RequestBody ComAlertCabinetColumnReq req) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		List<AlertCabinetColumnConfigDataDTO> pageResult =  alertCabinetColumnClient.queryCabinetColumnAlert(reqNew);
		List<Map<String, Object>> dataLists = Lists.newArrayList();
		for (AlertCabinetColumnConfigDataDTO entity : pageResult) {
			int c = entity.getCabinetCount();
			int cAlert = entity.getCabinetAlertCount();
			int d = entity.getDeviceCount();
			int dAlert = entity.getDeviceAlertCount();
			int status = entity.getStatus();
			Integer alertStatus = entity.getAlertStatus()==null?0:entity.getAlertStatus();
			String cStr = c+"";
			if(status==1 && dAlert>0) {
				cStr = c+"("+cAlert+")";
			}
			String dStr = d+"";
			if(status==1) {
				dStr = d+"("+dAlert+")";
			}
			if(status==0 || alertStatus==1) {
				entity.setBizSystemCount(entity.getBizSysCount());
			}
            Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
            map.put("cStr", cStr);
            map.put("dStr", dStr);
            dataLists.add(map);
        }
		String[] headerList = {"资源池","机房","列头柜名称","管理机柜数","管理设备数","影响业务数"};
        String[] keyList = {"idcType","roomId","cabinetColumnName","cStr","dStr","bizSystemCount"};
        String title = "列头柜下电故障";
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
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
        	LOGGER.error("导出Excel数据失败!", e);
        } 
	}
	
	
	/**
	 * 查询机柜告警
	 */
	@Override
	public PageResponse<Map<String,Object>> queryCabinetAlertPageList(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnClient.queryCabinetAlertPageList(reqNew);
		
		return pageResult;
	}
	
	@Override
	public PageResponse<Map<String,Object>> queryDeviceAlertPageList(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryDeviceAlertPageList req is null");
			throw new RuntimeException("queryDeviceAlertPageList req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnClient.queryDeviceAlertPageList(reqNew);
		
		return pageResult;
	}
	
	/**
	 * 查询机柜告警不分页
	 */
	@Override
	public List<Map<String,Object>> queryCabinetAlert(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		
		
		
		List<Map<String,Object>> pageResult =  alertCabinetColumnClient.queryCabinetAlert(reqNew);
	
		return pageResult;
	}


	@Override
	public String getScheduleConfig(String indexType) {
		if (org.apache.commons.lang.StringUtils.isBlank(indexType)) {
			LOGGER.error("getScheduleConfig indexType is null");
			throw new RuntimeException("getScheduleConfig indexType is null");
		}
		String info =  alertCabinetColumnClient.getScheduleConfig(indexType);
		return info;
	}
	
	
	@Override
	public PageResponse<Map<String,Object>> queryBizSystemPageList(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error(" req is null");
			throw new RuntimeException("queryqueryBizSystemPageListBizSystemPageList req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		
		
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnClient.queryBizSystemPageList(reqNew);
		
		return pageResult;
	}
	
	
	
	@Override
	public void exportBizSystemList(@RequestBody ComAlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryBizSystemPageList req is null");
			throw new RuntimeException("queryBizSystemPageList req is null");
		}
		AlertCabinetColumnReq reqNew = PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnReq.class, req);
		List<Map<String,Object>> result =  alertCabinetColumnClient.queryBizSystemList(reqNew);
		
		String[] headerList = {"影响业务系统","业务联系人姓名","业务联系人手机号码","电源故障设备数","电源故障告警数量"};
        String[] keyList = {"bizSystem","business_concat","business_concat_phone","deviceAlertCount","alertCount"};
        String title = "影响业务";
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
            eeu.exportExcel(book, 0, title, headerList, result, keyList);
            book.write(os);
        } catch (Exception e) {
        	LOGGER.error("导出Excel数据失败!", e);
        }
	}
}
