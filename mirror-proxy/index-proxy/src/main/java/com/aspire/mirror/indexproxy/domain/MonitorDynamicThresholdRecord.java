package com.aspire.mirror.indexproxy.domain;

import static com.aspire.mirror.indexproxy.util.DateUtil.isWorkdayByClock;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: MonitorDynamicThresholdRecord
 * <p/>
 *
 * 类功能描述: 动态阈值配置对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of={"deviceId", "triggerId", "deviceItemId"}, callSuper=false)
public class MonitorDynamicThresholdRecord extends BasicDataOperateAware implements Serializable {
	private static final long	serialVersionUID	= 1157831097611669090L;
	
	@JsonProperty("model_id")
	private String modelId;
	
	@JsonProperty("trigger_id")
	private String triggerId;
	
	@JsonProperty("device_item_id")
	private String deviceItemId;
	
	@JsonProperty("device_id")
	private String deviceId;
	
	@JsonProperty("ip")
	private String ip;
	
	@JsonProperty("idc_type")
	private String idcType;
	
	@JsonProperty("create_time")
	private Date createTime;
	
	@JsonProperty("update_time")
	private Date updateTime;
	
	@JsonProperty("model_json")
	private String modelJson;
	
	@JsonProperty("thrid_system_id")
	private String thridSystemId;
	
	@JsonProperty("model_status")
	private String modelStatus;
	
	private DynamicJsonMeta dynamicJsonMeta;
	
	public void setModelJson(String modelJson) {
		this.modelJson = modelJson;
		this.dynamicJsonMeta = null;
	}
	
	@JsonIgnore
	public void resolveModelJsonMeta() {
		if (dynamicJsonMeta != null) {
			return;
		}
		if (StringUtils.isBlank(modelJson)) {
			dynamicJsonMeta = null;
			return;
		} 
		
		DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(modelJson);
		Integer modelType = jsonCtx.read("$.type", Integer.class);
		if (modelType.intValue() == DynamicJsonMeta.MODEL_TYPE_STABLE.intValue()) {
			dynamicJsonMeta = DynamicJsonMeta.ofStable(jsonCtx.read("$.low", Double.class), jsonCtx.read("$.high", Double.class));
			return;
		}
		PeriodicMeta workDayMeta = parsePeriodMeta(jsonCtx, DynamicJsonMeta.KEY_WORKDAY);
		PeriodicMeta noWorkDayMeta = parsePeriodMeta(jsonCtx, DynamicJsonMeta.KEY_NO_WORKDAY);
		dynamicJsonMeta = DynamicJsonMeta.ofPeriodic(workDayMeta, noWorkDayMeta);
	}
	
	private PeriodicMeta parsePeriodMeta(DocumentContext jsonCtx, String dayTypeKey) {
		PeriodicMeta periodMeta = new PeriodicMeta();
		periodMeta.setDelay(jsonCtx.read("$." + dayTypeKey + ".delay", Integer.class));
		TypeRef<Double[]> valTypeRef = new TypeRef<Double[]>() {};
		periodMeta.setLows(jsonCtx.read("$." + dayTypeKey + ".lows", valTypeRef));
		periodMeta.setHighs(jsonCtx.read("$." + dayTypeKey + ".highs", valTypeRef));
		TypeRef<List<String>> keysTypeRef = new TypeRef<List<String>>() {};
		List<String> keysList = jsonCtx.read("$." + dayTypeKey + ".keys", keysTypeRef);
		Integer[] dayClockArr = new Integer[keysList.size()];
		for (int i = 0; i < keysList.size(); i++) {
			dayClockArr[i] = LocalTime.parse(keysList.get(i)).toSecondOfDay();
		}
		periodMeta.setKeys(dayClockArr);
		return periodMeta;
	}
	
