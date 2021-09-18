package com.aspire.ums.cmdb.teamwork.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:08
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamworkLabelVO {

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
    private String twLabelName;
    /**
     * 
     */
    private int isDelete;
    
    private String summaryContent;
}