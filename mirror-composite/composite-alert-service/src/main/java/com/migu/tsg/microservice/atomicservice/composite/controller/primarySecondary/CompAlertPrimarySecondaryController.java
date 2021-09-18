package com.migu.tsg.microservice.atomicservice.composite.controller.primarySecondary;

import com.aspire.mirror.alert.api.dto.primarySecondary.AlertPrimarySecondaryReq;
import com.aspire.mirror.alert.api.dto.primarySecondary.AlertPrimarySecondaryResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.primarySecondary.CompAlertPrimarySecondaryReq;
import com.aspire.mirror.composite.payload.primarySecondary.CompAlertPrimarySecondaryResp;
import com.aspire.mirror.composite.service.primarySecondary.ICompAlertPrimarySecondaryService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.primarySecondary.AlertPrimarySecondaryServiceClient;
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
 * @title: AlertPrimarySecondaryController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class CompAlertPrimarySecondaryController implements ICompAlertPrimarySecondaryService {
    @Autowired
    private AlertPrimarySecondaryServiceClient alertPrimarySecondaryServiceClient;

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<CompAlertPrimarySecondaryResp> list(@RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "status", required = false) String status,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertPrimarySecondaryResp> list = alertPrimarySecondaryServiceClient.list(name, status, pageNum, pageSize);
        List<CompAlertPrimarySecondaryResp> compAlertPrimarySecondaryRespList = PayloadParseUtil.jacksonBaseParse(CompAlertPrimarySecondaryResp.class, list.getResult());
        PageResponse<CompAlertPrimarySecondaryResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertPrimarySecondaryRespList);
        return page;
    }

    /**
     * 查询屏蔽规则详情
     *
     * @param id
     * @return
     */
    public CompAlertPrimarySecondaryResp detail(@PathVariable String id) {
        AlertPrimarySecondaryResp detail = alertPrimarySecondaryServiceClient.detail(id);
        CompAlertPrimarySecondaryResp compAlertPrimarySecondaryResp = PayloadParseUtil.jacksonBaseParse(CompAlertPrimarySecondaryResp.class, detail);
        return compAlertPrimarySecondaryResp;
    }
    /**
     * 校验名称是否可用
     *
     * @param name
     * @return
     */
    public Map<String, Object> checkName(@PathVariable("name") String name) {
        return alertPrimarySecondaryServiceClient.checkName(name);
    }
    /**
     * 新增屏蔽规则
     *
     * @param alertPrimarySecondaryReq
     * @return
     */
    public CompAlertPrimarySecondaryResp insert(@RequestBody CompAlertPrimarySecondaryReq alertPrimarySecondaryReq) {

        AlertPrimarySecondaryReq alertPrimarySecondaryReq1 = PayloadParseUtil.jacksonBaseParse(AlertPrimarySecondaryReq.class, alertPrimarySecondaryReq);
        if (StringUtils.isEmpty(alertPrimarySecondaryReq1.getStatus())) {
            alertPrimarySecondaryReq1.setStatus("0");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertPrimarySecondaryReq1.setCreater(authCtx.getUser().getUsername());
        alertPrimarySecondaryReq1.setEditer(authCtx.getUser().getUsername());
        AlertPrimarySecondaryResp alertPrimarySecondaryResp = alertPrimarySecondaryServiceClient.insert(alertPrimarySecondaryReq1);
        return PayloadParseUtil.jacksonBaseParse(CompAlertPrimarySecondaryResp.class, alertPrimarySecondaryResp);
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertPrimarySecondaryReq
     * @return
     */
    public void update(@RequestBody CompAlertPrimarySecondaryReq alertPrimarySecondaryReq) {
        AlertPrimarySecondaryReq alertPrimarySecondaryReq1 = PayloadParseUtil.jacksonBaseParse(AlertPrimarySecondaryReq.class, alertPrimarySecondaryReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertPrimarySecondaryReq1.setEditer(authCtx.getUser().getUsername());
        alertPrimarySecondaryServiceClient.update(alertPrimarySecondaryReq1);
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
        alertPrimarySecondaryServiceClient.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertPrimarySecondaryServiceClient.delete(operater, ids);
    }

    @Override
    public void copyDerive(@RequestParam("id") String id) {
        alertPrimarySecondaryServiceClient.copyDerive(id);
    }
}