	public static void main(String[] args) {
		int clock = 1609343701;
		Instant instant = Instant.ofEpochMilli(clock * 1000L);
		ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	    System.out.println(localDateTime.toLocalTime().toSecondOfDay());
		
		String modelJson = "{\"noworkofdays\":{\"delay\":\"10\",\"keys\":[\"00:00:00\",\"00:10:00\",\"00:20:00\",\"00:30:00\",\"00:40:00\",\"00:50:00\",\"01:00:00\",\"01:10:00\",\"01:20:00\",\"01:30:00\",\"01:40:00\",\"01:50:00\",\"02:00:00\",\"02:10:00\",\"02:20:00\",\"02:30:00\",\"02:40:00\",\"02:50:00\",\"03:00:00\",\"03:10:00\",\"03:20:00\",\"03:30:00\",\"03:40:00\",\"03:50:00\",\"04:00:00\",\"04:10:00\",\"04:20:00\",\"04:30:00\",\"04:40:00\",\"04:50:00\",\"05:00:00\",\"05:10:00\",\"05:20:00\",\"05:30:00\",\"05:40:00\",\"05:50:00\",\"06:00:00\",\"06:10:00\",\"06:20:00\",\"06:30:00\",\"06:40:00\",\"06:50:00\",\"07:00:00\",\"07:10:00\",\"07:20:00\",\"07:30:00\",\"07:40:00\",\"07:50:00\",\"08:00:00\",\"08:10:00\",\"08:20:00\",\"08:30:00\",\"08:40:00\",\"08:50:00\",\"09:00:00\",\"09:10:00\",\"09:20:00\",\"09:30:00\",\"09:40:00\",\"09:50:00\",\"10:00:00\",\"10:10:00\",\"10:20:00\",\"10:30:00\",\"10:40:00\",\"10:50:00\",\"11:00:00\",\"11:10:00\",\"11:20:00\",\"11:30:00\",\"11:40:00\",\"11:50:00\",\"12:00:00\",\"12:10:00\",\"12:20:00\",\"12:30:00\",\"12:40:00\",\"12:50:00\",\"13:00:00\",\"13:10:00\",\"13:20:00\",\"13:30:00\",\"13:40:00\",\"13:50:00\",\"14:00:00\",\"14:10:00\",\"14:20:00\",\"14:30:00\",\"14:40:00\",\"14:50:00\",\"15:00:00\",\"15:10:00\",\"15:20:00\",\"15:30:00\",\"15:40:00\",\"15:50:00\",\"16:00:00\",\"16:10:00\",\"16:20:00\",\"16:30:00\",\"16:40:00\",\"16:50:00\",\"17:00:00\",\"17:10:00\",\"17:20:00\",\"17:30:00\",\"17:40:00\",\"17:50:00\",\"18:00:00\",\"18:10:00\",\"18:20:00\",\"18:30:00\",\"18:40:00\",\"18:50:00\",\"19:00:00\",\"19:10:00\",\"19:20:00\",\"19:30:00\",\"19:40:00\",\"19:50:00\",\"20:00:00\",\"20:10:00\",\"20:20:00\",\"20:30:00\",\"20:40:00\",\"20:50:00\",\"21:00:00\",\"21:10:00\",\"21:20:00\",\"21:30:00\",\"21:40:00\",\"21:50:00\",\"22:00:00\",\"22:10:00\",\"22:20:00\",\"22:30:00\",\"22:40:00\",\"22:50:00\",\"23:00:00\",\"23:10:00\",\"23:20:00\",\"23:30:00\",\"23:40:00\",\"23:50:00\"],\"highs\":[8.0,8.01,8.01,8.01,8.0,8.0,8.0,8.05,8.0,8.0,8.06,8.43,8.88,9.48,9.91,10.31,12.88,13.86,14.85,15.83,14.42,14.13,13.56,13.33,12.85,13.17,11.44,11.18,11.63,11.81,14.34,14.15,14.08,14.56,14.87,14.45,14.25,13.97,13.89,12.74,13.09,12.4,11.26,12.59,12.46,11.1,11.76,10.81,11.59,12.04,12.67,11.07,11.2,10.77,10.94,11.82,10.43,9.95,8.93,8.82,9.32,10.9,8.47,9.52,9.38,8.65,11.24,11.48,8.66,8.35,8.72,8.72,8.65,8.06,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.27,8.23,8.27,8.27,8.27,8.27,11.0,8.14,8.25,8.25,8.25,8.25,8.5,8.25,8.25,8.25,8.25,8.25,11.0,8.25,8.21,8.25,8.25,8.25,8.25,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0],\"lows\":[0.0123,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.03,0.18,0.18,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]},\"workofdays\":{\"delay\":\"10\",\"keys\":[\"00:00:00\",\"00:10:00\",\"00:20:00\",\"00:30:00\",\"00:40:00\",\"00:50:00\",\"01:00:00\",\"01:10:00\",\"01:20:00\",\"01:30:00\",\"01:40:00\",\"01:50:00\",\"02:00:00\",\"02:10:00\",\"02:20:00\",\"02:30:00\",\"02:40:00\",\"02:50:00\",\"03:00:00\",\"03:10:00\",\"03:20:00\",\"03:30:00\",\"03:40:00\",\"03:50:00\",\"04:00:00\",\"04:10:00\",\"04:20:00\",\"04:30:00\",\"04:40:00\",\"04:50:00\",\"05:00:00\",\"05:10:00\",\"05:20:00\",\"05:30:00\",\"05:40:00\",\"05:50:00\",\"06:00:00\",\"06:10:00\",\"06:20:00\",\"06:30:00\",\"06:40:00\",\"06:50:00\",\"07:00:00\",\"07:10:00\",\"07:20:00\",\"07:30:00\",\"07:40:00\",\"07:50:00\",\"08:00:00\",\"08:10:00\",\"08:20:00\",\"08:30:00\",\"08:40:00\",\"08:50:00\",\"09:00:00\",\"09:10:00\",\"09:20:00\",\"09:30:00\",\"09:40:00\",\"09:50:00\",\"10:00:00\",\"10:10:00\",\"10:20:00\",\"10:30:00\",\"10:40:00\",\"10:50:00\",\"11:00:00\",\"11:10:00\",\"11:20:00\",\"11:30:00\",\"11:40:00\",\"11:50:00\",\"12:00:00\",\"12:10:00\",\"12:20:00\",\"12:30:00\",\"12:40:00\",\"12:50:00\",\"13:00:00\",\"13:10:00\",\"13:20:00\",\"13:30:00\",\"13:40:00\",\"13:50:00\",\"14:00:00\",\"14:10:00\",\"14:20:00\",\"14:30:00\",\"14:40:00\",\"14:50:00\",\"15:00:00\",\"15:10:00\",\"15:20:00\",\"15:30:00\",\"15:40:00\",\"15:50:00\",\"16:00:00\",\"16:10:00\",\"16:20:00\",\"16:30:00\",\"16:40:00\",\"16:50:00\",\"17:00:00\",\"17:10:00\",\"17:20:00\",\"17:30:00\",\"17:40:00\",\"17:50:00\",\"18:00:00\",\"18:10:00\",\"18:20:00\",\"18:30:00\",\"18:40:00\",\"18:50:00\",\"19:00:00\",\"19:10:00\",\"19:20:00\",\"19:30:00\",\"19:40:00\",\"19:50:00\",\"20:00:00\",\"20:10:00\",\"20:20:00\",\"20:30:00\",\"20:40:00\",\"20:50:00\",\"21:00:00\",\"21:10:00\",\"21:20:00\",\"21:30:00\",\"21:40:00\",\"21:50:00\",\"22:00:00\",\"22:10:00\",\"22:20:00\",\"22:30:00\",\"22:40:00\",\"22:50:00\",\"23:00:00\",\"23:10:00\",\"23:20:00\",\"23:30:00\",\"23:40:00\",\"23:50:00\"],\"highs\":[8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,7.96,8.01,8.44,8.93,9.18,12.89,10.94,11.4,15.03,15.55,15.97,16.32,16.17,16.47,16.51,16.11,15.86,15.53,16.22,16.31,16.0,15.92,15.34,15.79,15.59,14.85,11.31,13.04,10.11,12.95,9.07,8.56,8.24,8.24,8.13,8.13,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,7.95,8.0,8.0,8.0,8.0,8.04,8.2,8.2,8.2,8.22,8.1,9.1,9.78,8.18,8.37,7.86,8.37,8.28,8.08,7.85,7.85,8.0,8.0,7.8,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.2,8.2,8.2,8.2,8.2,8.22,10.58,8.26,8.29,8.32,8.37],\"lows\":[0.0321,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.101,0.103,0.144,0.16,0.11,0.12,0.22,0.3,0.4,0.5,0.6,0.7,0.8,0.9,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7]},\"type\":\"1\"}";
		MonitorDynamicThresholdRecord record = new MonitorDynamicThresholdRecord();
		record.setModelJson(modelJson);
		Pair<Double, Double> valueSpan = record.resolveClockValueSpan(clock);
		System.out.println(valueSpan);
	}
	
