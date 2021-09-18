package com.aspire.ums.cmdb.module.payload;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Module implements Serializable{
	private static final long serialVersionUID = 1L;
    /**
     * 模型ID
     */
    private String id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型编码
     */
    private String code;
    /**
     * 模型分组ID
     */
    private String catalogId;
    /**
     * 父模型ID
     */
    private String parentId;
    /**
     * 是否附属模型
     */
    private Integer isVice;
    /**
     * 是否需要审核
     */
    private Integer enableApprove;
    /**
     * 模型图标路径
     */
    private String iconUrl;
    /**
     * 模型排序
     */
    private Integer sortIndex;
    /**
     * 删除标识
     */
    private Integer isDelete;
    /**
     * 删除标识
     */
    private Integer isDisplay;
    /**
     * 模型分区ID
     */
    private Integer partitionId;
    /**
     * 模型分组对象
     */
    private CmdbV3ModuleCatalog moduleCatalog;
    /**
     * 子模型列表
     */
    private List<Module> childModules;
    /**
     * 模型标签
     */
    private List<ModuleTag> tags;
    /**
     * 模型权限列表
     */
    private List<CmdbV3Authorization> auths;
    /**
     * 引用模型列表
     */
    private List<Module> refModules;
    /**
     * 模型分组ID
     */
    private List<CmdbModuleCodeGroup> groupList;
    /**
     * 模型显示码表集合
     */
//    private List<CmdbV3ModuleCodeSetting> codeSettingList;
}
