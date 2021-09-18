package com.migu.tsg.microservice.atomicservice.composite.controller.exceptionAlert;

import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionReq;
import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.exceptionAlert.CompAlertConfigExceptionReq;
import com.aspire.mirror.composite.payload.exceptionAlert.CompAlertConfigExceptionResp;
import com.aspire.mirror.composite.service.exceptionAlert.ICompAlertConfigExceptionService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.exceptionAlert.AlertConfigExceptionServiceClient;
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
public class CompAlertConfigExceptionController implements ICompAlertConfigExceptionService {
    @Autowired
    private AlertConfigExceptionServiceClient alertConfigExceptionServiceClient;

    /**
     * 查询异常信息规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<CompAlertConfigExceptionResp> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "status", required = false) String status,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                           @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertConfigExceptionResp> list = alertConfigExceptionServiceClient.list(name, status, pageNum, pageSize);
        List<CompAlertConfigExceptionResp> compAlertConfigExceptionRespList = PayloadParseUtil.jacksonBaseParse(CompAlertConfigExceptionResp.class, list.getResult());
        PageResponse<CompAlertConfigExceptionResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertConfigExceptionRespList);
        return page;
    }

    /**
     * 查询异常信息规则详情
     *
     * @param id
     * @return
     */
    @Override
    public CompAlertConfigExceptionResp detail(@PathVariable("id") String id) {
        AlertConfigExceptionResp detail = alertConfigExceptionServiceClient.detail(id);
        CompAlertConfigExceptionResp compAlertConfigExceptionResp = PayloadParseUtil.jacksonBaseParse(CompAlertConfigExceptionResp.class, detail);
        return compAlertConfigExceptionResp;
    }

    /**
     * 新增异常信息规则
     *
     * @param compAlertConfigExceptionReq
     * @return
     */
    @Override
    public CompAlertConfigExceptionResp insert(@RequestBody CompAlertConfigExceptionReq compAlertConfigExceptionReq) {
        AlertConfigExceptionReq compAlertConfigExceptionReq1 = PayloadParseUtil.jacksonBaseParse(AlertConfigExceptionReq.class, compAlertConfigExceptionReq);
        if (StringUtils.isEmpty(compAlertConfigExceptionReq1.getStatus())) {
            compAlertConfigExceptionReq1.setStatus("0");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertConfigExceptionReq1.setCreater(authCtx.getUser().getUsername());
        compAlertConfigExceptionReq1.setEditer(authCtx.getUser().getUsername());
        AlertConfigExceptionResp compAlertConfigExceptionResp = alertConfigExceptionServiceClient.insert(compAlertConfigExceptionReq1);
        return PayloadParseUtil.jacksonBaseParse(CompAlertConfigExceptionResp.class, compAlertConfigExceptionResp);
    }

    /**
     * 修改异常信息规则
     *
     * @param compAlertConfigExceptionReq
     * @return
     */
    @Override
    public void update(@RequestBody CompAlertConfigExceptionReq compAlertConfigExceptionReq) {
        AlertConfigExceptionReq compAlertConfigExceptionReq1 = PayloadParseUtil.jacksonBaseParse(AlertConfigExceptionReq.class, compAlertConfigExceptionReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        compAlertConfigExceptionReq1.setEditer(authCtx.getUser().getUsername());
        alertConfigExceptionServiceClient.update(compAlertConfigExceptionReq1);
    }

    /**
     * 批量修改异常信息启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @Override
    public void status(@PathVariable("status") String status,
                       @RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertConfigExceptionServiceClient.status(status, operater, ids);
    }

    /**
     * 批量删除异常信息规则
     *
     * @param ids
     */
    @Override
    public void delete(@RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertConfigExceptionServiceClient.delete(operater, ids);
    }
}
