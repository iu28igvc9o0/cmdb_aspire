package com.aspire.ums.cmdb.v3.module.payload;

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
public class CmdbV3ModuleCodeSetting {

    /**
     * ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 码表归属模型 归属本模型, 或归属引入模型
     */
    private String ownerModuleId;
    /**
     * 分组ID
     */
    private String groupId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否显示 0 显示 1 不显示
     */
    private Integer display;
    /**
     * 是否删除 0 否 1 是
     */
    private Integer isDelete;
}