package com.aspire.mirror.alert.server.dao.derive;

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
public interface AlertDeriveAlertsHisV2Mapper {
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
}
