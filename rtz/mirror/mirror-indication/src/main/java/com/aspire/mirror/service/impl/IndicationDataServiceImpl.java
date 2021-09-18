package com.aspire.mirror.service.impl;

import com.aspire.common.FactoryUtils;
import com.aspire.mirror.dao.IIndicationDataDAO;
import com.aspire.mirror.dao.IIndicationItemDAO;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationDataEntity;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.ProcessEntity;
import com.aspire.mirror.indication.day.AbstractDayIndicationFactory;
import com.aspire.mirror.indication.hour.AbstractHourIndicationFactory;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.service.IProcessService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationDataServiceImpl
 */
@Service
@Slf4j
public class IndicationDataServiceImpl implements IIndicationDataService {

    @Autowired
    private IIndicationItemDAO indicationItemDAO;
    @Autowired
    private IIndicationDataDAO indicationDataDAO;
    @Autowired
    public IProcessService processService;
    @Autowired
	private IIndicationService indicationService;

    @Override
    public void batchInsert(List<IndicationDataEntity> indicationDataList) {
        indicationDataDAO.batchInsert(indicationDataList);
    }

    @Override
    public List<Map<String, String>> getIndicationDataForCountry(Integer indicationId, List<String> prevDays) {
        List<IndicationAllItemEntity> itemList = indicationItemDAO.getIndicationAllItemsByIndicationId(indicationId);
        if(itemList.size()==0) {
        	return new ArrayList<Map<String, String>>();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String prevDay : prevDays) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(",");
            }
            stringBuilder.append("'").append(prevDay).append("'");
        }
        return indicationDataDAO.getIndicationDataForCountry(itemList, indicationId, stringBuilder.toString());
    }

    @Override
    public List<Map<String, String>> getExceptionDataByIndicationIdAndProvinceCode(String dateTime, String provinceCode,
                                                                                   Integer indicationId, String indicationName) {
        List<IndicationAllItemEntity> itemList = indicationItemDAO.getIndicationAllItemsByIndicationId(indicationId);
        if(itemList.size()==0) {
        	return new ArrayList<Map<String, String>>();
        }
        String orderName = itemList.get(0).getIndicationItemName();
        for(IndicationAllItemEntity in:itemList) {
            if (StringUtils.isEmpty(indicationName)) {
                if (in.getPrimaryItem() == 1) {
                    orderName = in.getIndicationItemName();
                    break;
                }
            } else {
                if(indicationName.contains(in.getIndicationItemName())) {
                    orderName = in.getIndicationItemName();
                    break;
                }
            }
        }
        return indicationDataDAO.getExceptionDataByIndicationIdAndProvinceCode(itemList, indicationId, provinceCode, dateTime,orderName);
    }

    /**
     * 根据指标ID 、开始时间、结束时间获取指标计算结果
     * @param indicationId 指标ID
     * @param calcDate 计算时间
     * @return
     */
    public List<IndicationDataEntity> getIndicationDataByIndicationIdAndCalcDate(Integer indicationId, String calcDate) {
        return indicationDataDAO.getIndicationDataByIndicationIdAndCalcDate(indicationId, calcDate);
    }

    /**
     * 日频率指标计算方法
     * @param beginDateStr 计算开始时间
     * @param endDateStr 计算结束时间
     * @param entityList 计算的指标集合
     * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
     * @throws ParseException
     */
    public void runDayCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
            throws ParseException {
        Date beginDate = DateUtils.parseDate(beginDateStr, new String[]{IndicationConst.DATE_PATTERN,
                IndicationConst.QUERY_MONGODB_DATE_PATTERN});
        Date endDate = DateUtils.parseDate(endDateStr, new String[]{IndicationConst.DATE_PATTERN,
                IndicationConst.QUERY_MONGODB_DATE_PATTERN});
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        String dateStr = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        String endStr = DateFormatUtils.format(endDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        log.info("Find need calc indication count : {}", entityList.size());
        while (Double.parseDouble(dateStr) <= Double.parseDouble(endStr)) {
            for (IndicationEntity indicationEntity : entityList) {
                if (indicationId != null && !indicationId.equals(indicationEntity.getIndicationId())) {
                    continue;
                }
                try {
                    log.info("Begin handler indication {}. data {} started at {}", indicationEntity.getIndicationName(), dateStr, new Date().getTime());
                    //根据指标ID, 查找对应的处理类 进行计算
                    ProcessEntity processEntity = processService.getProcessByIndicationId(indicationEntity.getIndicationId());
                    if (processEntity == null) {
                        log.error("Can't find indication_id " + indicationEntity.getIndicationId() + " process class. Please check it.");
                        continue;
                    }
                    log.info("Handler day indication {} process class [{}]", indicationEntity.getIndicationName(), processEntity.getProcessClass());
                    AbstractDayIndicationFactory factory = FactoryUtils.invokeDayFactory(processEntity.getProcessClass());
                    factory.setCalcDate(dateStr);
                    factory.setIndicationEntity(indicationEntity);
                    factory.setProcessEntity(processEntity);
                    factory.init();
                } catch (Exception e) {
                    log.error("Handler day indication {} error:{}", indicationEntity.getIndicationName(), e.getMessage(), e);
                }
            }
            calendar.add(Calendar.DATE, 1);
            dateStr = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        }
    }

    /**
     * 小时频率指标计算方法
     * @param beginDateStr 计算开始时间
     * @param endDateStr 计算结束时间
     * @param entityList 计算的指标集合
     * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
     * @throws ParseException
     */
    public void runHourCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
            throws ParseException {
        Date beginDate = DateUtils.parseDate(beginDateStr, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_0});
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        String calcDate = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
        long endTime = Long.parseLong(endDateStr.substring(0,10));
        while (Long.parseLong(calcDate) <= endTime) {
            for (IndicationEntity indicationEntity : entityList) {
                if (indicationId != null && !indicationId.equals(indicationEntity.getIndicationId())) {
                    continue;
                }
                try {
                    log.info("Begin handler indication {}. date {} started at {}", indicationEntity.getIndicationName(), calcDate, new Date().getTime());
                    //根据指标ID, 查找对应的处理类 进行计算
                    ProcessEntity processEntity = processService.getProcessByIndicationId(indicationEntity.getIndicationId());
                    if (processEntity == null) {
                        log.error("Can't find indication_id " + indicationEntity.getIndicationId() + " process class. Please check it.");
                        continue;
                    }
                    log.info("Handler hour indication {} process class [{}]", indicationEntity.getIndicationName(), processEntity.getProcessClass());
                    AbstractHourIndicationFactory factory = FactoryUtils.invokeHourFactory(processEntity.getProcessClass());
                    factory.setCalcDate(calcDate);
                    factory.setIndicationEntity(indicationEntity);
                    factory.setProcessEntity(processEntity);
                    factory.init();
                } catch (Exception e) {
                    log.error("Handler hour indication {} error:{}", indicationEntity.getIndicationName(), e.getMessage(), e);
                }
            }
            calendar.add(Calendar.DATE, 1);
            calcDate = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
        }
    }

