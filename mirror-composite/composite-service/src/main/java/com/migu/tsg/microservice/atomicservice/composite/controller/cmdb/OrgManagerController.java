package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.IOrgManagerService;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.OrgManagerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/28 09:27
 * 版本: v1.0
 */
@RestController
@Slf4j
public class OrgManagerController implements IOrgManagerService {
    
    @Autowired
    private OrgManagerClient orgManagerClient;
    
    @Override
    public Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                   @RequestParam(value = "pageSize",required = false) int pageSize,
                                                   @RequestParam(value = "pid",required = false) String pid,
                                                   @RequestParam(value = "orgName",required = false) String orgName,
                                                   @RequestParam(value = "orgType",required = false) String orgType) {
        return orgManagerClient.getAllOrgManagerData(pageNum,pageSize,pid,orgName,orgType);
    }
    
    @Override
    public String insert(@RequestBody OrgManager orgManager) {
        return orgManagerClient.insert(orgManager);
    }
    
    @Override
    public OrgManager getById(@RequestParam("id") String id) {
        return orgManagerClient.getById(id);
    }
    
    @Override
    public List<OrgManager> getByPid(@RequestParam("pid") String pid) {
        return orgManagerClient.getByPid(pid);
    }
    
    @Override
    public String update(@RequestBody OrgManager orgManager) {
        return orgManagerClient.update(orgManager);
    }
    
    @Override
    public String deleteOrg(@RequestParam("id") String id) {
        return orgManagerClient.deleteOrg(id);
    }
    
    @Override
    public List<OrgManager> loadTree() {
        return orgManagerClient.loadTree();
    }
    
    @Override
    public List<Map> loadTreeDepBiz() {
        List<Map> rs = orgManagerClient.loadTreeDepBiz();
        return rs;
    }

    @Override
    public List<OrgManager> getAllOrg() {
        return orgManagerClient.getAllOrg();
    }

    @Override
    public OrgManager getDepInfo(@RequestParam("department1") String department1,
                                 @RequestParam(name = "department2",required = false) String department2) {
        return orgManagerClient.getDepInfo(department1, department2);
    }
}
