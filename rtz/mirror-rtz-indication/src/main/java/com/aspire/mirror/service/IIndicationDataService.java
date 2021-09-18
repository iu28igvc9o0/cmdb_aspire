package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface IIndicationDataService {
    /**
     * 获取指标列表
     * @param indicationOwner
     * @param indicationPosition
     * @param indicationFrequency
     */
    JSONArray getIndicationList(String indicationOwner, String indicationCatalog, String indicationPosition,
                                String indicationFrequency) throws RuntimeException;

    /**
     * 提供给freemarker的专用方法
     * @param entityList
     * @param calcDate
     * @param filter
     * @return
     */
    Map<String, List<JSONObject>> getExceptionDataForFreemarker(List<IndicationEntity> entityList, String calcDate, Map<String, String> filter);

    /**
     * 提供给发送邮件的专用方法
     * @param entityList
     * @param calcDate
     * @param filter
     * @return
     */
    Map<String, List<JSONObject>> getExceptionDataForMail(List<IndicationEntity> entityList, String calcDate, Map<String, String> filter);

    /**
     * 获取指标列表数据
     * @param indicationOwner
     * @param indicationCatalog
     * @param indicationPosition
     * @param indicationFrequency
     * @param calcDate
     * @param filter
     * @return
     */
    JSONObject getIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                 String indicationFrequency, String calcDate, Map<String, String> filter) throws RuntimeException;

    /**
     * 获取指标列表数据
     * @param indicationOwner
     * @param indicationCatalog
     * @param indicationPosition
     * @param indicationFrequency
     * @param calcDate
     * @param filter
     * @return
     */
    JSONObject getMinuteIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                 String indicationFrequency, String calcDate, Map<String, String> filter) throws RuntimeException;
}
