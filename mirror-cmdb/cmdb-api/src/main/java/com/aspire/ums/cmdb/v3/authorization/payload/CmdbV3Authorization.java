package com.aspire.ums.cmdb.v3.authorization.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:17
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3Authorization {

    /**
     * 权限定义ID
     */
    private String id;
    /**
     * 权限归属 模型/码表/其他
     */
    private String authOwner;
    /**
     * 权限编码
     */
    private String authCode;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 权限动作
     */
    private String authAction;
}