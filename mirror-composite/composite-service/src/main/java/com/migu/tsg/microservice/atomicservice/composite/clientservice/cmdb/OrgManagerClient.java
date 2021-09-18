package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/28 09:22
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface OrgManagerClient {
    @GetMapping("/cmdb/orgManager/list")
    Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                            @RequestParam(value = "pageSize",required = false) int pageSize,
                                            @RequestParam(value = "pid",required = false) String pid,
                                            @RequestParam(value = "orgName",required = false) String orgName,
                                            @RequestParam(value = "orgType",required = false) String orgType);
    @PostMapping("/cmdb/orgManager/add")
    String insert(@RequestBody OrgManager orgManager);
    
    @GetMapping("/cmdb/orgManager/getById")
    OrgManager getById (@RequestParam("id") String id);
    
    @GetMapping("/cmdb/orgManager/getByPid")
    List<OrgManager> getByPid (@RequestParam("pid") String pid);
    
    @PostMapping("/cmdb/orgManager/update")
    String update(@RequestBody OrgManager orgManager);
    
    @DeleteMapping("/cmdb/orgManager/deleteOrg")
    String deleteOrg(@RequestParam("id") String id);
    
    @GetMapping("/cmdb/orgManager/loadTree")
    List<OrgManager> loadTree();
    
    @GetMapping("/cmdb/orgManager/loadTreeDepBiz")
    List<Map> loadTreeDepBiz();

    @GetMapping("/cmdb/orgManager/getAllOrg")
    List<OrgManager> getAllOrg();

    @GetMapping("/cmdb/orgManager/getDepInfo")
    OrgManager getDepInfo(@RequestParam("department1") String department1,
                          @RequestParam(name = "department2",required = false) String department2);
}
