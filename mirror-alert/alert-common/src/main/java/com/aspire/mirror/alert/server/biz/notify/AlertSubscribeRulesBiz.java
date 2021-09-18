package com.aspire.mirror.alert.server.biz.notify;

import com.aspire.mirror.alert.server.dao.notify.po.*;
import com.aspire.mirror.alert.server.vo.notify.UpdateAlertSubscribeRulesVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

public interface AlertSubscribeRulesBiz {
    /**
     * 订阅告警管理的查询
     *
     * @return
     */
    PageResponse<AlertSubscribeRules> query(AlertSubscribeRules alertSubscribeRules);

    PageResponse<AlertSubscribeRules> queryRules(AlertSubscribeRules alertSubscribeRules);

    void deteleRules(List<String> idlist);


    void deleteSubscribeRulesManagement(List<String> idlist);

    void updateRules(List<String> idlist, String isOpen);

    List<ExportAlertSubscribeRulesManagement> export(List<String> idlist);

    List<AlertSubscribeRulesManagement> querySubscribeRules();

    void CreateSubscribeRules(CreateAlertSubscribeRules createAlertSubscribeRules);

    void UpdateSubscribeRules(UpdateAlertSubscribeRulesVo updateAlertSubscribeRulesVo);

    AlertSubscribeRulesDetailShow GetSubscribeRulesById(String id);

    List<AlertSubscribeRules> queryAlertSubscribeRules();
}
