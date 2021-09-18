package com.migu.tsg.microservice.atomicservice.composite.controller.primarySecondary;

import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompQueryParams;
import com.aspire.mirror.composite.service.primarySecondary.ICompAlertPrimarySecondaryAlertsV2Service;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.primarySecondary.AlertPrimarySecondaryAlertsV2ServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.alert.v2
 * @Author: baiwenping
 * @CreateTime: 2020-03-03 15:13
 * @Description: ${Description}
 */
@RestController
public class CompAlertPrimarySecondaryAlertsV2Controller implements ICompAlertPrimarySecondaryAlertsV2Service {
    @Autowired
    private AlertPrimarySecondaryAlertsV2ServiceClient alertPrimarySecondaryAlertsV2ServiceClient;
    /**
     * 查询次要记录列表
     *
     * @param compQueryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> list(@RequestBody CompQueryParams compQueryParams) {
        return alertPrimarySecondaryAlertsV2ServiceClient.list(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }

    /**
     * 导出次要记录列表
     *
     * @param compQueryParams
     * @return
     */
    @Override
    public Map<String, Object> export(@RequestBody CompQueryParams compQueryParams) {
        return alertPrimarySecondaryAlertsV2ServiceClient.export(PayloadParseUtil.jacksonBaseParse(QueryParams.class, compQueryParams));
    }
}
