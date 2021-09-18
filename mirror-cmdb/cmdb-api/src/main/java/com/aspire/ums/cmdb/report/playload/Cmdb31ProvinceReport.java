package com.aspire.ums.cmdb.report.playload;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Cmdb31ProvinceReport {

    private String id;
    /**
     * 资源归属
     */
    private String resourceOwner;
    /**
     * 
     */
    private String provinceName;
    /**
     * 
     */
    private String submitMonth;
    /**
     * 表名
     */
    private String tableId;
    /**
     * 
     */
    private Date submitTime;
    /**
     * 
     */
    private Date updateTime;
    /**
     * 审核状态
     */
    private String approveStatus;
    /**
     * 更新人
     */
    private String updatePerson;
    /**
     * 每行列数据
     */
    private List<Cmdb31ProvinceReportValue> reportValueList;
}