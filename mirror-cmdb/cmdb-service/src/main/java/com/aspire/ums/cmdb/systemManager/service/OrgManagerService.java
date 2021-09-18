package com.aspire.ums.cmdb.systemManager.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.service
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 15:25
 * 版本: v1.0
 */
public interface OrgManagerService {
    Result<OrgManager> getAllOrgManagerData(int pageNum,
                                            int pageSize,
                                            String pid,
                                            String orgName,
                                            String orgType);

    String insert(OrgManager bizSystem);

    String pureInsert(OrgManager bizSystem);

    OrgManager getOrgManagerById(String id);

    String update(OrgManager orgManager);

    String delete(String id);
    
    String delete(OrgManager orgManager);

    List<OrgManager> getParentOrg(String id);

    List<OrgManager> getOrgManagerByPid(String pid);

    List<OrgManager> loadTree();
    
    List<Map> loadTreeDepBiz();

    List<OrgManager> getAllOrg();

    List<OrgManager> getAllEipOrg();

    List<OrgManager> getOrgManagerData(String pid, String orgName, String orgType);

    OrgManager getWithDepInfo(String dep1,String dep2);
}
