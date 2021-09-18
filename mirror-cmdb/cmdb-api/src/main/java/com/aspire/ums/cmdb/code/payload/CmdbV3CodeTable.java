package com.aspire.ums.cmdb.code.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-17 09:51:14
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CodeTable {

    /**
     * ID
     */
    private String id;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 表格列表
     */
    private String colTitle;
    /**
     * 表格列KEY
     */
    private String colKey;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private Integer isDelete;
}