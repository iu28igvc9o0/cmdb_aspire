package com.aspire.ums.cmdb.systemManager.service;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.OrgManager;

import java.util.List;

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
    String eipInsert(OrgManager bizSystem);

    String insert(OrgManager bizSystem);
    
    OrgManager getOrgManagerById(String id);
    
    String update(OrgManager orgManager);
    
    String delete(String id);
    
    List<OrgManager> getParentOrg(String id);
    
    List<OrgManager> getOrgManagerByPid(String pid);
    
    List<OrgManager> loadTree();
}
