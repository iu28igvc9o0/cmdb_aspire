package com.aspire.mirror.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.mirror.util.XMLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndicationDataServiceImpl implements IIndicationDataService {

    @Autowired
    private EnvConfigProperties envConfigProperties;

    @Override
    public JSONArray getIndicationList(String indicationOwner, String indicationCatalog, String indicationPosition,
                                       String indicationFrequency) throws RuntimeException {
        List<IndicationEntity> entityList = XMLUtil.getIndicationList(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency);
        return JSONArray.fromObject(entityList);
    }

    public Map<String, List<JSONObject>> getExceptionDataForFreemarker(List<IndicationEntity> entityList, String calcDate, Map<String, String> filter) {
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        if (calcDate.length() < 14) {
            int calcDataLength = calcDate.length();
            for (int i=0; i < 14 - calcDataLength; i++) {
                calcDate += "0";
            }
        }
        Map<String, List<JSONObject>> entityMap = new HashMap<String, List<JSONObject>>();
        //先将indicationEntity进行分组
        List<String> existsIndications = new ArrayList<String>();
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

    public Map<String, List<JSONObject>> getExceptionDataForMail(List<IndicationEntity> entityList, String calcDate, Map<String, String> filter) {
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        if (calcDate.length() < 14) {
            int calcDataLength = calcDate.length();
            for (int i=0; i < 14 - calcDataLength; i++) {
                calcDate += "0";
            }
        }
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
        //获取数据
        JSONObject params = new JSONObject();
        params.accumulate("start_time", calcDate);
        params.accumulate("end_time", calcDate);
        Object result = HttpUtil.getMethod(envConfigProperties.getInterFace().getIndicationResult() + indicationId, params);
        JSONArray exceptionArray = new JSONArray();
        if (result != null) {
            JSONArray resultArray = JSONArray.fromObject(result);
            if (resultArray.size() > 0) {
                for (Object jsonObject : resultArray) {
                    JSONObject jsonData = JSONObject.fromObject(jsonObject);
                    if (StringUtils.isNotEmpty(jsonData.getString(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                        exceptionArray.add(jsonData);
                    }
                }
            }
        }
        return exceptionArray;
    }



    @Override
    public JSONObject getIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                        String indicationFrequency, String calcDate, Map<String, String> filter) throws RuntimeException {
        List<IndicationEntity> entityList = XMLUtil.getIndicationList(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency);
        JSONObject returnObject = new JSONObject();
        JSONArray provinceExceptionArray = new JSONArray();
        JSONArray provinceAllArray = new JSONArray();
        JSONArray countryArray = new JSONArray();
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_EXCEPTION, provinceExceptionArray);
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_ALL, provinceAllArray);
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_COUNTRY, countryArray);
        if (entityList.size() == 0) {
            return returnObject;
        }
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        if (calcDate.length() < 14) {
            int calcDataLength = calcDate.length();
            for (int i=0; i < 14 - calcDataLength; i++) {
                calcDate += "0";
            }
        }
        //日指标需要加载全国14天的数据
        List<String> dateList;
        if (IndicationConst.INDICATION_FREQUENCY_DAY.equals(indicationFrequency)) {
            dateList = IndicationUtils.getPrevDays(calcDate, -14);
        } else {
            dateList = new ArrayList<String>();
            dateList.add(calcDate);
        }
        //获取指标计算结果数据
        for (IndicationEntity entity : entityList) {
            JSONObject indicationData = new JSONObject();
            indicationData.put("indicationId", entity.getIndicationId());
            indicationData.put("indicationCatalog", entity.getIndicationCatalog());
            indicationData.put("limit", entity.getLimitEntity());
            indicationData.put("indicationName", entity.getIndicationName());
            indicationData.put("indicationGroup", entity.getIndicationGroup());
            indicationData.put("indicationUnit", entity.getIndicationUnit());
            String interfaceUrl = envConfigProperties.getInterFace().getIndicationResult() + entity.getIndicationId();
            //全国指标 获取数据
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
                JSONArray countryData = new JSONArray();
                for (String day : dateList) {
                    JSONObject params = new JSONObject();
                    params.accumulate("start_time", day);
                    params.accumulate("end_time", day);
                    Object result = HttpUtil.getMethod(interfaceUrl, params);
                    if (result != null) {
                        JSONArray resultArray = JSONArray.fromObject(result);
                        if (resultArray.size() > 0) {
                            countryData.add(resultArray.get(0));
                        }
                    }
                }
                indicationData.put("data", countryData);
                countryArray.add(indicationData);
            }
            //省份指标 获取指定当天的数据
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_PROVINCE)) {
                JSONObject params = new JSONObject();
                params.accumulate("start_time", calcDate);
                params.accumulate("end_time", calcDate);
                Object result = HttpUtil.getMethod(interfaceUrl, params);
                if (result == null) {
                    continue;
                }
                JSONArray resultArray = JSONArray.fromObject(result);
                JSONArray exceptionArray = new JSONArray();
                JSONArray dataArray = new JSONArray();
                for (Object data : resultArray) {
                    JSONObject jsonData = JSONObject.fromObject(data);
                    if (!isFilter(filter, jsonData)) {
                        if (StringUtils.isNotEmpty(jsonData.getString(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                            exceptionArray.add(jsonData);
                        }
                        dataArray.add(jsonData);
                    }
                }
                JSONObject exceptionData = JSONObject.fromObject(indicationData);
                exceptionData.put("data", exceptionArray);
                provinceExceptionArray.add(exceptionData);
                indicationData.put("data", dataArray);
                provinceAllArray.add(indicationData);
            }
        }
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_EXCEPTION, provinceExceptionArray);
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_ALL, provinceAllArray);
        returnObject.accumulate(IndicationConst.EXCEPTION_GROUP_PROVINCE_COUNTRY, countryArray);
        return returnObject;
    }

    @Override
    public JSONObject getMinuteIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate, Map<String, String> filter) throws RuntimeException {
        List<IndicationEntity> entityList = XMLUtil.getIndicationList(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency);
        JSONObject returnObject = new JSONObject();
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        String startDate = calcDate, endDate = calcDate;
        if (calcDate.length() == 8) {
            startDate = calcDate + "000000";
            endDate = calcDate + "235900";
        } else if (calcDate.length() == 12) {
            startDate = calcDate.substring(0,10) + "0000";
            endDate = calcDate.substring(0,10) + "5900";
        }
        JSONArray returnArray = new JSONArray();
        //获取指标计算结果数据
        for (IndicationEntity entity : entityList) {
            JSONObject indicationData = new JSONObject();
            indicationData.put("indicationId", entity.getIndicationId());
            indicationData.put("indicationCatalog", entity.getIndicationCatalog());
            indicationData.put("limit", entity.getLimitEntity());
            indicationData.put("indicationName", entity.getIndicationName());
            indicationData.put("indicationGroup", entity.getIndicationGroup());
            indicationData.put("indicationUnit", entity.getIndicationUnit());
            String interfaceUrl = envConfigProperties.getInterFace().getIndicationResult() + entity.getIndicationId();
            JSONObject params = new JSONObject();
            params.accumulate("start_time", startDate);
            params.accumulate("end_time", endDate);
            Object result = HttpUtil.getMethod(interfaceUrl, params);
            if (result == null) {
                continue;
            }
            JSONArray resultArray = JSONArray.fromObject(result);
            List<JSONObject> listObject = new ArrayList<JSONObject>();
            for (Object data : resultArray) {
                JSONObject jsonData = JSONObject.fromObject(data);
                if (!isFilter(filter, jsonData)) {
                    listObject.add(jsonData);
                }
            }
            Collections.sort(listObject, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    long time1 = o1.getLong(IndicationConst.INDICATION_ITEM_NAME_CALC_DATE);
                    long time2 = o2.getLong(IndicationConst.INDICATION_ITEM_NAME_CALC_DATE);
                    return time1 >= time2 ? -1 : 1;
                }
            });
            indicationData.put("data", listObject);
            returnArray.add(indicationData);
        }
        returnObject.put("DATA", returnArray);
        return returnObject;
    }

    private boolean isFilter(Map<String, String> filter, JSONObject jsonData) {
        //添加过滤条件
        if (filter != null) {
            for (String key : filter.keySet().toArray(new String[]{})) {
                if (key.equals("provinceCode") && StringUtils.isNotEmpty(filter.get(key))) {
                    if (!jsonData.getString(IndicationConst.INDICATION_ITEM_NAME_PROVINCE_CODE).equals(filter.get(key))) {
                        return true;
                    }
                }
                if (key.equals("period") && StringUtils.isNotEmpty(filter.get(key))) {
                    //todo
                    return true;
                }
            }
        }
        return false;
    }
}
