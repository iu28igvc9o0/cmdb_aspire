package com.aspire.ums.cmdb.v3.module.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3ModuleGroup {

    /**
     * ID标识
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;

    /**
     * 模型分组配置编码
     */
    private String moduleGroupCode;

    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 父分组ID
     */
    private String parentGroupId;
    /**
     * 是否末级节点 0 否 1 是
     */
    private Integer isEndNode;
    /**
     * 节点级别
     */
    private Integer nodeLevel;
    /**
     * 排序
     */
    private Integer display;
    /**
     * 固定位置 left/right/空
     */
    private String fixed;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除, 0 否 1 是
     */
    private Integer isDelete;
}