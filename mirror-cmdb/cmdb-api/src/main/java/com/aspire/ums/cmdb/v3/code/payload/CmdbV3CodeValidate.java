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
public class CmdbV3CodeValidate {

    /**
     * ID
     */
    private String id;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 验证类型
     */
    private String validType;
    /**
     * 验证表达式 当为正则表达式时, 此项必填
     */
    private String validTypeExpression;
    /**
     * 验证处理类
     */
    private String handlerClass;
}