package com.aspire.mirror.alert.server.controller.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsStatisticClassifyDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsTop10DTOResponse;
import com.aspire.mirror.alert.api.service.alert.AlertIndexPageService;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.alert.AlertsIndexPageBiz;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.third.AlertStorageUseRateDao;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.alert.AlertsTop10Vo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AlertIndexPageController implements AlertIndexPageService {
	@Autowired
	private AlertsIndexPageBiz alertsIndexPageBiz;
	
	@Autowired
	private AlertStorageUseRateDao alertStorageUseRateDao;

	@Value("${alert.branch:}")
	private String branch;
	@Autowired
	private AuthHelper authHelper;

	@Override
	public AlertStatisticSummaryResponse summary(@RequestParam(value = "idcType", required = false) String idcType) {
		AlertStatisticSummaryResponse response = new AlertStatisticSummaryResponse();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String startTime = simpleDateFormat.format(date);
		String endTime = DateUtils.getSpecifiedDayAfter(startTime,1);
		AlertStatisticSummaryDTO toBeConfirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsIndexPageBiz.getSummaryByOperateType(0, idcType,startTime,endTime));// 待确认
		response.setToBeConfirmed(toBeConfirmed);
		AlertStatisticSummaryDTO confirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsIndexPageBiz.getSummaryByOperateType(1, idcType,startTime,endTime));
		response.setConfirmed(confirmed);// 已确认

		AlertStatisticSummaryDTO toBeResolved = new AlertStatisticSummaryDTO();
		toBeResolved.setSummary(toBeConfirmed.getSummary() + confirmed.getSummary());
		toBeResolved.setLow(toBeConfirmed.getLow() + confirmed.getLow());
		toBeResolved.setMedium(toBeConfirmed.getMedium() + confirmed.getMedium());
		toBeResolved.setHigh(toBeConfirmed.getHigh() + confirmed.getHigh());
		toBeResolved.setSerious(toBeConfirmed.getSerious() + confirmed.getSerious());
		response.setToBeResolved(toBeResolved);// 待解决 = 待确认 + 已确认

		response.setResolved(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsIndexPageBiz.getHisSummaryByOperateType(idcType,startTime,endTime)));// 已处理
		return response;
	}

	@Override
	public AlertStatisticSummaryDTO alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span,
			@RequestParam(value = "idcType", required = false) String idcType) {
		if (StringUtils.isEmpty(span)) {
			return null;
		}
		Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
		Date endDate = DateUtils.getTimesmorning();
		return PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsIndexPageBiz.getSummaryByDateRange(startDate, endDate, idcType));
	}

	@Override
	public Map trend(@RequestParam(value = "span") String inteval,
			@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel,
			@RequestParam(value = "source", required = false) String source) {
		if(StringUtils.isNotBlank(source)) {
			if(source.equals("日志")) {
				source = "syslog";
			}
		}
		return alertsIndexPageBiz.getTrendSeries(inteval, idcType, deviceType, alertLevel, source);
	}

	@Override
	public List<AlertsStatisticClassifyDTO> classifyByTimeSpan(@RequestParam(value = "span") String span,
			@RequestParam(value = "idcType", required = false) String idcType) {
		if (StringUtils.isEmpty(span)) {
			return null;
		}
		Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
		Date endDate = DateUtils.getTimesmorning();
		// 获取数据权限
		Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
		return PayloadParseUtil.jacksonBaseParse(AlertsStatisticClassifyDTO.class, alertsIndexPageBiz.getClassifySeries(startDate, endDate, idcType,resFilterMap));
	}

	@Override
	public List<AlertsTop10DTOResponse> alertTop10(@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel,
			@RequestParam(value = "colName") String colName) {
		if (StringUtils.isEmpty(colName)) {
			return null;
		}
		return PayloadParseUtil.jacksonBaseParse(AlertsTop10DTOResponse.class, alertsIndexPageBiz.getAlertTop10(idcType, deviceType, alertLevel, colName));
	}

	@Override
	public PageResponse<AlertsTop10DTOResponse> alertMoniIndexTop10(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "alertLevel", required = false) String alertLevel) {
		PageResponse<AlertsTop10Vo> alertMoniIndexTop10 = alertsIndexPageBiz.getAlertMoniIndexTop10(startDate, endDate, idcType, deviceType, alertLevel);
		PageResponse<AlertsTop10DTOResponse> pageResponse = new PageResponse<>();
		pageResponse.setCount(alertMoniIndexTop10.getCount());
		pageResponse.setResult(PayloadParseUtil.jacksonBaseParse(AlertsTop10DTOResponse.class, alertMoniIndexTop10.getResult()));
		return pageResponse;
	}

	@Override
	public List<AlertsDetailResponse> latest(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
			if(StringUtils.isBlank(startDate)) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				endDate = sdf1.format( now);
				startDate = sdf.format(now)+" 00:00:00";
			}
			if (AlertConfigConstants.BRANCH_IT_YUN.equalsIgnoreCase(branch)) {
				return TransformUtils.transform(AlertsDetailResponse.class,
						alertsIndexPageBiz.getLatestOrder(limit,idcType,operateStatus,null,null));
			} else {
				return TransformUtils.transform(AlertsDetailResponse.class,
						alertsIndexPageBiz.getLatest(limit,idcType,operateStatus,startDate,endDate));
			}
	}

	/**
     * 导出警报列表数据
     */
    @Override
    public List<Map<String, Object>> exportLatest(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws Exception {
    	if(StringUtils.isBlank(startDate)) {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			endDate = sdf1.format( now);
			startDate = sdf.format(now)+" 00:00:00";
		} 
        List<Map<String, Object>> dataLists = Lists.newArrayList();
		if (AlertConfigConstants.BRANCH_IT_YUN.equalsIgnoreCase(branch)) {
			List<Alerts> alertsList = alertsIndexPageBiz.getLatestOrder(limit, idcType, operateStatus, null, null);
			for (Alerts alerts: alertsList) {
				alerts.setAlertLevel(AlertCommonConstant.alertLevelMap.get(alerts.getAlertLevel()));
				Map<String, Object> val = objectToMap(alerts);
				String pattern = "yyyy-MM-dd HH:mm:ss";
				for (Map.Entry<String, Object> entry: val.entrySet()) {
					Object value = entry.getValue();
					if (value == null) {
						continue;
					}
					if (value instanceof Date) {
						Date date = (Date) value;
						val.put(entry.getKey(), DateUtil.format(date, pattern));
					}
				}
				dataLists.add(val);
			}

		} else {
			List<AlertsVo> pageResult =  alertsIndexPageBiz.getLatest(limit,idcType,operateStatus,startDate,endDate);
			for (AlertsVo alertsVo : pageResult) {
				alertsVo.setAlertLevel(AlertCommonConstant.alertLevelMap.get(alertsVo.getAlertLevel()));
				Map<String, Object> val = objectToMap(alertsVo);
				String pattern = "yyyy-MM-dd HH:mm:ss";
				if(null!=val.get("alertStartTime")) {
					Date start = (Date)val.get("alertStartTime");
					val.put("alertStartTime", DateUtil.format(start, pattern));
				}
				if(null!=val.get("curMoniTime")) {
					Date start = (Date)val.get("curMoniTime");
					val.put("curMoniTime", DateUtil.format(start, pattern));
				}

				dataLists.add(val);
			}
		}

        return dataLists;
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

	/*****************************************************
	 * 监控首页
	 ************************************/

	
	@Override
	public Map<String, Object> StorageUseRate() {
		return alertStorageUseRateDao.getStorageUserate();
	}
	
	
	@Override
	public List<Map<String,Object>> selectAlertsByOperateStatus(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "colType", required = false) String colType) {
		if(StringUtils.isBlank(startDate)) {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			endDate = sdf1.format( now);
			startDate = sdf.format(now)+" 00:00:00";
			//startDate = "2020-05-01";
		}
		if (AlertConfigConstants.BRANCH_IT_YUN.equalsIgnoreCase(branch)) {
			if(colType.equals("idcType")) {
				return alertsIndexPageBiz.selectIdcTypeAlertsByOperateStatusOrder(operateStatus,null,null);
			}else {
				return alertsIndexPageBiz.selectDeviceTypeAlertsByOperateStatusOrder(operateStatus,null,null);
			}
		} else {
			if(colType.equals("idcType")) {
				return alertsIndexPageBiz.selectIdcTypeAlertsByOperateStatus(operateStatus,startDate,endDate);
			}else {
				return alertsIndexPageBiz.selectDeviceTypeAlertsByOperateStatus(operateStatus,startDate,endDate);
			}
		}

		
	}

	public List<Map<String, Object>> alertIdcDoHours(@RequestParam(value = "alertLevel") String alertLevel) {
		return alertsIndexPageBiz.alertIdcDoHours(alertLevel);
	}

}
