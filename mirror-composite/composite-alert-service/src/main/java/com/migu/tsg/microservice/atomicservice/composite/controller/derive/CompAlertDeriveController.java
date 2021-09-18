package com.migu.tsg.microservice.atomicservice.composite.controller.derive;

import com.aspire.mirror.alert.api.dto.derive.AlertDeriveReq;
import com.aspire.mirror.alert.api.dto.derive.AlertDeriveResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.derive.CompAlertDeriveReq;
import com.aspire.mirror.composite.payload.derive.CompAlertDeriveResp;
import com.aspire.mirror.composite.service.derive.ICompAlertDeriveService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.derive.AlertDeriveServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: AlertDeriveController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class CompAlertDeriveController implements ICompAlertDeriveService {
    @Autowired
    private AlertDeriveServiceClient alertDeriveServiceClient;

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param effectiveDateFrom
     * @param effectiveDateTo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<CompAlertDeriveResp> list(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "status", required = false) String status,
                                                  @RequestParam(value = "effectiveDateFrom", required = false) String effectiveDateFrom,
                                                  @RequestParam(value = "effectiveDateTo", required = false) String effectiveDateTo,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertDeriveResp> list = alertDeriveServiceClient.list(name, status, effectiveDateFrom, effectiveDateTo, pageNum, pageSize);
        List<CompAlertDeriveResp> compAlertDeriveRespList = PayloadParseUtil.jacksonBaseParse(CompAlertDeriveResp.class, list.getResult());
        PageResponse<CompAlertDeriveResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertDeriveRespList);
        return page;
    }

    /**
     * 查询屏蔽规则详情
     *
     * @param id
     * @return
     */
    public CompAlertDeriveResp detail(@PathVariable String id) {
        AlertDeriveResp detail = alertDeriveServiceClient.detail(id);
        CompAlertDeriveResp compAlertDeriveResp = PayloadParseUtil.jacksonBaseParse(CompAlertDeriveResp.class, detail);
        return compAlertDeriveResp;
    }
    /**
     * 校验名称是否可用
     *
     * @param name
     * @return
     */
    public Map<String, Object> checkName(@PathVariable("name") String name) {
        return alertDeriveServiceClient.checkName(name);
    }
    /**
     * 新增屏蔽规则
     *
     * @param alertDeriveReq
     * @return
     */
    public CompAlertDeriveResp insert(@RequestBody CompAlertDeriveReq alertDeriveReq) {

        AlertDeriveReq alertDeriveReq1 = PayloadParseUtil.jacksonBaseParse(AlertDeriveReq.class, alertDeriveReq);
        if (StringUtils.isEmpty(alertDeriveReq1.getStatus())) {
            alertDeriveReq1.setStatus("0");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertDeriveReq1.setCreater(authCtx.getUser().getUsername());
        alertDeriveReq1.setEditer(authCtx.getUser().getUsername());
        AlertDeriveResp alertDeriveResp = alertDeriveServiceClient.insert(alertDeriveReq1);
        return PayloadParseUtil.jacksonBaseParse(CompAlertDeriveResp.class, alertDeriveResp);
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertDeriveReq
     * @return
     */
    public void update(@RequestBody CompAlertDeriveReq alertDeriveReq) {
        AlertDeriveReq alertDeriveReq1 = PayloadParseUtil.jacksonBaseParse(AlertDeriveReq.class, alertDeriveReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertDeriveReq1.setEditer(authCtx.getUser().getUsername());
        alertDeriveServiceClient.update(alertDeriveReq1);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertDeriveServiceClient.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertDeriveServiceClient.delete(operater, ids);
    }

    @Override
    public void copyDerive(@RequestParam("id") String id) {
        alertDeriveServiceClient.copyDerive(id);
    }
}
