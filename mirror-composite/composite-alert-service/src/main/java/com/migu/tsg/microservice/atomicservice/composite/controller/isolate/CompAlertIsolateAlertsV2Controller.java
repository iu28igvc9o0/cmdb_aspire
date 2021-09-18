package com.migu.tsg.microservice.atomicservice.composite.controller.isolate;

import com.aspire.mirror.alert.api.dto.AlertsClearRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsClearRequest;
import com.aspire.mirror.composite.payload.alert.CompAlertsOrderRequest;
import com.aspire.mirror.composite.payload.alert.CompQueryParams;
import com.aspire.mirror.composite.service.isolate.ICompAlertIsolateAlertsV2Service;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.isolate.AlertIsolateAlertsV2ServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.alert.v2
 * @Author: baiwenping
 * @CreateTime: 2020-03-03 15:13
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class CompAlertIsolateAlertsV2Controller implements ICompAlertIsolateAlertsV2Service {
    @Autowired
    private AlertIsolateAlertsV2ServiceClient alertIsolateAlertsV2ServiceClient;

    /**
     * 查询屏蔽记录列表
     *
     * @param compQueryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> list(@RequestBody CompQueryParams compQueryParams) {
        return alertIsolateAlertsV2ServiceClient.list(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 导出屏蔽记录列表
     *
     * @param compQueryParams
     * @return
     */
    @Override
    public Map<String, Object> export(@RequestBody CompQueryParams compQueryParams) {
        return alertIsolateAlertsV2ServiceClient.export(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 告警工单
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    public Map<String, Object> alertOrder(@RequestBody CompAlertsOrderRequest alertsOperationRequest) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertsOperationRequest.setUsername(authCtx.getUser().getUsername());
        alertsOperationRequest.setNamespace(authCtx.getUser().getNamespace());
        return alertIsolateAlertsV2ServiceClient.alertOrder(PayloadParseUtil.jacksonBaseParse(AlertsOrderRequest.class, alertsOperationRequest));
    }

    /**
     * 恢复告警
     *
     * @param params 告警ID集合
     * @return 处理结果
     */
    public Map<String, Object> alertRecovery(@RequestBody Map<String, Object> params) {
        return alertIsolateAlertsV2ServiceClient.alertRecovery(params);
    }

    /**
     * 清除告警屏蔽日志
     *
     * @param compAlertsClearRequest 告警ID集合
     * @return 处理结果
     */
    @Override
    public Map<String, Object> remove(@RequestBody CompAlertsClearRequest compAlertsClearRequest) {
        log.info("method[deleteByPrimayKeyArrays] alert_ids is {}", compAlertsClearRequest.getAlertIds());

        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertsClearRequest.setUsername(authCtx.getUser().getUsername());

        AlertsClearRequest alertsOperationRequest = PayloadParseUtil.jacksonBaseParse(AlertsClearRequest.class, compAlertsClearRequest);

        return alertIsolateAlertsV2ServiceClient.remove(alertsOperationRequest);
    }
}
