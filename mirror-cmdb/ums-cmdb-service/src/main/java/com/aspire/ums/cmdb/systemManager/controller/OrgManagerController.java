package com.aspire.ums.cmdb.systemManager.controller;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.OrgManager;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.controller
 * 类名称:
 * 类描述: 组织管理
 * 创建人: PJX
 * 创建时间: 2019/5/23 15:24
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb/orgManager")
public class OrgManagerController {
    @Autowired
    private OrgManagerService orgManagerService;
    
    @GetMapping("/list")
    public Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                  @RequestParam(value = "pageSize",required = false) int pageSize,
                                                   @RequestParam(value = "pid",required = false) String pid,
                                                  @RequestParam(value = "orgName",required = false) String orgName,
                                                  @RequestParam(value = "orgType",required = false) String orgType){
        return orgManagerService.getAllOrgManagerData(pageNum,pageSize,pid,orgName,orgType);
    }
    
    @PostMapping("/add")
    public String insert(@RequestBody OrgManager orgManager){
        return orgManagerService.insert(orgManager);
    }

    @PostMapping("/eip-add")
    public String pureInsert(@RequestBody OrgManager orgManager) {
        return orgManagerService.eipInsert(orgManager);
    }
    
    @GetMapping("/getById")
    public OrgManager getById (@RequestParam("id") String id){
        return orgManagerService.getOrgManagerById(id);
    }
    
    @GetMapping("/getByPid")
    public List<OrgManager> getByPid (@RequestParam("pid") String pid){
        return orgManagerService.getOrgManagerByPid(pid);
    }
    
    @GetMapping("/getParentOrg")
    public List<OrgManager> getParentOrg(@RequestParam(value = "id",required = false) String id) {
        return orgManagerService.getParentOrg(id);
    }
    
    
    
    @PostMapping("/update")
    public String update(@RequestBody OrgManager orgManager){
        return orgManagerService.update(orgManager);
    }
    
    @DeleteMapping("/deleteOrg")
    public String deleteOrg(@RequestParam("id") String id) {
        return orgManagerService.delete(id);
    }
    
    @GetMapping("/loadTree")
    public List<OrgManager> loadTree() {
        return orgManagerService.loadTree();
    }
}
