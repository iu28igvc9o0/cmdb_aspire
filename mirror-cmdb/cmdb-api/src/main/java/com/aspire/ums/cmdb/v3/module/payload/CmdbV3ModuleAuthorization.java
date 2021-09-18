package com.aspire.ums.cmdb.v3.module.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3ModuleAuthorization {

    /**
     * 权限ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 权限ID
     */
    private String authId;
    /**
     * 是否删除 0 否 1 是
     */
    private Integer isDelete;
}