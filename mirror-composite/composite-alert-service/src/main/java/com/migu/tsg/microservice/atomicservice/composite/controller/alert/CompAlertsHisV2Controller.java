package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompQueryParams;
import com.aspire.mirror.composite.service.alert.ICompAlertsHisV2Service;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsHisV2ServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.alert.v2.alert
 * @Author: baiwenping
 * @CreateTime: 2020-03-12 21:04
 * @Description: ${Description}
 */
@RestController
public class CompAlertsHisV2Controller implements ICompAlertsHisV2Service {
    @Autowired
    private AlertsHisV2ServiceClient alertsHisV2ServiceClient;
    /**
     * 告警查询
     *
     * @param compQueryParams
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public PageResponse<Map<String, Object>> query(@RequestBody CompQueryParams compQueryParams) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return alertsHisV2ServiceClient.query(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 导出历史告警列表数据
     *
     * @param compQueryParams
     */
    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public Map<String, Object> export(@RequestBody CompQueryParams compQueryParams) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return alertsHisV2ServiceClient.export(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 查询详情
     *
     * @param alertId
     * @return
     */
    @Override
    public Map<String, Object> detail(@PathVariable(name = "alert_id") String alertId) {
        return alertsHisV2ServiceClient.detail(alertId);
    }
}
