package com.aspire.ums.cmdb.v3.module.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-02-22 14:33:21
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3ModuleGroupRelation {

    /**
     * ID标识
     */
    private String id;
    /**
     * 分组ID
     */
    private String groupId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 是否显示 0 显示 1 不显示
     */
    private Integer display;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除, 0 否 1 是
     */
    private Integer isDelete;
}