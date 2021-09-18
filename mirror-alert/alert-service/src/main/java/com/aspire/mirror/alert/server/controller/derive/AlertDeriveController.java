package com.aspire.mirror.alert.server.controller.derive;

import com.aspire.mirror.alert.api.dto.derive.AlertDeriveReq;
import com.aspire.mirror.alert.api.dto.derive.AlertDeriveResp;
import com.aspire.mirror.alert.api.service.derive.AlertDeriveService;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveBiz;
import com.aspire.mirror.alert.server.dao.derive.po.AlertDerive;
import com.aspire.mirror.alert.server.vo.derive.AlertDeriveVo;
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
 * @title: AlertDeriveController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertDeriveController implements AlertDeriveService {
    @Autowired
    private IAlertDeriveBiz alertDeriveBiz;

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
    public PageResponse<AlertDeriveResp> list(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "effectiveDateFrom", required = false) String effectiveDateFrom,
                                               @RequestParam(value = "effectiveDateTo", required = false) String effectiveDateTo,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertDeriveVo alertDeriveVo = new AlertDeriveVo();
        alertDeriveVo.setName(name);
        alertDeriveVo.setStatus(status);
        alertDeriveVo.setEffectiveDateFrom(effectiveDateFrom);
        alertDeriveVo.setEffectiveDateTo(effectiveDateTo);
        int begin = (pageNum - 1) * pageSize;
//        begin = begin < 0 ? 0 : begin;
        alertDeriveVo.setBegin(begin);
        alertDeriveVo.setPageSize(pageSize);
        log.debug("list page paramters are : {}", alertDeriveVo.toString());
        PageResponse<AlertDerive> DeriveBizPage = alertDeriveBiz.findPage(alertDeriveVo);
        PageResponse<AlertDeriveResp> page = new PageResponse<AlertDeriveResp>();
        page.setCount(DeriveBizPage.getCount());
        List<AlertDeriveResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(DeriveBizPage.getResult())) {
            for (AlertDerive alertDerive : DeriveBizPage.getResult()) {
                AlertDeriveResp alertDeriveResp = new AlertDeriveResp();
                BeanUtils.copyProperties(alertDerive, alertDeriveResp);
                listAlert.add(alertDeriveResp);
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
    public AlertDeriveResp detail(@PathVariable String id) {
        AlertDerive alertDerive = alertDeriveBiz.get(id);
        AlertDeriveResp alertDeriveResp = new AlertDeriveResp();
        BeanUtils.copyProperties(alertDerive, alertDeriveResp);
        return alertDeriveResp;
    }

    /**
     * 校验名称是否可用
     *
     * @param name
     * @return
     */
    public Map<String, Object> checkName(@PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", alertDeriveBiz.checkName(name));
        return map;
    }

    /**
     * 新增屏蔽规则
     *
     * @param alertDeriveReq
     * @return
     */
    public AlertDeriveResp insert(@RequestBody AlertDeriveReq alertDeriveReq) {
        AlertDerive alertDerive = new AlertDerive();
        BeanUtils.copyProperties(alertDeriveReq, alertDerive);
        Date now = new Date();
        alertDerive.setCreatedAt(now);
        alertDerive.setUpdatedAt(now);
        alertDeriveBiz.insert(alertDerive);
        AlertDeriveResp alertDeriveResp = new AlertDeriveResp();
        BeanUtils.copyProperties(alertDerive, alertDeriveResp);
        return alertDeriveResp;
    }

    /**
     * 修改屏蔽规则
     *
     * @param alertDeriveReq
     * @return
     */
    public void update(@RequestBody AlertDeriveReq alertDeriveReq) {
        AlertDerive alertDerive = new AlertDerive();
        BeanUtils.copyProperties(alertDeriveReq, alertDerive);
        Date now = new Date();
        alertDerive.setUpdatedAt(now);
        alertDeriveBiz.update(alertDerive);
    }

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    public void status(@PathVariable String status, @PathVariable String operater, @RequestBody String... ids) {
        alertDeriveBiz.status(status, operater, ids);
    }

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    public void delete(@PathVariable String operater, @RequestBody String... ids) {
        alertDeriveBiz.delete(operater, ids);
    }

    @Override
    public void copyDerive(@RequestParam("id") String id) {
        alertDeriveBiz.copyDerive(id);
    }
}
