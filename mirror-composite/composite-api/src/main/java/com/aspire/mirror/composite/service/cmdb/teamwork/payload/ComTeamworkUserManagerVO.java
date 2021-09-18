package com.aspire.mirror.composite.service.cmdb.teamwork.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:17
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTeamworkUserManagerVO {

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
    private String twUserName;
    /**
     * 
     */
    private String twUserAccount;
    /**
     * 用户账号
     */
    private String twUserRole;
    /**
     * 
     */
    private int twAllowClose;
    /**
     * 
     */
    private int twAllowManagerUser;
    /**
     * 
     */
    private int twAllowManagerLabel;
    /**
     * 
     */
    private int isDelete;
}