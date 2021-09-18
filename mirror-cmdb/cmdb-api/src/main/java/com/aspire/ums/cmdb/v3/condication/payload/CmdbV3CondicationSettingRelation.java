package com.aspire.ums.cmdb.v3.condication.payload;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
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
public class CmdbV3CondicationSettingRelation {

    /**
     * ID
     */
    private String id;
    /**
     * 查询条件ID
     */
    private String condicationSettingId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 码表ID
     */
    private CmdbCode cmdbCode;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 查询条件类型
     */
    private String operateType;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否必填
     */
    private String isRequire;
    /**
     * 手工输入
     */
    private String showInput;
    /**
     * 显示选项
     */
    private String showOption;
    /**
     * 是否必填
     */
    private Integer isDelete;
    /**
     * 单个查询条件模糊匹配多个字段时的码表ID
     */
    private String containCodeId;
}