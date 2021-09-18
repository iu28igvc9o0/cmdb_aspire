package com.aspire.mirror.composite.service.inspection.payload;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:27
 * 版本: v1.0
 */
@Data
public class BizSystem implements Serializable {
    
    private String id;
    
    private String bizName;
    
    private String pid;
    
    private String orgId;
    
    private String orgName;
    
    private String isdisable;
    
    private String remark;
    
    private int isdel;
    
    private OrgManager orgManager;
    
    List<Concat> concatList;
    
    List<BizSystem> children;
}
