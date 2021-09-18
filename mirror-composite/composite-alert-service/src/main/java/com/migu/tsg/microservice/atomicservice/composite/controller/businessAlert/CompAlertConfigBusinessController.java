package com.migu.tsg.microservice.atomicservice.composite.controller.businessAlert;

import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessReq;
import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.businessAlert.CompAlertConfigBusinessReq;
import com.aspire.mirror.composite.payload.businessAlert.CompAlertConfigBusinessResp;
import com.aspire.mirror.composite.service.businessAlert.ICompAlertConfigBusinessService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.businessAlert.AlertConfigBusinessServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.alert.strategy
 * @Author: baiwenping
 * @CreateTime: 2020-06-22 20:49
 * @Description: ${Description}
 */
@RestController
public class CompAlertConfigBusinessController implements ICompAlertConfigBusinessService {
    @Autowired
    private AlertConfigBusinessServiceClient alertConfigBusinessServiceClient;

    /**
     * 查询业务告警规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<CompAlertConfigBusinessResp> list(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "status", required = false) String status,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertConfigBusinessResp> list = alertConfigBusinessServiceClient.list(name, status, pageNum, pageSize);
        List<CompAlertConfigBusinessResp> compAlertConfigBusinessRespList = PayloadParseUtil.jacksonBaseParse(CompAlertConfigBusinessResp.class, list.getResult());
        PageResponse<CompAlertConfigBusinessResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertConfigBusinessRespList);
        return page;
    }

    /**
     * 查询业务告警规则详情
     *
     * @param id
     * @return
     */
    @Override
    public CompAlertConfigBusinessResp detail(@PathVariable("id") String id) {
        AlertConfigBusinessResp detail = alertConfigBusinessServiceClient.detail(id);
        CompAlertConfigBusinessResp compAlertConfigBusinessResp = PayloadParseUtil.jacksonBaseParse(CompAlertConfigBusinessResp.class, detail);
        return compAlertConfigBusinessResp;
    }

    /**
     * 新增业务告警规则
     *
     * @param compAlertConfigBusinessReq
     * @return
     */
    @Override
    public CompAlertConfigBusinessResp insert(@RequestBody CompAlertConfigBusinessReq compAlertConfigBusinessReq) {
        AlertConfigBusinessReq alertConfigBusinessReq1 = PayloadParseUtil.jacksonBaseParse(AlertConfigBusinessReq.class, compAlertConfigBusinessReq);
        if (StringUtils.isEmpty(alertConfigBusinessReq1.getStatus())) {
            alertConfigBusinessReq1.setStatus("0");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertConfigBusinessReq1.setCreater(authCtx.getUser().getUsername());
        alertConfigBusinessReq1.setEditer(authCtx.getUser().getUsername());
        AlertConfigBusinessResp AlertConfigBusinessResp = alertConfigBusinessServiceClient.insert(alertConfigBusinessReq1);
        return PayloadParseUtil.jacksonBaseParse(CompAlertConfigBusinessResp.class, AlertConfigBusinessResp);
    }

    /**
     * 修改业务告警规则
     *
     * @param compAlertConfigBusinessReq
     * @return
     */
    @Override
    public void update(@RequestBody CompAlertConfigBusinessReq compAlertConfigBusinessReq) {
        AlertConfigBusinessReq alertConfigBusinessReq1 = PayloadParseUtil.jacksonBaseParse(AlertConfigBusinessReq.class, compAlertConfigBusinessReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertConfigBusinessReq1.setEditer(authCtx.getUser().getUsername());
        alertConfigBusinessServiceClient.update(alertConfigBusinessReq1);
    }

    /**
     * 批量修改业务告警启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @Override
    public void status(@PathVariable("status") String status,
                       @RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertConfigBusinessServiceClient.status(status, operater, ids);
    }

    /**
     * 批量删除业务告警规则
     *
     * @param ids
     */
    @Override
    public void delete(@RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertConfigBusinessServiceClient.delete(operater, ids);
    }
}
