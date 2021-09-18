package com.aspire.mirror.alert.server.controller.primarySecondary;

import com.aspire.mirror.alert.api.dto.primarySecondary.AlertPrimarySecondaryReq;
import com.aspire.mirror.alert.api.dto.primarySecondary.AlertPrimarySecondaryResp;
import com.aspire.mirror.alert.api.service.primarySecondary.AlertPrimarySecondaryService;
import com.aspire.mirror.alert.server.biz.primarySecondary.IAlertPrimarySecondaryBiz;
import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondary;
import com.aspire.mirror.alert.server.vo.primarySecondary.AlertPrimarySecondaryVo;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
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
public class AlertPrimarySecondaryController implements AlertPrimarySecondaryService {
    @Autowired
    private IAlertPrimarySecondaryBiz alertPrimarySecondaryBiz;

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<AlertPrimarySecondaryResp> list(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertPrimarySecondaryVo alertPrimarySecondaryVo = new AlertPrimarySecondaryVo();
        alertPrimarySecondaryVo.setName(name);
        alertPrimarySecondaryVo.setStatus(status);
        int begin = (pageNum - 1) * pageSize;
//        begin = begin < 0 ? 0 : begin;
        alertPrimarySecondaryVo.setBegin(begin);
        alertPrimarySecondaryVo.setPageSize(pageSize);
        log.debug("list page paramters are : {}", alertPrimarySecondaryVo.toString());
        PageResponse<AlertPrimarySecondary> PrimarySecondaryBizPage = alertPrimarySecondaryBiz.findPage(alertPrimarySecondaryVo);
        PageResponse<AlertPrimarySecondaryResp> page = new PageResponse<>();
        page.setCount(PrimarySecondaryBizPage.getCount());
        List<AlertPrimarySecondaryResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(PrimarySecondaryBizPage.getResult())) {
            for (AlertPrimarySecondary alertPrimarySecondary : PrimarySecondaryBizPage.getResult()) {
                AlertPrimarySecondaryResp alertPrimarySecondaryResp = new AlertPrimarySecondaryResp();
                BeanUtils.copyProperties(alertPrimarySecondary, alertPrimarySecondaryResp);
                listAlert.add(alertPrimarySecondaryResp);
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
    public AlertPrimarySecondaryResp detail(@PathVariable String id) {
        AlertPrimarySecondary alertPrimarySecondary = alertPrimarySecondaryBiz.get(id);
        AlertPrimarySecondaryResp alertPrimarySecondaryResp = new AlertPrimarySecondaryResp();
        BeanUtils.copyProperties(alertPrimarySecondary, alertPrimarySecondaryResp);
        return alertPrimarySecondaryResp;
    }

    /**
     * 校验名称是否可用
     *
     * @param name
     * @return
     */
    public Map<String, Object> checkName(@PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", alertPrimarySecondaryBiz.checkName(name));
        return map;
    }

    /**
     * 新增屏蔽规则
     *
     * @param alertPrimarySecondaryReq
     * @return
     */
    public AlertPrimarySecondaryResp insert(@RequestBody AlertPrimarySecondaryReq alertPrimarySecondaryReq) {
        AlertPrimarySecondary alertPrimarySecondary = new AlertPrimarySecondary();
        BeanUtils.copyProperties(alertPrimarySecondaryReq, alertPrimarySecondary);
        Date now = new Date();
        alertPrimarySecondary.setCreatedAt(now);
        alertPrimarySecondary.setUpdatedAt(now);
        alertPrimarySecondaryBiz.insert(alertPrimarySecondary);
        AlertPrimarySecondaryResp alertPrimarySecondaryResp = new AlertPrimarySecondaryResp();
        BeanUtils.copyProperties(alertPrimarySecondary, alertPrimarySecondaryResp);
        return alertPrimarySecondaryResp;
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertPrimarySecondaryReq
     * @return
     */
    public void update(@RequestBody AlertPrimarySecondaryReq alertPrimarySecondaryReq) {
        AlertPrimarySecondary alertPrimarySecondary = new AlertPrimarySecondary();
        BeanUtils.copyProperties(alertPrimarySecondaryReq, alertPrimarySecondary);
        Date now = new Date();
        alertPrimarySecondary.setUpdatedAt(now);
        alertPrimarySecondaryBiz.update(alertPrimarySecondary);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @PathVariable String operater, @RequestBody String... ids) {
        alertPrimarySecondaryBiz.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@PathVariable String operater, @RequestBody String... ids) {
        alertPrimarySecondaryBiz.delete(operater, ids);
    }

    @Override
    public void copyDerive(@RequestParam("id") String id) {
        alertPrimarySecondaryBiz.copyDerive(id);
    }
}
