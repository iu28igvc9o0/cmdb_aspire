package com.aspire.mirror.alert.server.controller.businessAlert;


import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessReq;
import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessResp;
import com.aspire.mirror.alert.api.service.businessAlert.AlertConfigBusinessService;
import com.aspire.mirror.alert.server.biz.businessAlert.IAlertConfigBusinessBiz;
import com.aspire.mirror.alert.server.dao.businessAlert.po.AlertConfigBusiness;
import com.aspire.mirror.alert.server.vo.businessAlert.AlertConfigBusinessVo;
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
import java.util.List;

/**
 * @author baiwp
 * @title: AlertConfigBusinessController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertConfigBusinessController implements AlertConfigBusinessService {
    @Autowired
    private IAlertConfigBusinessBiz AlertConfigBusinessBiz;

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<AlertConfigBusinessResp> list(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "status", required = false) String status,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertConfigBusinessVo alertConfigBusiness = new AlertConfigBusinessVo();
        alertConfigBusiness.setName(name);
        alertConfigBusiness.setStatus(status);
        int begin = (pageNum - 1) * pageSize;
//        begin = begin < 0 ? 0 : begin;
        alertConfigBusiness.setBegin(begin);
        alertConfigBusiness.setPageSize(pageSize);
        log.debug("list page paramters are : {}", alertConfigBusiness.toString());
        PageResponse<AlertConfigBusiness> isolateBizPage = AlertConfigBusinessBiz.findPage(alertConfigBusiness);
        PageResponse<AlertConfigBusinessResp> page = new PageResponse<AlertConfigBusinessResp>();
        page.setCount(isolateBizPage.getCount());
        List<AlertConfigBusinessResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(isolateBizPage.getResult())) {
            for (AlertConfigBusiness AlertConfigBusiness : isolateBizPage.getResult()) {
                AlertConfigBusinessResp AlertConfigBusinessResp = new AlertConfigBusinessResp();
                BeanUtils.copyProperties(AlertConfigBusiness, AlertConfigBusinessResp);
                listAlert.add(AlertConfigBusinessResp);
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
    public AlertConfigBusinessResp detail(@PathVariable String id) {
        AlertConfigBusiness AlertConfigBusiness = AlertConfigBusinessBiz.get(id);
        AlertConfigBusinessResp AlertConfigBusinessResp = new AlertConfigBusinessResp();
        BeanUtils.copyProperties(AlertConfigBusiness, AlertConfigBusinessResp);
        return AlertConfigBusinessResp;
    }

    /**
     * 新增屏蔽规则
     *
     * @param AlertConfigBusinessReq
     * @return
     */
    public AlertConfigBusinessResp insert(@RequestBody AlertConfigBusinessReq AlertConfigBusinessReq) {
        AlertConfigBusiness AlertConfigBusiness = new AlertConfigBusiness();
        BeanUtils.copyProperties(AlertConfigBusinessReq, AlertConfigBusiness);
        Date now = new Date();
        AlertConfigBusiness.setCreatedAt(now);
        AlertConfigBusiness.setUpdatedAt(now);
        AlertConfigBusinessBiz.insert(AlertConfigBusiness);
        AlertConfigBusinessResp AlertConfigBusinessResp = new AlertConfigBusinessResp();
        BeanUtils.copyProperties(AlertConfigBusiness, AlertConfigBusinessResp);
        return AlertConfigBusinessResp;
    }

    /**
     * 修改屏蔽规则
     *
     * @param AlertConfigBusinessReq
     * @return
     */
    public void update(@RequestBody AlertConfigBusinessReq AlertConfigBusinessReq) {
        AlertConfigBusiness AlertConfigBusiness = new AlertConfigBusiness();
        BeanUtils.copyProperties(AlertConfigBusinessReq, AlertConfigBusiness);
        Date now = new Date();
        AlertConfigBusiness.setUpdatedAt(now);
        AlertConfigBusinessBiz.update(AlertConfigBusiness);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @PathVariable String operater, @RequestBody String... ids) {
        AlertConfigBusinessBiz.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@PathVariable String operater, @RequestBody String... ids) {
        AlertConfigBusinessBiz.delete(operater, ids);
    }
}
