package com.aspire.cdn.esdatawrap.biz.thirdpartyalert;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.cdn.esdatawrap.anno.EmptyCheckWithConds;
import com.aspire.cdn.esdatawrap.biz.client.model.SyncItemResponse;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.biz.umsalert.UmsAlertMessage;
import com.aspire.cdn.esdatawrap.biz.umsalert.UmsAlertSendHelper;
import com.aspire.cdn.esdatawrap.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: ThirdpartyAlertReceiveBiz
 * <p/>
 *
 * 类功能描述: 第三方告警接入
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Service
public class ThirdpartyAlertReceiveBiz {
	
	@Autowired 
	private UmsAlertSendHelper umsAlertSendHelper;
	
	@Autowired
	private MetricAlertHelper  metricAlertHelper;
	
	
	/** 
	 * 功能描述: 接入第三方告警---单条  
	 * <p>
	 * @param alertBody
	 * @return
	 */
	public SyncItemResponse thirdpartyAlertSync(ThirdPartyAlertBody alertBody) {
		try {
			Pair<Boolean, SyncItemResponse> chkResult = checkAlertBody(alertBody);
			if (!chkResult.getLeft()) {
				return chkResult.getRight();
			}
			UmsAlertMessage umsAlert = alertBody.parseUmsAlert();
			List<UmsAlertMessage> filterAlertList = filterSkipAlert(Collections.singletonList(umsAlert));
			if (CollectionUtils.isEmpty(filterAlertList)) {
				return SyncItemResponse.SUCCESS;
			}
			SyncItemResponse resp = umsAlertSendHelper.sendUmsAlertMessage(filterAlertList.get(0));
			sync2MetricAlert(alertBody);
			return resp;
		} catch (Exception e) {
			log.error("Error when invoke checkAlertBody method.", e);
			return SyncItemResponse.GENERAL_ERROR;
		}
	}
	
	/** 
	 * 功能描述: 过滤掉  vip节点不可用  的 消警  
	 * <p>
	 * @param alertList
	 * @return
	 */
	private List<UmsAlertMessage> filterSkipAlert(final List<UmsAlertMessage> alertList) {
		return alertList.stream().filter(alert -> {
			return MetricAlert.MONI_RESULT_ACTIVE.toString().equals(alert.getMoniResult())
					|| !("B2BELK".equals(alert.getSource()) && "hyCDN_93".equals(alert.getItemKey()));
		}).collect(Collectors.toList());
	}
	
