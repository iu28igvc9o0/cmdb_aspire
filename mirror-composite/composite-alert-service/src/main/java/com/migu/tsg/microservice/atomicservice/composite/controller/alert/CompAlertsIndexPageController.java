package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsStatisticClassifyDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsTop10DTOResponse;
import com.aspire.mirror.composite.payload.alert.*;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.helper.ResourceAuthV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.alert.IComAlertIndexPageService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.constant.AlertConfigConstants;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CompAlertsIndexPageController implements IComAlertIndexPageService {

	@Autowired
	private AlertsIndexPageServiceClient indexPageServiceClient;
	
	@Autowired
	private CmdbV2Helper cmdbHelper;
	
	 @Autowired
    protected ResourceAuthV2Helper resAuthHelper;

	@Override
	public CompAlertStatisticSummaryResp summary(@RequestParam(value = "idcType", required = false) String idcType) {
		return jacksonBaseParse(CompAlertStatisticSummaryResp.class, indexPageServiceClient.summary(idcType));
	}

	@Override
	public CompAlertStatisticLevelResp alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span,
																  @RequestParam(value = "idcType", required = false) String idcType) {
		return jacksonBaseParse(CompAlertStatisticLevelResp.class,
				indexPageServiceClient.alertLevelSummayByTimeSpan(span, idcType));
	}

	@Override
	public Map trend(@RequestParam(value = "span") String inteval,
			@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel,
			@RequestParam(value = "source", required = false) String source) {
		return indexPageServiceClient.trend(inteval, idcType, deviceType, alertLevel, source);
	}

	@Override
	@ResAction(resType="mainIndexPage", action="classifyByTimeSpan", loadResFilter=true)
	public List<CompAlertsStatisticClassifyResp> classifyByTimeSpan(@RequestParam(value = "span") String span,
																	@RequestParam(value = "idcType", required = false) String idcType) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
		List<AlertsStatisticClassifyDTO> classifyList = indexPageServiceClient.classifyByTimeSpan(span, idcType);
		List<CompAlertsStatisticClassifyResp> respList = Lists.newArrayList();
		for (AlertsStatisticClassifyDTO classifyDTO : classifyList) {
			CompAlertsStatisticClassifyResp resp = new CompAlertsStatisticClassifyResp();
			BeanUtils.copyProperties(classifyDTO, resp);
			respList.add(resp);
		}
		return respList;
	}

	@Override
	public List<ComAlertsTop10DTOResponse> alertDeviceTop10(
			@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel,
			@RequestParam(value = "colName") String colName) {
		return jacksonBaseParse(ComAlertsTop10DTOResponse.class,
				indexPageServiceClient.alertTop10(idcType, deviceType, alertLevel, colName));
	}

	@Override
	public PageResponse<ComAlertsTop10DTOResponse> alertContentTop10(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel) {
		PageResponse<AlertsTop10DTOResponse> result = indexPageServiceClient
				.alertMoniIndexTop10(startDate + " 00:00:00", endDate + " 23:59:59", idcType, deviceType, alertLevel);
		PageResponse<ComAlertsTop10DTOResponse> response = new PageResponse<>();
		response.setCount(result.getCount());
		List<AlertsTop10DTOResponse> returnList = result.getResult();
		if (null != returnList && returnList.size() > 0) {
			response.setResult(jacksonBaseParse(ComAlertsTop10DTOResponse.class, returnList));
		}
		return response;
	}

	/**
	 * 最新警报列表
	 *
	 * @param operateStatus
	 * @return
	 */
	@Override
	public List<CompAlertsDetailResp> latest(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
	
		List<AlertsDetailResponse> detailResponseList = indexPageServiceClient.latest(null, null,operateStatus,
				startDate,endDate);
		List<CompAlertsDetailResp> respList = Lists.newArrayList();
		for (AlertsDetailResponse detailResponse : detailResponseList) {
			CompAlertsDetailResp resp = new CompAlertsDetailResp();
			String codeName = cmdbHelper.getCodeName(AlertConfigConstants.IDC_TRANSFER, detailResponse.getIdcType());
			if (StringUtils.isNotEmpty(codeName)) {
				detailResponse.setIdcType(codeName);
			}
			BeanUtils.copyProperties(detailResponse, resp);
			respList.add(resp);
		}
		return respList;
	}

	/**
	 * 导出最新热点告警
	 *
	 * @param operateStatus
	 * @param response
	 */
	@Override
	public void exportLatest(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response)
			throws Exception {

		List<String> headerLi = Lists.newArrayList();
		headerLi.add("级别");
		headerLi.add("设备IP");
		headerLi.add("资源池");
		headerLi.add("业务线");
		headerLi.add("设备分类");
		headerLi.add("告警内容");
		headerLi.add("告警时间");
		List<String> keyLi = Lists.newArrayList();
		keyLi.add("alertLevel");
		keyLi.add("deviceIp");
		keyLi.add("idcType");
		keyLi.add("bizSys");
		keyLi.add("deviceClass");
		keyLi.add("moniIndex");
		keyLi.add("alertStartTime");
		if (1 == operateStatus) {
			headerLi.add("处理人");
			headerLi.add("处理时间");
			keyLi.add("execUser");
			keyLi.add("execTime");
		}
		String[] headerList = headerLi.toArray(new String[]{});
		String[] keyList = keyLi.toArray(new String[]{});

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String title = "最新热点告警导出列表_" + sDateFormat.format(new Date());
		String fileName = title + ".xlsx";
		List<Map<String, Object>> dataList = indexPageServiceClient.exportLatest(null, null,operateStatus,
				startDate,endDate);
//       
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
		book.write(os);
	}
	
	@Override
	public List<Map<String,Object>> selectAlertsByOperateStatus(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "colType", required = false) String colType) {
		List<Map<String, Object>> mapList = indexPageServiceClient.selectAlertsByOperateStatus(operateStatus, startDate, endDate, colType);
		if ("idcType".equalsIgnoreCase(colType)) {
			for (Map<String, Object> map: mapList) {
				String col = MapUtils.getString(map, "col");
				String name = cmdbHelper.getCodeName(AlertConfigConstants.IDC_TRANSFER, col);
				if (StringUtils.isNotEmpty(name)) {
					map.put("col", name);
				}
			}
		}
		return mapList;
		
	}

	public List<Map<String, Object>> alertIdcDoHours(@RequestParam(value = "alertLevel") String alertLevel) {
		List<Map<String, Object>> mapList = indexPageServiceClient.alertIdcDoHours(alertLevel);
		for (Map<String, Object> map: mapList) {
			String col = MapUtils.getString(map, "idc_type");
			String name = cmdbHelper.getCodeName(AlertConfigConstants.IDC_TRANSFER, col);
			if (StringUtils.isNotEmpty(name)) {
				map.put("idc_type", name);
			}
		}
		return mapList;
	}
}
