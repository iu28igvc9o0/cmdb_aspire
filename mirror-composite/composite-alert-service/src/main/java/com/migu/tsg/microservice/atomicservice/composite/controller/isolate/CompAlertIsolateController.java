package com.migu.tsg.microservice.atomicservice.composite.controller.isolate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterOptionDTO;
import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateReq;
import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.isolate.CompAlertIsolateReq;
import com.aspire.mirror.composite.payload.isolate.CompAlertIsolateResp;
import com.aspire.mirror.composite.service.isolate.ICompAlertIsolateService;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.filter.AlertsFilterSceneServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.isolate.AlertIsolateServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.UserHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author baiwp
 * @title: AlertIsolateController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class CompAlertIsolateController implements ICompAlertIsolateService {
    @Autowired
    private AlertIsolateServiceClient alertIsolateServiceClient;
    @Autowired
    private AlertsFilterSceneServiceClient alertsFilterSceneService;
    @Autowired
    private UserHelper userHelper;
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
    public PageResponse<CompAlertIsolateResp> list(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "status", required = false) String status,
                                                   @RequestParam(value = "effectiveDateFrom", required = false) String effectiveDateFrom,
                                                   @RequestParam(value = "effectiveDateTo", required = false) String effectiveDateTo,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertIsolateResp> list = alertIsolateServiceClient.list(name, status, effectiveDateFrom, effectiveDateTo, pageNum, pageSize);
        List<CompAlertIsolateResp> compAlertIsolateRespList = PayloadParseUtil.jacksonBaseParse(CompAlertIsolateResp.class, list.getResult());
        PageResponse<CompAlertIsolateResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertIsolateRespList);
        return page;
    }

    /**
     * 查询屏蔽规则详情
     *
     * @param id
     * @return
     */
    public CompAlertIsolateResp detail(@PathVariable String id) {
        AlertIsolateResp detail = alertIsolateServiceClient.detail(id);
        CompAlertIsolateResp compAlertIsolateResp = PayloadParseUtil.jacksonBaseParse(CompAlertIsolateResp.class, detail);
        return compAlertIsolateResp;
    }

    /**
     * 新增屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    public CompAlertIsolateResp insert(@RequestBody CompAlertIsolateReq alertIsolateReq) {
        AlertIsolateReq alertIsolateReq1 = PayloadParseUtil.jacksonBaseParse(AlertIsolateReq.class, alertIsolateReq);
        if (StringUtils.isEmpty(alertIsolateReq1.getStatus())) {
            alertIsolateReq1.setStatus("0");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertIsolateReq1.setCreater(authCtx.getUser().getUsername());
        alertIsolateReq1.setEditer(authCtx.getUser().getUsername());
        AlertIsolateResp alertIsolateResp = alertIsolateServiceClient.insert(alertIsolateReq1);
        return PayloadParseUtil.jacksonBaseParse(CompAlertIsolateResp.class, alertIsolateResp);
    }

    @Override
    public Map<String, Object> insertFromBpm(@RequestBody CompAlertIsolateReq alertIsolateReq) {
        Map<String, Object> res = Maps.newHashMap();
        try {
            check(alertIsolateReq);
            AlertIsolateReq alertIsolateReq1 = PayloadParseUtil.jacksonBaseParse(AlertIsolateReq.class, alertIsolateReq);
            if (StringUtils.isEmpty(alertIsolateReq1.getStatus())) {
                alertIsolateReq1.setStatus("0");
            }
            AlertIsolateResp alertIsolateResp = alertIsolateServiceClient.insert(alertIsolateReq1);
            res.put("status", Boolean.TRUE);
            res.put("msg", alertIsolateResp.getId());
        } catch (Exception e) {
            res.put("status", Boolean.FALSE);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    private void check(CompAlertIsolateReq alertIsolateReq) {
        // 名称
        if (StringUtils.isEmpty(alertIsolateReq.getName())) {
            throw new RuntimeException("Create IsoLate Request Name is Empty");
        }
        // 起止时间
        if (null == alertIsolateReq.getEffectiveDate() || null == alertIsolateReq.getExpireDate()) {
            throw new RuntimeException("Create IsoLate Request EffectiveDate Or ExpireDate is Empty");
        }
        // 维护用户
        if (StringUtils.isEmpty(alertIsolateReq.getOperateUser())) {
            throw new RuntimeException("Create IsoLate Request OperateUser is Empty");
        }
        CompUserVo user = userHelper.findByLdapId(alertIsolateReq.getOperateUser());
        if (null == user) {
            throw new RuntimeException("Create IsoLate Request OperateUser does not exist");
        }
        // 校验过滤条件
        List<AlertFilterOptionDTO> options = alertsFilterSceneService.getAllOptions();
        List<String> codeList = options.stream().map(AlertFilterOptionDTO::getCode).collect(Collectors.toList());
        JSONObject filterOption = JSON.parseObject(alertIsolateReq.getOptionCondition());
        JSONArray optionList = filterOption.getJSONArray("data");
        for (int i = 0;i < optionList.size();i++) {
            JSONObject option = optionList.getJSONObject(i);
            JSONArray andList = option.getJSONArray("data");
            for (int j = 0;j < andList.size();j++) {
                JSONObject val = andList.getJSONObject(j);
                String filterItemName = val.getString("filterItemName");
                String operate = val.getString("operate");
                String value = val.getString("value");
                if (StringUtils.isNotEmpty(filterItemName)) {
                    if (!codeList.contains(filterItemName)) {
                        throw new RuntimeException("Create IsoLate Request Option Code is absent");
                    }
                    if (StringUtils.isEmpty(operate) || StringUtils.isEmpty(value)) {
                        throw new RuntimeException("Create IsoLate Request Option Value is absent");
                    }
                }
            }
        }
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    public void update(@RequestBody CompAlertIsolateReq alertIsolateReq) {
        AlertIsolateReq alertIsolateReq1 = PayloadParseUtil.jacksonBaseParse(AlertIsolateReq.class, alertIsolateReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        alertIsolateReq1.setEditer(authCtx.getUser().getUsername());
        alertIsolateServiceClient.update(alertIsolateReq1);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable(value = "status") String status,
                       @RequestParam(value = "operater",required = false) String operater,
                       @RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operateUser = StringUtils.isEmpty(operater) ? authCtx.getUser().getUsername() : operater;
        alertIsolateServiceClient.status(status, operateUser, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@RequestBody String... ids) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String operater = authCtx.getUser().getUsername();
        alertIsolateServiceClient.delete(operater, ids);
    }
}
