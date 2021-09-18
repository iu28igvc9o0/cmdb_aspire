package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.bpm;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.cmdb.restful.bpm.IBPMRestfulAPI;
import com.aspire.ums.cmdb.common.Result;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.bpm.IBPMRestfulClient;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BpmRestfulController implements IBPMRestfulAPI {

    @Autowired
    private IBPMRestfulClient ibpmRestfulClient;

    @Override
    public Map<String, Object> resourceRequestProcess(@RequestBody Map<String, Object> resourceInfo) {
        log.info("Request BpmRestfulController.resourceRequestProcess request body -> {}", resourceInfo);
        return ibpmRestfulClient.resourceRequestProcess(resourceInfo);
    }

    @Override
    public Map<String, Object> syncOrgSystem(@RequestBody Map<String, Object> orgManagerData) {
        log.info("Request BpmRestfulController.syncOrgSystem request body -> {}", orgManagerData);
        return ibpmRestfulClient.syncOrgSystem(orgManagerData);
    }

    @Override
    public List<Map<String, Object>> listOrderReportData(@RequestParam("time") String submitMonth) {
        // return ibpmRestfulClient.listOrderReportData(submitMonth);
        return Lists.newArrayList();
    }

    /**
     * 根据系统账号获取业务系统列表
     * 1. 如果账号有绑定业务系统, 则显示绑定的业务系统列表
     * 2. 如果账号没有绑定业务系统, 则显示账号归属的部门及该部门下所有子部门的业务系统列表
     *
     * @param account 系统账号
     * @return
     */
    @Override
    public Result<Map<String, Object>> getBizSystemListByAccount(@RequestParam("account") String account,
                                            @RequestParam(value = "bizSystem", required = false) String bizSystem,
                                            @RequestParam("currentPage") int currentPage,
                                            @RequestParam("pageSize") int pageSize) {
        // return ibpmRestfulClient.getBizSystemListByAccount(account, bizSystem, currentPage, pageSize);
        return new Result<Map<String, Object>>();
    }
}
