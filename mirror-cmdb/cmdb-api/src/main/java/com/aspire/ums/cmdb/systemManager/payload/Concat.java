package com.aspire.ums.cmdb.systemManager.payload;

import lombok.Data;

import java.io.Serializable;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:30
 * 版本: v1.0
 */
@Data
public class Concat implements Serializable {
    private String bizId;
    
    private String name;
    
    private String phone;
    
    private String email;
    
}
