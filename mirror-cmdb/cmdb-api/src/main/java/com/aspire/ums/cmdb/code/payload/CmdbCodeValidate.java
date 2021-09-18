package com.aspire.ums.cmdb.code.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCodeValidate {

    /**
     * 验证规则ID
     */
    private String validateId;
    /**
     * 验证规则名称
     */
    private String validateName;
    /**
     * 验证方法
     */
    private String validateFunc;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 排序
     */
    private Integer sortIndex;
}