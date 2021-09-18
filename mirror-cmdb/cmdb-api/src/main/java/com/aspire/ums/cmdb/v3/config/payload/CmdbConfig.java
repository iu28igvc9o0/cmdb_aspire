package com.aspire.ums.cmdb.v3.config.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-05-06 10:54:58
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbConfig {

    /**
     * ID
     */
    private String id;
    /**
     * 配置编码
     */
    private String configCode;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置值类型
     */
    private String configValueType;
    /**
     * 配置备注
     */
    private String configRemark;
}