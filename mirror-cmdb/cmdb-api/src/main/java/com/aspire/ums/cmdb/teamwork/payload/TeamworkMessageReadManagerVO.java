package com.aspire.ums.cmdb.teamwork.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:15
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamworkMessageReadManagerVO {

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
    private String twMessageId;
    /**
     * 
     */
    private String twReceiveAccount;
    /**
     * 
     */
    private Date twReceiveTime;
    /**
     * 
     */
    private String twReadStatus;
    /**
     * 
     */
    private int isDelete;
}