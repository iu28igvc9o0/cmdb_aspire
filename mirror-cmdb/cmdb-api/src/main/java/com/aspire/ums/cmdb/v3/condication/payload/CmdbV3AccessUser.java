package com.aspire.ums.cmdb.v3.condication.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:42
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3AccessUser {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String userName;
    /**
     * 
     */
    private String password;
    /**
     * 
     */
    private String displayName;
    /**
     * 
     */
    private Integer isDelete;
}