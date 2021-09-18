package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AlertsIndexPageBiz;
import com.aspire.mirror.alert.server.config.properties.OrderOvertimeProperties;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.alert.AlertsDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsIndexPageDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsTransferDao;
import com.aspire.mirror.alert.server.dao.alert.po.*;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsStatisticClassifyVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsTop10Vo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.alert.server.constant.OrderStatusEnum;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

@Data
@Service
@Slf4j
@EnableConfigurationProperties(OrderOvertimeProperties.class)
public class AlertsIndexPageBizImpl implements AlertsIndexPageBiz {
    @Autowired
    private AlertsDao alertsDao;
    @Autowired
    private AlertsIndexPageDao alertsIndexPageDao;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsTransferDao alertsTransferDao;
    @Autowired
    private OrderOvertimeProperties orderOvertimeProperties;
    private static final String ALERT_LEVEL_TIP = "1";
    private static final String ALERT_LEVEL_LOW = "2";
    private static final String ALERT_LEVEL_MEDIUM = "3";
    private static final String ALERT_LEVEL_HIGH = "4";
    private static final String ALERT_LEVEL_SERIOUS = "5";

    private static final String TIME_RANGE_WEEK = "week";
    private static final String TIME_RANGE_MONTH = "month";
    private static final String TIME_RANGE_SEASON = "season";
    private static final String TIME_RANGE_YEAR = "year";

    private static final int ALERT_OPERATE_STATUS_CONFIRMED = 1;

    private AlertStatisticSummaryVo calcSummay(List<AlertsStatisticOverview> overviewList) {
        int tip = 0;
        int low = 0;
        int medium = 0;
        int high = 0;
        int serious = 0;
        int confirmed = 0;
        int summary = 0;
        	for (AlertsStatisticOverview overview : overviewList) {
                switch (overview.getAlertLevel()) {
                    case ALERT_LEVEL_TIP:
                        tip += overview.getCount();
                        break;
                    case ALERT_LEVEL_LOW:
                        low += overview.getCount();
                        break;
                    case ALERT_LEVEL_MEDIUM:
                        medium += overview.getCount();
                        break;
                    case ALERT_LEVEL_HIGH:
                        high += overview.getCount();
                        break;
                    case ALERT_LEVEL_SERIOUS:
                        serious += overview.getCount();
                        break;
                    default:
                        break;
                }
                if (overview.getOperateStatus() != null && overview.getOperateStatus() == ALERT_OPERATE_STATUS_CONFIRMED) {
                    confirmed += overview.getCount();
                }
                summary += overview.getCount();
            }

        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        summaryDTO.setSummary(summary);
        summaryDTO.setConfirmed(confirmed);
        summaryDTO.setTip(tip);
        summaryDTO.setLow(low);
        summaryDTO.setMedium(medium);
        summaryDTO.setHigh(high);
        summaryDTO.setSerious(serious);
        return summaryDTO;
    }

