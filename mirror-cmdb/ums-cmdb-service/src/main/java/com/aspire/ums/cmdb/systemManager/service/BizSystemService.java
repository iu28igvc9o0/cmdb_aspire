package com.aspire.ums.cmdb.systemManager.service;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.BizSystem;
import com.aspire.ums.cmdb.systemManager.entity.Concat;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.service
 * 类名称: BizSystemService
 * 类描述: 业务系统管理
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:25
 * 版本: v1.0
 */
public interface BizSystemService {
    public Result<BizSystem> getAllBizSystemData(int pageNum,
                                                 int pageSize,
                                                 String pid,
                                                 String bizName,
                                                 String isdisable);
    
    String insert(BizSystem bizSystem);
    
    BizSystem getBizSystemById(String id);
    
    String update(BizSystem bizSystem);
    
    List<BizSystem> loadTree();
    
    String delete (String id);
    
    List<Concat> getConcatsById(String id);
}
