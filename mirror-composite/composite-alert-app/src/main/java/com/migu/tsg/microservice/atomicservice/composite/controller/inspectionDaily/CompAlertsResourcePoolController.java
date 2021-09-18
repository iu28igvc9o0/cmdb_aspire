package com.migu.tsg.microservice.atomicservice.composite.controller.inspectionDaily;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyReq;
import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyResponse;
import com.aspire.mirror.composite.payload.inspectionDaily.ComAlertInspectionDailyReq;
import com.aspire.mirror.composite.payload.inspectionDaily.ComAlertInspectionDailyResponse;
import com.aspire.mirror.composite.service.inspectionDaily.ICompAlertsResourcePoolService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspectionDaily.AlertResourcePoolClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
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
public class CompAlertsResourcePoolController implements ICompAlertsResourcePoolService {
	private Logger logger = LoggerFactory.getLogger(CompAlertsResourcePoolController.class);

	@Autowired
	private AlertResourcePoolClient alertResourcePoolClient;


	@Override
	public PageResponse<ComAlertInspectionDailyResponse> inspectionDaily(@RequestBody ComAlertInspectionDailyReq pageRequset) {
		logger.info("method[pageRequset] request body is {}", pageRequset);
		// 调原子层
		AlertInspectionDailyReq alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertInspectionDailyReq.class, pageRequset);
		PageResponse<AlertInspectionDailyResponse> pageResponse = alertResourcePoolClient.inspectionDaily(alertsPageRequset);
		PageResponse<ComAlertInspectionDailyResponse> response = new PageResponse<ComAlertInspectionDailyResponse>();
		response.setCount(pageResponse.getCount());
		List<ComAlertInspectionDailyResponse> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
			alertList = jacksonBaseParse(ComAlertInspectionDailyResponse.class, pageResponse.getResult());
		}
		response.setResult(alertList);
		return response;
	}


	@Override
	public void exportDaily(ComAlertInspectionDailyReq pageRequest) {
		 ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        HttpServletResponse response = servletRequestAttributes.getResponse();
	        
	        AlertInspectionDailyReq alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertInspectionDailyReq.class, pageRequest);
	        try {
	        	List<Map<String,Object>> configDictAll = alertResourcePoolClient.exportDaily(alertsPageRequset);
	            //logger.info("[alert exportDaily] >>> {}", configDictAll );
	            String[] headerList = {"工程期数","所属POD","设备大类","设备小类","设备厂家","设备型号","设备数量","重大告警数量"
	            		,"高告警数量","中告警数量","低告警数量","告警数量"};
	            String[] keyList = {"project_name","pod_name","device_class","device_type","device_mfrs"
	            		,"device_model","count","impCount","highCount","middCount","lowCount","alertCount"};
	            String title = "巡检日报";
	            String fileName = title+".xlsx";
	            OutputStream os = response.getOutputStream();// 取得输出流
	            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
	            response.setHeader("Connection", "close");
	            response.setHeader("Content-Type", "application/vnd.ms-excel");
	            //excel constuct
	            ExportExcelUtil eeu = new ExportExcelUtil();
	            Workbook book = new SXSSFWorkbook(128);
	            eeu.exportExcel(book, 0, title, headerList, configDictAll, keyList);
	            book.write(os);
	        } catch (Exception e) {
	        	logger.error("[export exportDaily is error] >>> " + e);
	        }
	}

}
