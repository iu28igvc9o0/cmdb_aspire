package com.aspire.ums.cmdb.v3.code.payload;
import java.util.Date;
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
public class CmdbV3CodeCollect {

    /**
     * ID
     */
    private String id;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 采集类型
     */
    private String collectType;
    /**
     * 映射KEY值
     */
    private String collectMapperKey;
    /**
     * 当采集类型为脚本采集时, 填写脚本ID
     */
    private String collectScriptId;
    /**
     * 采集频率
     */
    private String collectFrequency;
    /**
     * 当采集频率为自定义时, 填写定时表达式
     */
    private String collectFrequencyExpression;
    /**
     * 是否删除
     */
    private Integer isDelete;
}