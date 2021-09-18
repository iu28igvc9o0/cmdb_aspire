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
public class CmdbV3CodeCascade {

    /**
     * ID
     */
    private String id;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 子码表ID
     */
    private String subCodeId;
    /**
     * 子码表code
     */
    private String subFiledCode;
    /**
     * 查询SQL
     */
    private String sqlString;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private Integer isDelete;
}