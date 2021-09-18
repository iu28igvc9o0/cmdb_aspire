package com.aspire.ums.cmdb.v3.code.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CodeBindSource {

    /**
     * ID
     */
    private String id;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 绑定数据源类型 数据字典/数据表/引用模型
     */
    private String bindSourceType;
    /**
     * 数据字典类型
     */
    private String dictSource;
    /**
     * 数据表名称
     */
    private String tableSource;
    /**
     * 数据表SQL配置
     */
    private String tableSql;
    /**
     * 引用模型ID
     */
    private String refModuleId;
    /**
     * 引用模型名称
     */
    private String refModuleName;
    /**
     * 显示模型码表ID
     */
    private String showModuleCodeId;
    /**
     * 显示模型码表名称
     */
    private String showModuleCodeName;
    /**
     * 引用模型数据查询条件
     */
    private String refModuleQuery;
    /**
     * 是否删除 0 否 1 是
     */
    private Integer isDelete;
}