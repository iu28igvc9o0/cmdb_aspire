package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationProvinceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.dao.IIndicationProvinceDao
 */
@Mapper
public interface IIndicationProvinceDao {

    /**
     * 根据省份名称 获取省份信息
     * @param provinceCode 省份编码
     * @return
     */
    IndicationProvinceEntity getIndicationProvinceByCode(@Param("provinceCode") String provinceCode);

    /**
     * 根据省份名称 获取省份信息
     * @param provinceName 省份名称
     * @return
     */
    IndicationProvinceEntity getIndicationProvinceByName(@Param("provinceName") String provinceName);

    /**
     * 获取所有省份
     * @return
     */
    List<IndicationProvinceEntity> getAllProvince();
}
