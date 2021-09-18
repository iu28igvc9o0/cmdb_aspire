package com.aspire.ums.cmdb.v3.condication.payload;

import com.aspire.ums.cmdb.module.payload.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CondicationSetting {

    /**
     * ID
     */
    private String id;
    /**
     * 配置编码
     */
    private String condicationCode;
    /**
     * 条件名称
     */
    private String condicationName;
    /**
     * 条件类型 页面/API
     */
    private String condicationType;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 模型对象
     */
    private SimpleModule simpleModule;
    /**
     * 接入用户ID
     */
    private String accessUserId;
    /**
     * 接入用户对象
     */
    private CmdbV3AccessUser accessUser;
    /**
     * 备注
     */
    private String remark;
    /**
     * 条件码表关联关系
     */
    private List<CmdbV3CondicationSettingRelation> settingRelationList;
    /**
     * 返回值关系配置
     */
    private List<CmdbV3CondicationReturnRelation> returnRelationList;
    /**
     * 排序配置
     */
    private List<CmdbV3CondicationSortRelation> sortRelationList;
    /**
     * 是否必填
     */
    private Integer isDelete;
}