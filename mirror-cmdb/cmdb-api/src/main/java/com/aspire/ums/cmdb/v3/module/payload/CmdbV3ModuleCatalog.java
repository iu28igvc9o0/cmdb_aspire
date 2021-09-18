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
public class CmdbV3ModuleCatalog {

    /**
     * ID标识
     */
    private String id;
    /**
     * 模型分类编码
     */
    private String catalogCode;
    /**
     * 模型分组名称
     */
    private String catalogName;
    /**
     * 父模型分组ID
     */
    private String parentCatalogId;
    /**
     * 模型分组排序
     */
    private Integer sortIndex;
    /**
     * 是否删除, 0 否 1 是
     */
    private Integer isDelete;
}