//    /**
//     * 实时频率指标计算方法
//     * @param beginDateStr 计算开始时间
//     * @param endDateStr 计算结束时间
//     * @param entityList 计算的指标集合
//     * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
//     * @throws ParseException
//     */
//    public void runActualCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId) {
//        for (IndicationEntity indicationEntity : entityList) {
//            if (indicationId != null && !indicationId.equals(indicationEntity.getIndicationId())) {
//                continue;
//            }
//            try {
//                log.info("Begin handler indication {}. date {} started at {}", indicationEntity.getIndicationName(),
//                        endDateStr, new Date().getTime());
//                //根据指标ID, 查找对应的处理类 进行计算
//                ProcessEntity processEntity = processService.getProcessByIndicationId(indicationEntity.getIndicationId());
//                if (processEntity == null) {
//                    log.error("Can't find indication_id " + indicationEntity.getIndicationId() + " process class. Please check it.");
//                    continue;
//                }
//                log.info("Handler actual indication {} process class [{}]", indicationEntity.getIndicationName(), processEntity.getProcessClass());
//                AbstractActualIndicationFactory factory = FactoryUtils.invokeActualFactory(processEntity.getProcessClass());
//                factory.setCalcDate(endDateStr);
//                factory.setIndicationEntity(indicationEntity);
//                factory.setProcessEntity(processEntity);
//                factory.init();
//            } catch (Exception e) {
//                log.error("Handler actual indication {} error:{}", indicationEntity.getIndicationName(), e.getMessage(), e);
//            }
//        }
//    }
//
//    /**
//     * 分钟频率指标计算方法
//     * @param beginDateStr 计算开始时间
//     * @param endDateStr 计算结束时间
//     * @param entityList 计算的指标集合
//     * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
//     * @throws ParseException
//     */
//    public void runMinuteCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
//            throws ParseException {
//        Date beginDate = DateUtils.parseDate(beginDateStr, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_1});
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(beginDate);
//        String calcDate = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_1);
//        long endTime = Long.parseLong(endDateStr.substring(0,12));
//        while (Long.parseLong(calcDate) <= endTime) {
//            for (IndicationEntity indicationEntity : entityList) {
//                if (indicationId != null && !indicationId.equals(indicationEntity.getIndicationId())) {
//                    continue;
//                }
//                try {
//                    log.info("Begin handler indication {}. date {} started at {}", indicationEntity.getIndicationName(), calcDate, new Date().getTime());
//                    //根据指标ID, 查找对应的处理类 进行计算
//                    ProcessEntity processEntity = processService.getProcessByIndicationId(indicationEntity.getIndicationId());
//                    if (processEntity == null) {
//                        log.error("Can't find indication_id " + indicationEntity.getIndicationId() + " process class. Please check it.");
//                        continue;
//                    }
//                    log.info("Handler minute indication {} process class [{}]", indicationEntity.getIndicationName(), processEntity.getProcessClass());
//                    AbstractMinuteIndicationFactory factory = FactoryUtils.invokeMinuteFactory(processEntity.getProcessClass());
//                    factory.setCalcDate(calcDate);
//                    factory.setIndicationEntity(indicationEntity);
//                    factory.setProcessEntity(processEntity);
//                    factory.init();
//                } catch (Exception e) {
//                    log.error("Handler minute indication {} error:{}", indicationEntity.getIndicationName(), e.getMessage(), e);
//                }
//            }
//            calendar.add(Calendar.MINUTE, 1);
//            calcDate = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_1);
//        }
//    }

    @Override
    public JSONObject getExceptionData(String indicationOwner, String catalogBox, String indicationCycle,
                                       String indicationFrequency, String dateTime, String group, String provinceCode, String indicationPosition) {
    	List<IndicationEntity> indications = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                indicationOwner,catalogBox,indicationFrequency, group, indicationPosition);
