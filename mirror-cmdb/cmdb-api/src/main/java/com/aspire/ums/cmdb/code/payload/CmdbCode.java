package com.aspire.ums.cmdb.code.payload;

import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeCollect;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:38
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCode {

    /**
     * ID
     */
    private String codeId;
    /**
     * 字段编码
     */
    private String filedCode;
    /**
     * 字段显示名称
     */
    private String filedName;
    /**
     * 模型分组ID
     */
    private String moduleCatalogId;
    /**
     * 字段分组
     */
    private CmdbV3ModuleCatalog catalog;
    /**
     * 控件类型ID
     */
    private String controlTypeId;
    /**
     * 控件类型ID
     */
    private CmdbControlType controlType;
    /**
     * 字段长度
     */
    private Integer codeLength;
    /**
     * 字段提示信息
     */
    private String codeTip;
    /**
     * 字段长度
     */
    private Integer displayStyle;
    /**
     * 是否绑定数据源
     */
    private String isBindSource;
    /**
     * 数据源信息
     */
    private CmdbV3CodeBindSource codeBindSource;
    /**
     * 是否验证
     */
    private String isValidate;
    /**
     * 验证信息
     */
    private List<CmdbV3CodeValidate> validates;
    /**
     * 是否审核
     */
    private String isApprove;
    /**
     * 审核信息
     */
    private CmdbV3CodeApprove approve;
    /**
     * 是否采集
     */
    private String isCollect;
    /**
     * 是否采集
     */
    private String defaultValue;
    /**
     * 是否采集
     */
    private Integer addReadOnly;
    /**
     * 是否采集
     */
    private Integer updateReadOnly;
    /**
     * 是否支持数据采集
     */
    private CmdbV3CodeCollect codeCollect;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 级联配置列表
     */
    private List<CmdbV3CodeCascade> cascadeList;
    /**
     * 表格配置列表
     */
    private List<CmdbV3CodeTable> tableColList;
    /**
     * 表格配置列表
     */
    private CmdbV3ModuleCodeSetting codeSetting;

    public CmdbCode (String codeId) {
        this.codeId = codeId;
    }
}
