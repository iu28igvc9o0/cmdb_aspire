package com.aspire.ums.cmdb.systemManager.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 18:43
 * 版本: v1.0
 */
@Data
public class OrgManager implements Serializable {
    private String id;
    
    private String orgName;
    
    private String orgType;
    
    private String pid;
    
    private String remark;
    
    private String isdel;
    
    List<OrgManager> children;
    
}
