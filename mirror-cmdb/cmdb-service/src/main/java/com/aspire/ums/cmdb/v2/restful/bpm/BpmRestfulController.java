package com.aspire.ums.cmdb.v2.restful.bpm;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.bpm.IBPMRestfulAPI;
import com.aspire.ums.cmdb.v2.restful.service.IBPMRestfulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BpmRestfulController implements IBPMRestfulAPI {

    @Autowired
    private IBPMRestfulService ibpmRestfulService;

    @Override
    public Map<String, Object> resourceRequestProcess(@RequestBody Map<String, Object> resourceInfo) {
        log.info("Request BpmRestfulController.resourceRequestProcess request body -> {}", resourceInfo);
        return ibpmRestfulService.resourceRequestProcess(resourceInfo);
    }

    @Override
    public Map<String, Object> syncOrgSystem(@RequestBody Map<String, Object> orgManagerData) {
        log.info("Request BpmRestfulController.syncOrgSystem request body -> {}", JSON.toJSONString(orgManagerData));
        return ibpmRestfulService.syncOrgSystem(orgManagerData);
    }

    /**
     * 获取工单数据-提供给BPM
     * @return 返回所有实例数据
     */
    @Override
    public List<Map<String, Object>> listOrderReportData(@RequestParam("time") String submitMonth) {
        return ibpmRestfulService.listOrderReportData(submitMonth);
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
        log.info("Request BpmRestfulController.getBizSystemListByAccount request account -> {} bizSystem -> {} currentPage -> {} pageSize -> {}",
                account, bizSystem, currentPage, pageSize);
        return ibpmRestfulService.getBizSystemListByAccount(account, bizSystem, currentPage, pageSize);
    }

}
