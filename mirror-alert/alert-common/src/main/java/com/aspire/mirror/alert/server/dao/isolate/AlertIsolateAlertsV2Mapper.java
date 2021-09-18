package com.aspire.mirror.alert.server.dao.isolate;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Mapper
public interface AlertIsolateAlertsV2Mapper {

    /**
     * 获取所有实例
     *
     * @param example 实例信息
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listByEntity(Criteria example);


    /**
     * 新增实例
     *
     * @param map 实例数据
     * @return
     */
    void insert(@Param("map") Map<String, Object> map);

    /**
     * 修改
     *
     * @param map 修改对象
     * @return 影响数据条数
     */
    int update (@Param("map") Map<String, Object> map);

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

    void deleteById (@Param("alertId") String alertId);

    /**
     * 根据主键更新数据,仅限派单使用，其他场景请使用模型更新
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Alerts alerts);

    /**
     * 查询告警列表
     *
     * @param alertIdArrays 告警id集合
     * @return List<Alerts> 告警列表
     */
    List<Map<String, Object>> selectByPrimaryKeyArrays(String[] alertIdArrays);

    /**
     * 根据工单号查找存在的工单号
     * @param list
     * @return
     */
    List<Map<String, Object>> checkOrderStatus(@Param("list") List<String> list);
}