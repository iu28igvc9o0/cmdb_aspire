package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.AlertsConfirmRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryField;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.alert.api.dto.model.AlertFieldDetail;
import com.aspire.mirror.alert.api.dto.notify.AlertsNotifyRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.*;
import com.aspire.mirror.composite.service.alert.ICompAlertsV2Service;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsV2ServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.model.AlertFieldServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.notify.AlertNotifyTemplateServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.alert.v2.alert
 * @Author: baiwenping
 * @CreateTime: 2020-03-12 21:03
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class CompAlertsV2Controller implements ICompAlertsV2Service {
    @Autowired
    private AlertsV2ServiceClient alertsV2ServiceClient;
    @Autowired
    private AlertNotifyTemplateServiceClient alertNotifyTemplateServiceClient;
    @Autowired
    private AlertFieldServiceClient alertFieldServiceClient;
    /**
     * 查询列表
     *
     * @param compQueryParams
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public PageResponse<Map<String, Object>> query(@RequestBody CompQueryParams compQueryParams) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return alertsV2ServiceClient.query(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 导出警报列表数据
     *
     * @param compQueryParams
     */
    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public Map<String, Object> export(@RequestBody CompQueryParams compQueryParams) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return alertsV2ServiceClient.export(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 查询详情
     *
     * @param alertId
     * @return
     */
    @Override
    public Map<String, Object> detail(@PathVariable(name = "alert_id") String alertId) {
        return alertsV2ServiceClient.detail(alertId);
    }

    @Override
    public List<CompAlertConfigDict> getMonitObjectList() {
        return PayloadParseUtil.jacksonBaseParse(CompAlertConfigDict.class, alertsV2ServiceClient.getMonitObjectList());
    }

    /**
     * 告警转派
     *
     * @param compAlertsTransferRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @Override
    public ResponseEntity<String> alertTransfer(@RequestBody CompAlertsTransferRequest compAlertsTransferRequest) {
        log.info("method[alertTransfer] alert_ids is {}", compAlertsTransferRequest.getAlertIds());
        log.info("method[alertTransfer] user_names is {}", compAlertsTransferRequest.getUserNames());

        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertsTransferRequest.setUsername(authCtx.getUser().getUsername());

        AlertsTransferRequest alertsOperationRequest = jacksonBaseParse(AlertsTransferRequest.class, compAlertsTransferRequest);

        try {

            alertsV2ServiceClient.alertTransfer(alertsOperationRequest);

        } catch (Exception e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警确认
     *
     * @param compAlertsConfirmRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @Override
    public ResponseEntity<String> alertConfirm(@RequestBody CompAlertsConfirmRequest compAlertsConfirmRequest) {
        log.info("method[alertConfirm] alert_ids is {}", compAlertsConfirmRequest.getAlertIds());

        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertsConfirmRequest.setUsername(authCtx.getUser().getUsername());

        AlertsConfirmRequest alertsOperationRequest = jacksonBaseParse(AlertsConfirmRequest.class, compAlertsConfirmRequest);

        alertsV2ServiceClient.alertConfirm(alertsOperationRequest);

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> alertObserve(@RequestBody Map<String,Object> request) {
        log.info("method[alertObserve] alert_ids is {}", request.get("alertIds"));
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.put("username",authCtx.getUser().getUsername());
        alertsV2ServiceClient.alertObserve(request);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警工单
     *
     * @param compAlertsOrderRequest 告警ID集合
     * @return 处理结果
     */
    @Override
    public ResponseEntity<String> alertOrder(@RequestBody CompAlertsOrderRequest compAlertsOrderRequest) {
        log.info("method[alertOrder] alert_ids is {}",  compAlertsOrderRequest.getAlertIds());
        log.info("0000method[alertOrder] param is {}",compAlertsOrderRequest);

        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertsOrderRequest.setUsername(authCtx.getUser().getUsername());
        AlertsOrderRequest alertsOperationRequest = jacksonBaseParse(AlertsOrderRequest.class, compAlertsOrderRequest);
        log.info("111111method[alertOrder] param is {}",alertsOperationRequest);
        return alertsV2ServiceClient.alertOrder(alertsOperationRequest);
    }

    /**
     * 清除告警
     *
     * @param compAlertsClearRequest 告警ID集合
     * @return 处理结果
     */
    @Override
    public ResponseEntity<String> alertRemove(@RequestBody CompAlertsClearRequest compAlertsClearRequest) {
        log.info("method[deleteByPrimayKeyArrays] alert_ids is {}", compAlertsClearRequest.getAlertIds());

        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertsClearRequest.setUsername(authCtx.getUser().getUsername());

        AlertsClearRequest alertsOperationRequest = jacksonBaseParse(AlertsClearRequest.class, compAlertsClearRequest);

        alertsV2ServiceClient.alertRemove(alertsOperationRequest);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警已通知功能
     *
     * @param request 告警ID集合
     * @return 处理结果
     */
    @Override
    public ResponseEntity<String> notifyStatus(@RequestBody Map<String,String> request) {
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        String username = authCtx.getUser().getUsername();
        return alertsV2ServiceClient.notifyStatus("1", request);
    }

    /**
     * 邮件通知
     *
     * @return
     */
    @Override
    @ResAction(action = "notify", resType = "alert")
    public ResponseEntity<String> emailNotify(@RequestBody CompAlertsNotifyRequest compAlertsNotifyRequest) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        AlertsNotifyRequest alertsNotifyRequest = jacksonBaseParse(AlertsNotifyRequest.class, compAlertsNotifyRequest);
        alertsNotifyRequest.setUsername(authCtx.getUser().getUsername());
        return alertsV2ServiceClient.alertemailNotify(alertsNotifyRequest);
    }

    //短信通知
    @Override
    @ResAction(action = "notify", resType = "alert")
    public ResponseEntity<String> smsNotify(@RequestBody CompAlertsNotifyRequest compAlertsNotifyRequest) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        AlertsNotifyRequest alertsNotifyRequest = jacksonBaseParse(AlertsNotifyRequest.class, compAlertsNotifyRequest);
        alertsNotifyRequest.setUsername(authCtx.getUser().getUsername());
        return alertsV2ServiceClient.recordSMSNotify(alertsNotifyRequest);
    }

    /**
     * 校验工单状态，返回告警未关闭的工单号
     * @param params
     * @return
     */
    public Map<String, Object> checkOrderStatus(@RequestBody Map<String, List<String>> params) {
        return alertsV2ServiceClient.checkOrderStatus(params);
    }

    /**
     * 根据级别统计告警所属设备数
     *
     * @param params
     * @return
     */
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public List<Map<String, Object>> summaryDeviceAlertsByLevel(@RequestBody Map<String, Object> params) {
        List<AlertFieldDetail> fieldList = alertFieldServiceClient.getModelFromRedis("alert_alerts", null);
        QueryParams queryParams = new QueryParams();
        List<QueryField> list = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = MapUtils.getString(params, key);
            for (AlertFieldDetail alertFieldDetail : fieldList) {
                String ciCode = alertFieldDetail.getCiCode();
                if (StringUtils.isNotEmpty(ciCode) && ciCode.startsWith(key)) {
                    QueryField queryField = new QueryField();
                    queryField.setFieldName(alertFieldDetail.getFieldCode());
                    queryField.setFieldType("and");
                    queryField.setFieldValue(value);
                    list.add(queryField);
                    break;
                }
                if (key.equalsIgnoreCase(alertFieldDetail.getFieldCode())) {
                    QueryField queryField = new QueryField();
                    queryField.setFieldName(alertFieldDetail.getFieldCode());
                    queryField.setFieldType("and");
                    queryField.setFieldValue(value);
                    list.add(queryField);
                    break;
                }
            }
        }
        queryParams.setList(list);
        return alertsV2ServiceClient.summaryDeviceAlertsByLevel(queryParams);
    }
}
