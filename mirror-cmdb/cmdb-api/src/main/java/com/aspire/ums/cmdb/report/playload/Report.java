package com.aspire.ums.cmdb.report.playload;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:43
 * 版本: v1.0
 */
@Data
public class Report implements Serializable {
    
    private String bizSystem;
    private String bizSystemConcat;
    private String department1;
    private String department2;
    private String idcType;
    private String podName;
    private String totalNum;
    private String serverNum;
    private String vmNum;
    private String storageNum;
    private String networkNum;
    private String concat;
    
}
