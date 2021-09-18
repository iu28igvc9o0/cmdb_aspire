package com.aspire.mirror.alert.server.biz.isolate;

import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertIsolateAlertsBizV2 {

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
     * 手动派单
     *
     * @param alertIds 告警ID集合
     * @param orderType
     */
    String genOrder(String namespace, String alertIds, Integer orderType);

    /**
     * 屏蔽恢复告警
     * @param alertIds
     * @return
     */
    Map<String, Object> alertRecovery(String[] alertIds);

    /**
     * 手工清除告警.  <br/>
     * <p>
     *
     * @param request
     */
    void remove(AlertsOperationRequestVo request);
}