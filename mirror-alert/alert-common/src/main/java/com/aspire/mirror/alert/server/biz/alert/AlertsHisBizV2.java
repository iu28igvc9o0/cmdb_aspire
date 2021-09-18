package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz
 * @Author: baiwenping
 * @CreateTime: 2020-02-26 14:55
 * @Description: ${Description}
 */
public interface AlertsHisBizV2 {
    /**
     * 新增告警历史数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    String insert (Map<String, Object> map);

    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> list(Criteria example);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    PageResponse<Map<String, Object>> findPage(Criteria example);

    /**
     * 查询详情
     * @auther baiwenping
     * @Description
     * @Date 14:58 2020/3/12
     * @Param [alertId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> detailById(String alertId);

    /**
     * 根据Id查询告警全部字段数据
     * @param alertIds
     * @return
     */
    List<Map<String, Object>> selectByIds (List<String> alertIds);

    void updateAlertHisByOrderId(String orderId,String orderStatus);
}
