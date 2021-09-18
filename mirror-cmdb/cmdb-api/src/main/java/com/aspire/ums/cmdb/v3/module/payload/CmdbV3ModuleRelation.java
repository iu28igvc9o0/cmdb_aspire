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
public class CmdbV3ModuleRelation {

    /**
     * ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 是否显示 0 显示 1 不显示
     */
    private Integer display;
}