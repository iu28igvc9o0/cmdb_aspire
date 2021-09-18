package com.aspire.mirror.alert.server.controller.isolate;

import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateReq;
import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateResp;
import com.aspire.mirror.alert.api.service.isolate.AlertIsolateService;
import com.aspire.mirror.alert.server.biz.isolate.IAlertIsolateBiz;
import com.aspire.mirror.alert.server.dao.isolate.po.AlertIsolate;
import com.aspire.mirror.alert.server.vo.isolate.AlertIsolateVo;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author baiwp
 * @title: AlertIsolateController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertIsolateController implements AlertIsolateService {
    @Autowired
    private IAlertIsolateBiz alertIsolateBiz;

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
    public PageResponse<AlertIsolateResp> list(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "effectiveDateFrom", required = false) String effectiveDateFrom,
                                               @RequestParam(value = "effectiveDateTo", required = false) String effectiveDateTo,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertIsolateVo alertIsolateVo = new AlertIsolateVo();
        alertIsolateVo.setName(name);
        alertIsolateVo.setStatus(status);
        alertIsolateVo.setEffectiveDateFrom(effectiveDateFrom);
        alertIsolateVo.setEffectiveDateTo(effectiveDateTo);
        int begin = (pageNum - 1) * pageSize;
//        begin = begin < 0 ? 0 : begin;
        alertIsolateVo.setBegin(begin);
        alertIsolateVo.setPageSize(pageSize);
        log.debug("list page paramters are : {}", alertIsolateVo.toString());
        PageResponse<AlertIsolate> isolateBizPage = alertIsolateBiz.findPage(alertIsolateVo);
        PageResponse<AlertIsolateResp> page = new PageResponse<AlertIsolateResp>();
        page.setCount(isolateBizPage.getCount());
        List<AlertIsolateResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(isolateBizPage.getResult())) {
            for (AlertIsolate alertIsolate : isolateBizPage.getResult()) {
                AlertIsolateResp alertIsolateResp = new AlertIsolateResp();
                BeanUtils.copyProperties(alertIsolate, alertIsolateResp);
                listAlert.add(alertIsolateResp);
            }
        }
        page.setResult(listAlert);

        return page;
    }

    /**
     * 查询屏蔽规则详情
     *
     * @param id
     * @return
     */
    public AlertIsolateResp detail(@PathVariable String id) {
        AlertIsolate alertIsolate = alertIsolateBiz.get(id);
        AlertIsolateResp alertIsolateResp = new AlertIsolateResp();
        BeanUtils.copyProperties(alertIsolate, alertIsolateResp);
        return alertIsolateResp;
    }

    /**
     * 新增屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    public AlertIsolateResp insert(@RequestBody AlertIsolateReq alertIsolateReq) {
        AlertIsolate alertIsolate = new AlertIsolate();
        BeanUtils.copyProperties(alertIsolateReq, alertIsolate);
        Date now = new Date();
        alertIsolate.setCreatedAt(now);
        alertIsolate.setUpdatedAt(now);
        alertIsolateBiz.insert(alertIsolate);
        AlertIsolateResp alertIsolateResp = new AlertIsolateResp();
        BeanUtils.copyProperties(alertIsolate, alertIsolateResp);
        return alertIsolateResp;
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    public void update(@RequestBody AlertIsolateReq alertIsolateReq) {
        AlertIsolate alertIsolate = new AlertIsolate();
        BeanUtils.copyProperties(alertIsolateReq, alertIsolate);
        Date now = new Date();
        alertIsolate.setUpdatedAt(now);
        alertIsolateBiz.update(alertIsolate);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @PathVariable String operater, @RequestBody String... ids) {
        alertIsolateBiz.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@PathVariable String operater, @RequestBody String... ids) {
        alertIsolateBiz.delete(operater, ids);
    }
}