	/** 
	 * 功能描述: 解析时间点对应的low-high阈值  
	 * <p>
	 * @param clockSeconds
	 * @return
	 */
	public Pair<Double, Double> resolveClockValueSpan(Integer clockSeconds) {
		resolveModelJsonMeta();
		return  dynamicJsonMeta == null ? null : dynamicJsonMeta.resolveClockValueSpan(clockSeconds);
	}
	
	@Slf4j
	@Getter
	private static class DynamicJsonMeta {
		public static final Integer				MODEL_TYPE_STABLE	= 2;				// 稳定型
		public static final Integer				MODEL_TYPE_PERIODIC	= 1;				// 周期型
		public static final String				KEY_WORKDAY			= "workofdays";
		public static final String				KEY_NO_WORKDAY		= "noworkofdays";
		private Integer							type;
		private Double							low;
		private Double							high;
		private final Map<String, PeriodicMeta>	periodMap			= new HashMap<>();
		
		public static DynamicJsonMeta ofStable(final Double low, final Double high) {
			DynamicJsonMeta meta = new DynamicJsonMeta();
			meta.type = MODEL_TYPE_STABLE;
			meta.low = low;
			meta.high = high;
			return meta;
		}
		
		public static DynamicJsonMeta ofPeriodic(final PeriodicMeta workDayMeta, final PeriodicMeta noWorkDayMeta) {
			DynamicJsonMeta meta = new DynamicJsonMeta();
			meta.type = MODEL_TYPE_PERIODIC;
			meta.periodMap.put(KEY_WORKDAY, workDayMeta);
			meta.periodMap.put(KEY_NO_WORKDAY, noWorkDayMeta);
			return meta;
		}
		
