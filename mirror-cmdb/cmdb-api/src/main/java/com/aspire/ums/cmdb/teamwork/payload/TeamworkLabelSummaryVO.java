package com.aspire.ums.cmdb.teamwork.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:11
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamworkLabelSummaryVO {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String twTeamworkId;
    /**
     * 
     */
    private String twLabelId;
    /**
     * 
     */
    private String summaryContent;
    /**
     * 
     */
    private int isDelete;
}