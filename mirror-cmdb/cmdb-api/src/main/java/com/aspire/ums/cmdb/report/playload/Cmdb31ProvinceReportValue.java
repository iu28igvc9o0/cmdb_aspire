package com.aspire.ums.cmdb.report.playload;
import java.util.Date;
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
public class Cmdb31ProvinceReportValue {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String reportId;
    /**
     * 
     */
    private String settingId;
    /**
     *
     */
    private String reportValue;
}