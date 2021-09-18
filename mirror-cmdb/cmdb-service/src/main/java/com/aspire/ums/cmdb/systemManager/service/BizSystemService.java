package com.aspire.ums.cmdb.systemManager.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.BizSystem;
import com.aspire.ums.cmdb.systemManager.payload.Concat;

import java.util.List;
import java.util.Map;

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
    Result<BizSystem> getAllBizSystemData(int pageNum,
                                          int pageSize,
                                          String pid,
                                          String department1,
                                          String department2,
                                          String bizName,
                                          String isdisable);
    
    String insert(BizSystem bizSystem);
    
    BizSystem getBizSystemById(String id);
    
    String update(BizSystem bizSystem);
    
    List<BizSystem> loadTree();
    
    String delete (String id);
    
    List<Concat> getConcatsById(String id);

    /**
     * 根据多个业务系统ID获取业务系统列表
     * @param ids
     * @return
     */
    List<Map<String, String>> getBizSystemByIds(String ids);
    /**
     * 根据业务系统名称获取部门信息
     * @param bizSystem 业务系统名称
     */
    Map<String, String> getDepartmentInfoByBizSystem(String bizSystem);
}
