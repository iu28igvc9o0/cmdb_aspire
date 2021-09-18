package com.aspire.ums.cmdb.v3.module.payload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiRelation
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CmdbV3ModuleCiRelation {

    /**
     * 关系id
     */
    private String id;
    /**
     * 当前模型id
     */
    private String moduleId;
    /**
     * 关联类型
     */
    private String relationType;
    /**
     * 关联关系字典id
     */
    private String relation;
    /**
     * 关联模型id
     */
    private String relationModuleId;
    /**
     * 关联资源名称(例：宿主机)
     */
    private String resourceName;
    /**
     * 关联sql
     */
    private String relationSql;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createPerson;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 字段关联
     */
    private List<CmdbV3ModuleCiCodeRelation> codeRelationList;


}
