package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IIndicationDataDAO {

    /**
     * 批量创建指标计算数据 每次提交1000条
     * @param indicationDataList
     */
    void batchInsert(@Param("indicationDataList") List<IndicationDataEntity> indicationDataList);

    /**
     * 获取最近14天的全国指标数据
     * @param itemList
     * @param indicationId
     * @param inCalcDate
     * @return
     */
    List<Map<String, String>> getIndicationDataForCountry(@Param("itemList") List<IndicationAllItemEntity> itemList,
                                                          @Param("indicationId") Integer indicationId,
                                                          @Param("inCalcDate") String inCalcDate);
    
    List<Map<String, String>> getIndicationDataForCountryPeriod(@Param("itemList") List<IndicationAllItemEntity> itemList,
            @Param("indicationId") Integer indicationId,
            @Param("beginDate") String beginDate,
            @Param("endDate") String endDate);

    /**
     * 获取指定日期的异常指标数据
     * @param itemList
     * @param provinceCode
     * @param calcDate
     * @return
     */
    List<Map<String, String>> getExceptionDataByIndicationIdAndProvinceCode(@Param("itemList") List<IndicationAllItemEntity> itemList,
                                                                            @Param("indicationId") Integer indicationId,
                                                                            @Param("provinceCode") String provinceCode,
                                                                            @Param("calcDate") String calcDate,
                                                                            @Param("orderName") String orderName);

    /**
     * 根据指标ID 、开始时间、结束时间获取指标计算结果
     * @param indicationId 指标ID
     * @param calcDate 计算时间
     * @return
     */
    List<IndicationDataEntity> getIndicationDataByIndicationIdAndCalcDate(@Param("indicationId") Integer indicationId,
                                                   @Param("calcDate") String calcDate);
    
    void deleteByDate( @Param("calcDate") String calcDate);
}
