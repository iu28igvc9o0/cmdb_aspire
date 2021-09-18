package com.aspire.ums.cmdb.systemManager.controller;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.IOrgManagerAPI;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
public class OrgManagerController implements IOrgManagerAPI {
    @Autowired
    private OrgManagerService orgManagerService;

    public Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                                   @RequestParam(value = "pageSize", required = false) int pageSize,
                                                   @RequestParam(value = "pid", required = false) String pid,
                                                   @RequestParam(value = "orgName", required = false) String orgName,
                                                   @RequestParam(value = "orgType", required = false) String orgType) {
        return orgManagerService.getAllOrgManagerData(pageNum, pageSize, pid, orgName, orgType);
    }

    @Override
    public List<OrgManager> getAllOrg() {
        return orgManagerService.getAllOrg();
    }

    @Override
    public List<OrgManager> getAllEipOrg() {
        return orgManagerService.getAllEipOrg();
    }

    public String insert(@RequestBody OrgManager orgManager) {
        return orgManagerService.insert(orgManager);
    }

    /**
     * 新增组织信息
     *
     * @param orgManager 组织信息对象
     * @return
     */
    @Override
    public String pureInsert(@RequestBody OrgManager orgManager) {
        return orgManagerService.pureInsert(orgManager);
    }

    public OrgManager getById(@RequestParam("id") String id) {
        return orgManagerService.getOrgManagerById(id);
    }

    public List<OrgManager> getByPid(@RequestParam("pid") String pid) {
        return orgManagerService.getOrgManagerByPid(pid);
    }

    public List<OrgManager> getParentOrg(@RequestParam(value = "id", required = false) String id) {
        return orgManagerService.getParentOrg(id);
    }

    public String update(@RequestBody OrgManager orgManager) {
        return orgManagerService.update(orgManager);
    }

    public String deleteOrg(@RequestParam("id") String id) {
        return orgManagerService.delete(id);
    }

    public List<OrgManager> loadTree() {
        return orgManagerService.loadTree();
    }
    
    public List<Map> loadTreeDepBiz() {
        return orgManagerService.loadTreeDepBiz();
    }

    @Override
    public OrgManager getDepInfo(@RequestParam("department1") String department1,
                                 @RequestParam(name = "department2",required = false) String department2) {
        return orgManagerService.getWithDepInfo(department1, department2);
    }
}
