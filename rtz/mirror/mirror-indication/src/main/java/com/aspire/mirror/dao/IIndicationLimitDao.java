package com.aspire.mirror.dao;

import com.aspire.mirror.bean.IndicationLimitEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.dao.IIndicationLimitDao
 */
@Mapper
public interface IIndicationLimitDao {

    /**
     * 根据limit_id查询限制信息
     * @param indicationLimitId
     * @return
     */
    IndicationLimitEntity getIndicationLimitByLimitId(@Param("indicationLimitId") Integer indicationLimitId);

    /**
     * 根据指标ID 加载限制信息
     * @param indicationId
     * @return
     */
    List<IndicationLimitEntity> getIndicationLimitByIndicationId(@Param("indicationId") Integer indicationId);

    /**
     * 获取所有指标阈值
     * @return
     */
    List<IndicationLimitEntity> getAll();

    /**
     * 删除网关所有配置
     * @param indicationIds
     * @return
     */
    int deleteGateWayAll(List<IndicationLimitEntity> indicationIds);

    /**
     * 保存网关配置
     * @param meritList
     * @return
     */
    int saveGateWayMerit(List<IndicationLimitEntity> meritList);

    /**
     * 根据指标ID 省份编码 获取指标阈值
     * @param indicationId 指标ID
     * @param provinceCode 省份编码
     * @return
     */
    IndicationLimitEntity getIndicationByIndicationIdAndProvinceCode(@Param("indicationId") Integer indicationId,
                                                                     @Param("provinceCode") String provinceCode);
}