//        String pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN;
//    	if (IndicationConst.INDICATION_FREQUENCY_HOUR.equals(indicationFrequency)) {
//            pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN_0;
//        }
//        if (IndicationConst.INDICATION_FREQUENCY_ACTUAL.equals(indicationFrequency)) {
//            pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN_0;
//        }
//        if (IndicationConst.INDICATION_FREQUENCY_MINUTE.equals(indicationFrequency)) {
//            pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN_1;
//        }
        dateTime = TimeUtil.clearDateStrPattern(dateTime);
        List<String> prevDays;
        if (IndicationConst.INDICATION_FREQUENCY_HOUR.equals(indicationFrequency)) {
            prevDays = IndicationUtils.getPrevDaysHour(dateTime, -14);
            dateTime = dateTime.substring(0, 10);
        } else if (IndicationConst.INDICATION_FREQUENCY_ACTUAL.equals(indicationFrequency)) {
            prevDays = IndicationUtils.getPrevDaysHour(dateTime, -14);
            dateTime = dateTime.substring(0, 10);
        } else if (IndicationConst.INDICATION_FREQUENCY_MINUTE.equals(indicationFrequency)) {
            prevDays = IndicationUtils.getPrevDaysMinute(dateTime, -14);
        } else {
            prevDays = IndicationUtils.getPrevDays(dateTime, IndicationConst.QUERY_MONGODB_DATE_PATTERN, -14);
        }
		JSONArray exArray = new JSONArray();
		JSONArray provinceArray = new JSONArray();
		JSONArray countryArray = new JSONArray();
		JSONObject result = new JSONObject();
		for (IndicationEntity in : indications) {
			JSONObject ob = new JSONObject();
			ob.put("indicationId", in.getIndicationId());
			ob.put("catalogBox", in.getIndicationCatalog());
			ob.put("limit", in.getLimitEntity());
			ob.put("indicationName", in.getIndicationName());
			ob.put("group", in.getIndicationGroup());
			ob.put("indicationUnit", in.getIndicationUnit());
			ob.put("indicationAlias", in.getIndicationAlias());
			
			if(in.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_PROVINCE)) {
				List<Map<String, String>> indicationExProvinceAll = getExceptionDataByIndicationIdAndProvinceCode(dateTime, provinceCode, in.getIndicationId(),
								in.getIndicationName());
				List<Map<String, String>> indicationEx = new ArrayList<Map<String, String>>();
				List<Map<String, String>> indicationProvince = new ArrayList<Map<String, String>>();
				for (Map<String, String> map : indicationExProvinceAll) {
					if (!StringUtils.isBlank(map.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))
                            && !map.get(IndicationConst.INDICATION_ITEM_NAME_PROVINCE).equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
						indicationEx.add(map);
					}
					if (!map.get(IndicationConst.INDICATION_ITEM_NAME_PROVINCE).equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
						indicationProvince.add(map);
					}
				}
				JSONObject ob1 = JSONObject.fromObject(ob);
				BeanUtils.copyProperties(ob, ob1);
				ob.put("data", indicationEx);
				ob1.put("data", indicationProvince);
				exArray.add(ob);
				provinceArray.add(ob1);
			}else {
				List<Map<String, String>> indicationCountry = getIndicationDataForCountry(in.getIndicationId(), prevDays);
				ob.put("data", indicationCountry);
				countryArray.add(ob);
			}
		}
		result.put("分省异常", exArray);
		result.put("分省全部", provinceArray);
		result.put("全国14天", countryArray);
		return result;
    }

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void clearData(String calcDate) {
        indicationDataDAO.deleteByDate(calcDate);
	}

    @Override
    public JSONObject getMinuteIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                              String indicationFrequency, String calcDate) throws RuntimeException {
        List<IndicationEntity> entityList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(indicationOwner, indicationCatalog, indicationFrequency, null, indicationPosition);
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        JSONObject returnObject = new JSONObject();
        String startDate = calcDate;
        String endDate = calcDate.substring(0, calcDate.length() - 2) + "59";
        JSONArray returnArray = new JSONArray();
        //获取指标计算结果数据
        for (IndicationEntity entity : entityList) {
            JSONObject indicationData = new JSONObject();
            indicationData.putAll(JSONObject.fromObject(entity));
            List<IndicationAllItemEntity> itemList = indicationItemDAO.getIndicationAllItemsByIndicationId(entity.getIndicationId());
            List<Map<String, String>> dataList = null;
            if(itemList.size() > 0) {
                dataList= indicationDataDAO.getIndicationDataForCountryPeriod(itemList, entity.getIndicationId(), startDate, endDate);
            }
            indicationData.put("data", dataList);
            returnArray.add(indicationData);
        }
        returnObject.put("DATA", returnArray);
        return returnObject;
    }

    public List<Map<String, String>> getMinuteIndicationDataForMail(Integer indicationId,
                                                                       String beginDate, String endDate) {
        List<IndicationAllItemEntity> itemList = indicationItemDAO.getIndicationAllItemsByIndicationId(indicationId);
        return indicationDataDAO.getIndicationDataForCountryPeriod(itemList, indicationId, beginDate, endDate);
    }

    public Map<String, List<JSONObject>> getRealHourAndActualExceptionDataForMail(List<IndicationEntity> entityList, String calcDate) {
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        Map<String, List<JSONObject>> entityMap = new LinkedHashMap<String, List<JSONObject>>();
        for (IndicationEntity entity : entityList) {
            String key = entity.getIndicationName().substring(2);
            List<JSONObject> tempList;
            if (entityMap.containsKey(key)) {
                tempList = entityMap.get(key);
            } else {
                tempList =  new ArrayList<JSONObject>();
            }
            tempList.add(JSONObject.fromObject(entity));
            entityMap.put(key, tempList);
        }
        for (Map.Entry<String, List<JSONObject>> entry : entityMap.entrySet()) {
            List<JSONObject> jsonList = entry.getValue();
            for (JSONObject entityJson : jsonList) {
                JSONArray exceptionArray = getExceptionArray(entityJson.getString("indicationId"), calcDate);
                entityJson.put("data", exceptionArray);
            }
            entityMap.put(entry.getKey(), jsonList);
        }
        return entityMap;
    }

    private JSONArray getExceptionArray(String indicationId, String calcDate) {
        List<Map<String, String>> indicationDataList = getExceptionDataByIndicationIdAndProvinceCode(calcDate, null, Integer.parseInt(indicationId), null);
        JSONArray exceptionArray = new JSONArray();
        if (indicationDataList != null && indicationDataList.size() > 0) {
            for (Map<String, String> data : indicationDataList) {
                if (StringUtils.isNotEmpty(data.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                    exceptionArray.add(data);
                }
            }
        }
        return exceptionArray;
    }
}
