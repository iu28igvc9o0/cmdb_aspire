package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmdb31ProvinceReportSetting {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String tableId;
    /**
     * 
     */
    private String resourceGroup;
    /**
     * 
     */
    private String resourceType;
    /**
     * 
     */
    private String calcExp;
    /**
     *
     */
    private String resourceControlType;
    /**
     *
     */
    private String calcValid;
    /**
     *
     */
    private String valueValid;
    /**
     * 排序
     */
    private Integer sortIndex;
}