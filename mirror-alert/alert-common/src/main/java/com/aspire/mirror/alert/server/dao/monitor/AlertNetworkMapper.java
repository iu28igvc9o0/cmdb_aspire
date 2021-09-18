package com.aspire.mirror.alert.server.dao.monitor;

import com.aspire.mirror.alert.server.dao.monitor.po.AlertNorm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:51
 */
@Mapper
public interface AlertNetworkMapper {
    /**
     *  查询设备端口指标配置表
     * @param indicatorName
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<AlertNorm> selectIndicatorsConfig(@Param("indicatorName") String indicatorName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    /**
     *  新增端口指标表
     * @param params
     * @return int
     */
    int insertIndicators(@Param("params") List<AlertNorm> params);

    /**
     *  根据用户名查询Top配置指标
     * @param name
     * @return
     */
    List<AlertNorm> selectIndicatorsByName(@Param("name") String name);

    /**
     * Top配置指标更新
     * @param params
     * @return int
     */
    int updateIndicators(@Param("params") List<AlertNorm> params);

    /**
     *  查询操作记录数数量
     * @param name
     * @return int
     */
    int selectIndicatorsByCount(@Param("name") String name);

    /**
     *  Top配置指标删除
     * @param id
     * @return int
     */
    void deleteIndicators(@Param("id") Integer id);
}
