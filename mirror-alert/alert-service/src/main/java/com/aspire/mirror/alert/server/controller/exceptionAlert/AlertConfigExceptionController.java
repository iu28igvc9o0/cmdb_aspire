package com.aspire.mirror.alert.server.controller.exceptionAlert;

import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionReq;
import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionResp;
import com.aspire.mirror.alert.api.service.exceptionAlert.AlertConfigExceptionService;
import com.aspire.mirror.alert.server.biz.exceptionAlert.IAlertConfigExceptionBiz;
import com.aspire.mirror.alert.server.dao.exceptionAlert.po.AlertConfigException;
import com.aspire.mirror.alert.server.vo.exceptionAlert.AlertConfigExceptionVo;
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
 * @title: AlertConfigExceptionController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertConfigExceptionController implements AlertConfigExceptionService {
    @Autowired
    private IAlertConfigExceptionBiz AlertConfigExceptionBiz;

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<AlertConfigExceptionResp> list(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "status", required = false) String status,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertConfigExceptionVo AlertConfigExceptionVo = new AlertConfigExceptionVo();
        AlertConfigExceptionVo.setName(name);
        AlertConfigExceptionVo.setStatus(status);
        int begin = (pageNum - 1) * pageSize;
//        begin = begin < 0 ? 0 : begin;
        AlertConfigExceptionVo.setBegin(begin);
        AlertConfigExceptionVo.setPageSize(pageSize);
        log.debug("list page paramters are : {}", AlertConfigExceptionVo.toString());
        PageResponse<AlertConfigException> isolateBizPage = AlertConfigExceptionBiz.findPage(AlertConfigExceptionVo);
        PageResponse<AlertConfigExceptionResp> page = new PageResponse<AlertConfigExceptionResp>();
        page.setCount(isolateBizPage.getCount());
        List<AlertConfigExceptionResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(isolateBizPage.getResult())) {
            for (AlertConfigException AlertConfigException : isolateBizPage.getResult()) {
                AlertConfigExceptionResp AlertConfigExceptionResp = new AlertConfigExceptionResp();
                BeanUtils.copyProperties(AlertConfigException, AlertConfigExceptionResp);
                listAlert.add(AlertConfigExceptionResp);
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
    public AlertConfigExceptionResp detail(@PathVariable String id) {
        AlertConfigException AlertConfigException = AlertConfigExceptionBiz.get(id);
        AlertConfigExceptionResp AlertConfigExceptionResp = new AlertConfigExceptionResp();
        BeanUtils.copyProperties(AlertConfigException, AlertConfigExceptionResp);
        return AlertConfigExceptionResp;
    }

    /**
     * 新增屏蔽规则
     *
     * @param AlertConfigExceptionReq
     * @return
     */
    public AlertConfigExceptionResp insert(@RequestBody AlertConfigExceptionReq AlertConfigExceptionReq) {
        AlertConfigException AlertConfigException = new AlertConfigException();
        BeanUtils.copyProperties(AlertConfigExceptionReq, AlertConfigException);
        Date now = new Date();
        AlertConfigException.setCreatedAt(now);
        AlertConfigException.setUpdatedAt(now);
        AlertConfigExceptionBiz.insert(AlertConfigException);
        AlertConfigExceptionResp AlertConfigExceptionResp = new AlertConfigExceptionResp();
        BeanUtils.copyProperties(AlertConfigException, AlertConfigExceptionResp);
        return AlertConfigExceptionResp;
    }

    /**
     * 修改屏蔽规则
     *
     * @param AlertConfigExceptionReq
     * @return
     */
    public void update(@RequestBody AlertConfigExceptionReq AlertConfigExceptionReq) {
        AlertConfigException AlertConfigException = new AlertConfigException();
        BeanUtils.copyProperties(AlertConfigExceptionReq, AlertConfigException);
        Date now = new Date();
        AlertConfigException.setUpdatedAt(now);
        AlertConfigExceptionBiz.update(AlertConfigException);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @PathVariable String operater, @RequestBody String... ids) {
        AlertConfigExceptionBiz.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@PathVariable String operater, @RequestBody String... ids) {
        AlertConfigExceptionBiz.delete(operater, ids);
    }
}