		/** 
		 * 功能描述: 根据时间点获取low-high阈值  
		 * <p>
		 * @param clockSeconds
		 * @return
		 */
		public Pair<Double, Double> resolveClockValueSpan(Integer clockSeconds) {
			if (MODEL_TYPE_STABLE.equals(type)) {
				return Pair.of(low, high);
			}
			
			PeriodicMeta periodMeta = isWorkdayByClock(clockSeconds) ? periodMap.get(KEY_WORKDAY) : periodMap.get(KEY_NO_WORKDAY);
			if (ArrayUtils.isEmpty(periodMeta.getKeys())) {
				log.warn("There is no dynamic time keys defined ...");
				return null;
			}
			
			int secondsOfDay = getSecondsOfDayByClock(clockSeconds);
			int idx = Arrays.binarySearch(periodMeta.getKeys(), secondsOfDay);
			if (idx >= 0) {
				return Pair.of(periodMeta.lows[idx], periodMeta.highs[idx]);
			}
			// 如果idx小于0, 说明未精确匹配到时间点, 此时找出idx上下2个时间点, 找出最靠近的那个点
			idx = (idx + 1) * -1;
			Integer lowIdx = null;
			Integer highIdx = null;
			int keySize = periodMeta.getKeys().length;
			// 当idx为两端时
			if (idx == 0 || idx == keySize) {
				lowIdx = keySize - 1;
				highIdx = 0;
			} else {
				lowIdx = idx - 1;
				highIdx = idx;
			}
			long timeLow = periodMeta.getKeys()[lowIdx];
			long timeHigh = periodMeta.getKeys()[highIdx];
			long middleTime = 0;
			if (timeLow > timeHigh) {
				long daySec = TimeUnit.HOURS.toSeconds(24);
				middleTime = (timeLow + timeHigh + daySec) / 2;
				if (secondsOfDay < timeLow) {
					secondsOfDay = secondsOfDay + (int)daySec;
				}
			} else {
				middleTime = (timeLow + timeHigh) / 2;
			}
			
			if (secondsOfDay > middleTime) {
				return Pair.of(periodMeta.lows[highIdx], periodMeta.highs[highIdx]);
			}
			return Pair.of(periodMeta.lows[lowIdx], periodMeta.highs[lowIdx]);
		}
		
		private int getSecondsOfDayByClock(Integer clockTime) {
			Instant instant = Instant.ofEpochMilli(clockTime * 1000L);
			ZoneId zone = ZoneId.systemDefault();
		    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		    return localDateTime.toLocalTime().toSecondOfDay();
		}
	}
	
	@Data
	private static class PeriodicMeta {
		private Integer		delay;
		private Integer[]	keys;
		private Double[]	highs;
		private Double[]	lows;
	}
}
