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
public class CmdbModuleCodeValidateFull {

    /**
     * 关系ID
     */
    private String relationId;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 验证规则ID
     */
    private String validateId;
}