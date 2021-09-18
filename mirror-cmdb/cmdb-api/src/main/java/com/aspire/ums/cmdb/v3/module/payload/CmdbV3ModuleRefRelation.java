package com.aspire.ums.cmdb.v3.module.payload;
import java.util.Date;
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
public class CmdbV3ModuleRefRelation {

    /**
     * 引用模型关系ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 引用模型ID
     */
    private String refModuleId;
    /**
     * 显示排序
     */
    private Integer sortIndex;
    /**
     * 是否删除 0 否 1是
     */
    private Integer isDelete;
}