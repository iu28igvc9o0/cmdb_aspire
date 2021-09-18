package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao
 * @Author: baiwenping
 * @CreateTime: 2020-02-26 15:03
 * @Description: ${Description}
 */
@Mapper
public interface AlertsHisV2Dao {
    /**
     * 新增告警历史数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    void insert (@Param("map")Map<String, Object> map);

    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 15:07 2020/3/6
     * @Param [alertIds]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> selectByIds (List<String> alertIds);

    /**
     * 获取所有实例
     *
     * @param example 实例信息
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listByEntity(@Param("example") Criteria example);

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

    /**
     * 查询详情
     * @auther baiwenping
     * @Description
     * @Date 14:58 2020/3/12
     * @Param [alertId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> detailById(String alertId);

    void updateAlertHisByOrderId(@Param("orderId") String orderId,
                                @Param("orderStatus") String orderStatus);
}
