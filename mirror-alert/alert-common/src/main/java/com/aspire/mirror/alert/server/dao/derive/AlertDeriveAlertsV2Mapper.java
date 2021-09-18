package com.aspire.mirror.alert.server.dao.derive;

import com.aspire.mirror.alert.server.vo.derive.AlertDeriveAlertsVo;
import com.aspire.mirror.alert.server.dao.derive.po.AlertDeriveAlertsV2;
import com.aspire.mirror.alert.server.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao
 * @Author: baiwenping
 * @CreateTime: 2020-02-27 15:30
 * @Description: ${Description}
 */
@Mapper
public interface AlertDeriveAlertsV2Mapper {
    /**
     * 查询
     *
     * @param alertDeriveAlertsVo 实例信息
     * @return 返回所有实例数据
     */
    List<AlertDeriveAlertsV2> select(AlertDeriveAlertsVo alertDeriveAlertsVo);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void updateByEntity(AlertDeriveAlertsV2 entity);

    /**
     * 修改
     *
     * @param map 修改对象
     * @return 影响数据条数
     */
    int update (@Param("map") Map<String, Object> map);

    List<Map<String, Object>> queryNewestByDeriveId (Map<String, Object> map);

    List<Map<String, Object>> queryDeriveAlertIdByDeriveId (Map<String, Object> map);

    List<Map<String, Object>> queryCountByDeriveId (Map<String, Object> map);

    /**
     * 新增数据
     *
     * @param map 新增对象
     * @return 影响数据条数
     */
    void insert (@Param("map")Map<String, Object> map);

    /**
     * 获取所有实例
     *
     * @param example 实例信息
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listByEntity(Criteria example);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> findPageWithResult(Criteria example);

    /**
     * 分页查询计数
     *
     * @param example
     * @return
     */
    Integer findPageWithCount(Criteria example);

    List<Map<String, Object>> selectDerivesByAlertId(@Param("alertId") String alertId);

    void deleteByAlertId(@Param("alertId") String alertId);

    /**
     * 查询衍生告警已消除的id
     * @return
     */
    List<Map<String, Object>> selectAlertHisIds();
}