    /**
     * 根据操作代码，获取警报统计数据
     * @param code
     * @return
     */
    @Override
    public AlertStatisticSummaryVo getSummaryByOperateType(Integer code,String idcType,String startTime,String endTime) {
        List<Map<String, Object>> maps = alertsIndexPageDao.selectSummeryByOperateCode(code,idcType,startTime,endTime);
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        response.setSummary(0);
        response.setConfirmed(0);
        response.setHigh(0);
        response.setLow(0);
        response.setMedium(0);
        response.setSerious(0);
        response.setTip(0);
        if (!CollectionUtils.isEmpty(maps)) {
            Map<String, Object> codeMap = maps.get(0);
            response.setSummary(codeMap.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(codeMap.get("codeCount"))));
            response.setSerious(codeMap.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(codeMap.get("sCount"))));
            response.setHigh(codeMap.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(codeMap.get("hCount"))));
            response.setMedium(codeMap.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(codeMap.get("mCount"))));
            response.setLow(codeMap.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(codeMap.get("lCount"))));
        }
        return response;
    }

    @Override
    public AlertStatisticSummaryVo getHisSummaryByOperateType(String idcType,String startTime,String endTime) {
        Map<String, Integer> stringIntegerMap = alertsIndexPageDao.selectHisSummary(idcType,startTime,endTime);
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        response.setSummary(0);
        response.setConfirmed(0);
        response.setHigh(0);
        response.setLow(0);
        response.setMedium(0);
        response.setSerious(0);
        response.setTip(0);
        if(null != stringIntegerMap) {
        	response.setSummary(stringIntegerMap.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(stringIntegerMap.get("codeCount"))));
            response.setSerious(stringIntegerMap.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(stringIntegerMap.get("sCount"))));
            response.setHigh(stringIntegerMap.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(stringIntegerMap.get("hCount"))));
            response.setMedium(stringIntegerMap.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(stringIntegerMap.get("mCount"))));
            response.setLow(stringIntegerMap.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(stringIntegerMap.get("lCount"))));
        }
        
        return response;
    }


    /**
     * 根据时间区间获取警报数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public AlertStatisticSummaryVo getSummaryByDateRange(Date startDate, Date endDate, String idcType) {
        return convertLevel(alertsIndexPageDao.selectByDateRange(startDate, endDate,idcType));
    }
    
    private AlertStatisticSummaryVo convertLevel(List<AlertsStatisticOverview> overviewList) {
        int tip = 0;
        int low = 0;
        int medium = 0;
        int high = 0;
        int serious = 0;
        int summary = 0;
        for (AlertsStatisticOverview overview : overviewList) {
            switch (overview.getAlertLevel()) {
                case ALERT_LEVEL_TIP:
                    tip += overview.getCount();
                    break;
                case ALERT_LEVEL_LOW:
                    low += overview.getCount();
                    break;
                case ALERT_LEVEL_MEDIUM:
                    medium += overview.getCount();
                    break;
                case ALERT_LEVEL_HIGH:
                    high += overview.getCount();
                    break;
                case ALERT_LEVEL_SERIOUS:
                    serious += overview.getCount();
                    break;
                default:
                    break;
            }
            summary += overview.getCount();
        }
        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        summaryDTO.setSummary(summary);
        summaryDTO.setTip(tip);
        summaryDTO.setLow(low);
        summaryDTO.setMedium(medium);
        summaryDTO.setHigh(high);
        summaryDTO.setSerious(serious);
        return summaryDTO;
    }


    /**
     * 根据不同的时间跨度获取不周级别的警报数据
     * @param span
     * @return
     */
   @Override
    public Map<String, Object> getTrendSeries(String span, String idcType, String deviceType, String alertLevel,
			String source) {
        Map<String, Object> map = new HashMap<>();
        Date startDate = null;
        Date endDate = new Date();
        String interval;
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        List<AlertsStatisticTrend> trendList;
       Locale.setDefault(Locale.CHINA);
        switch (span.toLowerCase()) {
            case TIME_RANGE_WEEK: // 展示前7天数据
                c.add(Calendar.DATE, -7);
                startDate = c.getTime();
                interval = "day";
                break;
            case TIME_RANGE_MONTH:// 展示前30天数据
                c.add(Calendar.MONTH, -1);
                startDate = c.getTime();
                interval = "day";
                break;
            case TIME_RANGE_SEASON: // 展示前12周数据
                c.add(Calendar.MONTH, -3);
                startDate = c.getTime();
                interval = "week";
                break;
            case TIME_RANGE_YEAR: // 展示前12个月的数据
                c.add(Calendar.YEAR, -1);
                startDate = c.getTime();
                interval = "month";
                break;
            default:
                startDate = endDate;
                interval = "day";
                break;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        params.put("interval", interval);
        params.put("idcType", idcType);
        if(StringUtils.isNotBlank(deviceType)) {
        	if(deviceType.equals("服务器")) {
        		 params.put("deviceClass", deviceType);
        	}else {
        		 params.put("deviceType", deviceType);
        	}
        }
        //params.put("deviceType", deviceType);
        params.put("source", source);
        params.put("alertLevel", alertLevel);
        trendList = alertsIndexPageDao.selectTrendBySection(params);
        Set<String> sectionSet = new TreeSet<>();
		/*
		 * for (AlertsStatisticTrend trend : trendList) {
		 * sectionSet.add(trend.getSection()); }
		 */
        Set<String> levelSet = new HashSet<>();
        Map<String, Map<String, Integer>> levelMap = new HashMap<>();
        for (AlertsStatisticTrend trend : trendList) {
        	sectionSet.add(trend.getSection());
            String level = trend.getLevel();
            if (levelSet.contains(level)) {
            	Map<String, Integer> dataMap = levelMap.get(level);
            	dataMap.put(trend.getSection(), trend.getAlertNum());
            } else {
                List<AlertsStatisticTrend> innerList = Lists.newArrayList();
                innerList.add(trend);
                Map<String, Integer> dataMap = new HashMap<>();
                dataMap.put(trend.getSection(), trend.getAlertNum());
                levelMap.put(level, dataMap);
                levelSet.add(level);
            }
        }
        Map<String, List<Integer>> seriesMap = new HashMap<>();
        for(String str : sectionSet) {
        	for(Map.Entry<String,Map<String, Integer>> m : levelMap.entrySet()) {
        		String key = m.getKey()+"";
        		Map<String, Integer> val = m.getValue();
        		if(seriesMap.containsKey(key)) {
        			List<Integer> list = seriesMap.get(key);
        			list.add(val.get(str)==null?0:val.get(str));
        		}else {
        			List<Integer> list = Lists.newArrayList();
        			list.add(val.get(str)==null?0:val.get(str));
        			seriesMap.put(key+"", list);
        		}
        	}
        }
		/*
		 * Map<String, List<Map<String, Integer>>> series = new HashMap<>(); if
		 * (levelMap != null) { Iterator<Map.Entry<String, List<AlertsStatisticTrend>>>
		 * iterator = levelMap.entrySet().iterator(); while (iterator.hasNext()) {
		 * Map.Entry<String, List<AlertsStatisticTrend>> entry = iterator.next(); String
		 * level = entry.getKey(); List<AlertsStatisticTrend> trends = entry.getValue();
		 * series.put(level, dealInnerSeries(sectionSet, trends)); } }
		 */
        map.put("xAxis", new ArrayList<>(sectionSet));
        map.put("series", seriesMap);
        return map;
    }

    private List<Map<String, Integer>> dealInnerSeries(Set<String> sectionSet, List<AlertsStatisticTrend> trends){
        List<Map<String, Integer>> list = Lists.newArrayList();
        for (String section : sectionSet) {
            Map<String, Integer> map = new HashMap<>();
            map.put(section, 0);
            for (AlertsStatisticTrend trend : trends) {
                if (section.equals(trend.getSection())) {
                    map.put(section, trend.getAlertNum());
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 警报类型数据分布
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<AlertsStatisticClassifyVo> getClassifySeries(Date startDate, Date endDate, String idcType, Map<String, List<String>> resFilterMap) {
        List<AlertsStatisticClassifyVo> list = Lists.newArrayList();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        params.put("idcType", idcType);
        List<AlertsStatisticClassify> classifyList = alertsIndexPageDao.selectAlertsClassify(params,resFilterMap);
        for (AlertsStatisticClassify classify : classifyList) {
        	if(StringUtils.isBlank(classify.getDeviceType())) {
        		continue;
        	}
        	 if(list.size()==5) {
             	classify.setDeviceType("其他");
             }
            AlertsStatisticClassifyVo dto = new AlertsStatisticClassifyVo();
            BeanUtils.copyProperties(classify, dto);
           
            if(list.size()==6) {
                AlertsStatisticClassifyVo v = list.get(5);
            	if(null!=classify.getAlertNum()) {
            		v.setAlertNum(v.getAlertNum() + classify.getAlertNum());
            	}
            	continue;
            }
            list.add(dto);
        }
        return list;
    }
    
    @Override
    public List<AlertsTop10Vo> getAlertTop10(String idcType,
                                             String deviceType, String alertLevel, String colName) {
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        if(StringUtils.isNotBlank(deviceType)) {
        	if(deviceType.equals("服务器")) {
        		 params.put("deviceClass", deviceType);
        	}else {
        		 params.put("deviceType", deviceType);
        	}
        }
        params.put("alertLevel", alertLevel);
        params.put("size", 10);
        
        if(colName.equals("mfrs")) {
        	params.put("colName", "device_mfrs");
        }else if(colName.equals("podName")) {
        	params.put("colName", "pod_name");
        }else if(colName.equals("deviceModel")) {
        	params.put("colName", "device_model");
        }else if(colName.equals("deviceType")) {
            params.put("colName", "device_type");
        }else {
        	return null;
        }
//        Date startDate1 = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss";
//		//String endDate = DateUtils.getDateStr(startDate1, "yyyy-MM-dd HH:mm:ss");
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.DATE, -1);
//		Date d = c.getTime();
        long l = System.currentTimeMillis();
        params.put("beginDate", DateUtil.format(new Date(l/(24 * 3600 *1000) * 24 * 3600 *1000), pattern));
		params.put("endDate", DateUtil.format(new Date(l), pattern));
		//startDate = DateUtils.getDateStr(d, "yyyy-MM-dd");
		
        List<AlertsTop10Vo> toplist = alertsIndexPageDao.selectAlertsTop10(params);
        return toplist;
    }
    
    @Override
    public PageResponse<AlertsTop10Vo> getAlertMoniIndexTop10(String startDate, String endDate, String idcType,
                                                              String deviceType, String alertLevel) {
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        if(StringUtils.isNotBlank(deviceType)) {
        	if(deviceType.equals("服务器")) {
        		 params.put("deviceClass", deviceType);
        	}else {
        		 params.put("deviceType", deviceType);
        	}
        }
       
        params.put("alertLevel", alertLevel);
        params.put("size", 10);
        
    	params.put("beginDate", startDate);
        params.put("endDate", endDate);
    	params.put("colName", "moni_index");
        
    	if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
    		 Date startDate1 = new Date();
    	 		//String endDate = DateUtils.getDateStr(startDate1, "yyyy-MM-dd HH:mm:ss");
    	 		Calendar c = Calendar.getInstance();
    	 		c.setTime(new Date());
    	 		c.add(Calendar.DATE, -7);
    	 		Date d = c.getTime();
    	 		params.put("beginDate", d);
    	 		params.put("endDate", startDate1);
    	}
    	
    	PageResponse<AlertsTop10Vo> pageResponse = new PageResponse<>();
        int totalCount = alertsIndexPageDao.selectAlertsMoniIndexCount(params);
        if(totalCount > 0) {
        	List<AlertsTop10Vo> toplist = alertsIndexPageDao.selectAlertsTop10(params);
             pageResponse.setResult(toplist);
        }
        pageResponse.setCount(totalCount);
        return pageResponse;
    }

	@Override
	public List<AlertsVo> getLatest(Integer limit, String idcType, Integer operateStatus,
                                    String startDate, String endDate) {
		List<Alerts> alertsList = alertsIndexPageDao.selectLatestAlertsByOperateStatus(limit,idcType,operateStatus
				,startDate,endDate);
        return TransformUtils.transform(AlertsVo.class, alertsList);
	}

    public List<Alerts> getLatestOrder(Integer limit, String idcType,Integer operateStatus,
                                   String startDate, String endDate) {
        List<Alerts> alertsList = alertsIndexPageDao.selectLatestOrderByOperateStatus(limit,idcType,operateStatus
                ,startDate,endDate);

        if (operateStatus != null && OrderStatusEnum.TO_DO.getCode().equalsIgnoreCase(operateStatus.toString())) {
            doOrderExpire(alertsList, orderOvertimeProperties.getTodo(), orderOvertimeProperties.getTodoRemind());
        } else if (operateStatus != null && OrderStatusEnum.DOING.getCode().equalsIgnoreCase(operateStatus.toString())) {
            doOrderExpire(alertsList, orderOvertimeProperties.getDoing(), orderOvertimeProperties.getDoingRemind());
        }
        return alertsList;
    }

    /**
     * 处理工单超时状态
     * @param alertsList
     * @param jsonObject
     */
    private void doOrderExpire (List<Alerts> alertsList, Map<String, Integer> jsonObject, Map<String, Integer> jsonObjectRemind) {
        long l = System.currentTimeMillis();
        // 判断超时
        if (jsonObject != null) {
            for (Alerts alerts: alertsList) {
                Date execTime = alerts.getExecTime();
                String alertLevel = alerts.getAlertLevel();
                if (StringUtils.isEmpty(alertLevel) || execTime == null) {
                    continue;
                }
                Integer intMinute = jsonObject.get(alertLevel);
                if (intMinute == null) {
                    continue;
                }
                if (l > (execTime.getTime() + intMinute * 60000)) {
                    alerts.setExpireStatus(AlertCommonConstant.NUM.ONE);
                }
            }
        }
        // 判断超时提醒
        if (jsonObjectRemind != null) {
            for (Alerts alerts: alertsList) {
                Date execTime = alerts.getExecTime();
                String alertLevel = alerts.getAlertLevel();
                if (StringUtils.isEmpty(alertLevel) || execTime == null) {
                    continue;
                }
                if (AlertCommonConstant.NUM.ONE.equals(alerts.getExpireStatus())) {
                    continue;
                }
                Integer intMinute = jsonObjectRemind.get(alertLevel);
                if (intMinute == null) {
                    continue;
                }
                if (l > (execTime.getTime() + intMinute * 60000)) {
                    alerts.setExpireStatus(AlertCommonConstant.NUM.TWO);
                }
            }
        }
    }

	@Override
	public List<Map<String,Object>> selectIdcTypeAlertsByOperateStatus(Integer operateStatus,
			String startDate, String endDate) {
		List<Map<String,Object>> alertsList = alertsIndexPageDao.selectIdcTypeAlertsByOperateStatus(operateStatus
				,startDate,endDate);
		
		/*
		 * Map<String,Map<String,Object>> val = getRate(alertsList);
		 * List<Map<String,Object>> list= Lists.newArrayList(); if(null!=val) { list =
		 * new ArrayList(val.values()); }
		 */
        return alertsList;
	}
	
	@Override
	public List<Map<String,Object>> selectDeviceTypeAlertsByOperateStatus(Integer operateStatus,
			String startDate, String endDate) {
		List<Map<String,Object>> alertsList = alertsIndexPageDao.selectDeviceTypeAlertsByOperateStatus(operateStatus
				,startDate,endDate);
        return alertsList;
	}

    @Override
    public List<Map<String,Object>> selectIdcTypeAlertsByOperateStatusOrder(Integer operateStatus, String startDate, String endDate) {
        List<Map<String,Object>> alertsList = alertsIndexPageDao.selectIdcTypeAlertsByOperateStatusOrder(operateStatus
                ,startDate,endDate);
        return alertsList;
    }

    @Override
    public List<Map<String,Object>> selectDeviceTypeAlertsByOperateStatusOrder(Integer operateStatus, String startDate, String endDate) {
        List<Map<String,Object>> alertsList = alertsIndexPageDao.selectDeviceTypeAlertsByOperateStatusOrder(operateStatus
                ,startDate,endDate);
        return alertsList;
    }
	
	Map<String,Map<String,Object>> getRate(List<Map<String,Object>> alertsList){
		int sum = 0;
		DecimalFormat decimalFormat=new DecimalFormat("#.##");
		Map<String,Map<String,Object>> Valmap = Maps.newHashMap();
		for(Map<String,Object> map:alertsList) {
			Map<String,Object> val = Maps.newHashMap();
			int count = Integer.parseInt(map.get("count").toString());
			String col = map.get("col")==null?"":map.get("col").toString();
			if(StringUtils.isBlank(col)) {
				continue;
			}
			sum+=count;
			val.put("name", col);
			val.put("count", count);
			Valmap.put(col, val);
		}
		//Valmap.put("total", sum);
		for(Entry<String,Map<String,Object>> entry:Valmap.entrySet()) {
			Map<String,Object> val = entry.getValue();
			int count = (int)val.get("count");
			float rate = ((float)count/sum)*100;
			val.put("rate", decimalFormat.format(rate));
		}
		
		return Valmap;
	}

    /**
     * 告警处理时长-资源池分布
     * @param alertLevel
     * @return
     */
    public List<Map<String,Object>> alertIdcDoHours (String alertLevel) {
        List<Map<String, Object>> mapList = alertsIndexPageDao.alertIdcDoHours(alertLevel);
        for (Map<String, Object> map: mapList) {
            Double hours = MapUtils.getDouble(map, "hours");
            if (hours <= 0) {
                map.put("hours", 0.1);
            }
        }
        return mapList;
    }

}
