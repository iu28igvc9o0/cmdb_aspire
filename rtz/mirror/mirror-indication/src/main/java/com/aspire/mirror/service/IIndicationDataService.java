package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationDataEntity;
import com.aspire.mirror.entity.IndicationEntity;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/10/24 软探针异常指标监控系统
 * com.aspire.mirror.service.IIndicationDataService
 */
public interface IIndicationDataService {

	/**
	 * 批量创建指标计算数据 每次提交1000条
	 * 
	 * @param indicationDataList
	 */
	void batchInsert(List<IndicationDataEntity> indicationDataList);

	/**
	 * 获取最近14天的全国指标数据
	 * 
	 * @param indicationId
	 * @param prevDays
	 * @return
	 */
	List<Map<String, String>> getIndicationDataForCountry(Integer indicationId, List<String> prevDays);


	/**
	 * 根据指标ID 、开始时间、结束时间获取指标计算结果
	 * 
	 * @param indicationId 指标ID
	 * @param calcDate    计算时间
	 * @return
	 */
	List<IndicationDataEntity> getIndicationDataByIndicationIdAndCalcDate(Integer indicationId, String calcDate);

	/**
	 * 获取指定日期的异常指标数据
	 * 
	 * @param dateTime     日期
	 * @param provinceCode 省份
	 * @param indicationId 指标ID
	 * @return
	 */
	List<Map<String, String>> getExceptionDataByIndicationIdAndProvinceCode(String dateTime, String provinceCode,
			Integer indicationId, String IndicationName);

    /**
     * 指标计算方法
     * @param beginDateStr 计算开始时间
     * @param endDateStr 计算结束时间
     * @param entityList 计算的指标集合
     * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
     * @throws ParseException
     */
    void runDayCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
            throws ParseException;

	/**
	 * 小时频率指标计算方法
	 * @param beginDateStr 计算开始时间
	 * @param endDateStr 计算结束时间
	 * @param entityList 计算的指标集合
	 * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
	 * @throws ParseException
	 */
	void runHourCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
			throws ParseException;

//	/**
//	 * 实时频率指标计算方法
//	 * @param beginDateStr 计算开始时间
//	 * @param endDateStr 计算结束时间
//	 * @param entityList 计算的指标集合
//	 * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
//	 * @throws ParseException
//	 */
//	void runActualCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
//			throws ParseException;

//	/**
//	 * 分钟频率指标计算方法
//	 * @param beginDateStr 计算开始时间
//	 * @param endDateStr 计算结束时间
//	 * @param entityList 计算的指标集合
//	 * @param indicationId 计算的指标ID, 当indicationId不为空值, 只计算
//	 * @throws ParseException
//	 */
//	void runMinuteCalc(String beginDateStr, String endDateStr, List<IndicationEntity> entityList, Integer indicationId)
//			throws ParseException;

    /**
     * 获取异常数据
     * @param catalogBox
     * @param indicationCycle
	 * @param indicationFrequency
     * @param dateTime
     * @param group
     * @param provinceCode
     * @return
     */
    JSONObject getExceptionData(String indicationOwner, String catalogBox, String indicationCycle,
								String indicationFrequency, String dateTime, String group,
								String provinceCode, String indicationPosition);
    /**
     * 获取分钟级异常数据
     * @param indicationOwner 所属系统
     * @param indicationCatalog 所属分类
     * @param indicationFrequency 计算频率
     * @param indicationPosition 显示位置
     * @param calcDate 计算时间
     * @return
     */
	JSONObject getMinuteIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition,
											  String indicationFrequency, String calcDate);

	/**
	 * 获取分钟数据
	 * @param indicationId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, String>> getMinuteIndicationDataForMail(Integer indicationId, String beginDate, String endDate);
    
    void clearData(String calcDate);

	Map<String, List<JSONObject>> getRealHourAndActualExceptionDataForMail(List<IndicationEntity> indicationList, String calcDate);
}
