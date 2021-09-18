package com.aspire.ums.cmdb.demand.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.demand.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/4 15:04
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDemandChanged {
    
    /**
     * 需求ID
     */
    private String demandId;
    
    private String name;
    
    private String oldVal;
    
    private String newVal;
    
    private String updateTime;
    
    private String updateUser;
    
}