	/** 
	 * 功能描述: 接入第三方告警---批量  
	 * <p>
	 * @param alertList
	 * @return
	 */
	public SyncItemResponse thirdpartyAlertBatchSync(List<ThirdPartyAlertBody> alertList) {
		List<UmsAlertMessage> validList = new ArrayList<>();
		List<ThirdPartyAlertBody> validSourceList = new ArrayList<>();
		for (ThirdPartyAlertBody alertBody : alertList) {
			try {
				Pair<Boolean, SyncItemResponse> chkResult = checkAlertBody(alertBody);
				if (!chkResult.getLeft()) {
					log.error(chkResult.getRight().getMessage());
					continue;
				}
				validSourceList.add(alertBody);
				validList.add(alertBody.parseUmsAlert());
			} catch (Exception e) {
				log.error("Error when invoke checkAlertBody method.", e);
			}
		}
		if (validList.isEmpty()) {
			String tip = "All of the thirdparty batch alerts are invalid.";
			log.warn(tip);
			return SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, tip);
		}
		List<UmsAlertMessage> filterAlertList = filterSkipAlert(validList);
		if (CollectionUtils.isEmpty(filterAlertList)) {
			return SyncItemResponse.SUCCESS;
		}
		SyncItemResponse resp = umsAlertSendHelper.batchSendUmsAlertMessage(filterAlertList);
		sync2MetricAlert(validSourceList.toArray(new ThirdPartyAlertBody[validSourceList.size()]));
		return resp;
	}
	
	/** 
	 * 功能描述: 验证第三方告警消息体  
	 * <p>
	 * @param alertBody
	 * @return
	 * @throws Exception
	 */
	public Pair<Boolean, SyncItemResponse> checkAlertBody(ThirdPartyAlertBody alertBody) throws Exception {
		String alertType = alertBody.getObjectType(); 
		if (!ThirdPartyAlertBody.ALERT_TYPE_DEVICE.equals(alertType)
				&& !ThirdPartyAlertBody.ALERT_TYPE_BIZ.equals(alertType)) {
			return Pair.of(false, 
					SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, "The value of 'moni_result' is null or invalid."));
		}
		
		for (Pair<Field, EmptyCheckWithConds> checkField : ThirdPartyAlertBody.fetchNotEmptyCheckFiledList()) {
			Field fd = checkField.getKey();
			EmptyCheckWithConds anno = checkField.getValue();
			String tipKey = StringUtils.isBlank(anno.tipKey()) ? fd.getName() : anno.tipKey();
			
			List<String> conds = Arrays.asList(anno.conds());
			if (!anno.chkNotNull() 
					|| (!conds.contains("*") && !conds.contains(alertType))) {
				continue;
			}
			
			Object fieldVal = fd.get(alertBody);
			if (fieldVal == null) {
				return Pair.of(false, SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, 
						"The value of '" + tipKey + "' is null."));
			}
			if (!anno.chkNotBlank()) {
				continue;
			}
			
			Class<?> fieldClazz = fd.getType();
			if (String.class.isAssignableFrom(fieldClazz) && StringUtils.isBlank(String.class.cast(fieldVal))) {
				return Pair.of(false, SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, 
						"The value of '" + tipKey + "' is blank."));
			}
			if (List.class.isAssignableFrom(fieldClazz) && (List.class.cast(fieldVal)).isEmpty()) {
				return Pair.of(false, SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, "The '" + tipKey + "' is emtpy."));
			}
			if (Map.class.isAssignableFrom(fieldClazz) && (Map.class.cast(fieldVal)).isEmpty()) {
				return Pair.of(false, SyncItemResponse.build(SyncItemResponse.ABSENT_PARAMS, "The '" + tipKey + "' is emtpy."));
			}
		}
		// 验证告警时间格式为 "yyyy-MM-dd HH:mm:ss"
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			format.parse(alertBody.getCurMoniTime());
		} catch (ParseException e) {
			log.error("Error when parse the curMoniTime value of thirdparty alert.", e);
			return Pair.of(false, SyncItemResponse.build(SyncItemResponse.WRONG_TIME_FORMAT, 
					"The time format of the 'cur_moni_time' is incorrect, it should be in format of 'yyyy-MM-dd HH:mm:ss'."));
		}
		return Pair.of(true, null);
	}
	
	/** 
	 * 功能描述: 存入 ES索引Metric_alert  
	 * <p>
	 */
	private void sync2MetricAlert(ThirdPartyAlertBody ... alertArr) {
		for (ThirdPartyAlertBody body : alertArr) {
			try {
				boolean flag = false;
				if ("B2BELK".equals(body.getSourceIdentify()) && "B2B".equals(body.getServSystem())) {
					flag = true;
				} 
				else if ("InternetTV".equals(body.getSourceIdentify()) && "OTT".equals(body.getServSystem())) {
					log.info("As the thirdparty-alert is of OTT-InternetTV, it will be rewrite the metric-alert ES index. detail:"
							+ JsonUtil.toJacksonJson(body));
					flag = true;
				}
				if (!flag) {
					continue;
				}
				MetricAlert metricAlert = body.parse2MetricAlert();
				metricAlertHelper.processMetricAlert(metricAlert);
			} catch (Exception e) {
				log.error(null, e);
			}
		}
	}
}
