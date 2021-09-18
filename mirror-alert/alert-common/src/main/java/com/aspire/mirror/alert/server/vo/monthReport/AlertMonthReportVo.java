package com.aspire.mirror.alert.server.vo.monthReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.dto
 * 类名称:    HistorySearchRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 20:18
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertMonthReportVo {
    
    private String idcType;

    private String  pod;
    
    private String days;
    
    private String department1;
    
    private boolean deviceTypeNull;
    
    private String deviceTypePhy;
    
    private String deviceTypeVm;
 

}
