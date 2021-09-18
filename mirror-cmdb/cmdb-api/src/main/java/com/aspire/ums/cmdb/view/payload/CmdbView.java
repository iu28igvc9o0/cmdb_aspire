package com.aspire.ums.cmdb.view.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbView
 * Author:   hangfang
 * Date:     2020/5/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdbView {
    /**
     * id
     */
    private String id;
    /**
     * 视图编码
     */
    private String viewCode;
    /**
     * 视图名称
     */
    private String viewName;
    /**
     * 模型分组id
     */
    private String catalogId;
    /**
     * 模型id
     */
    private String moduleId;
    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonIgnore
    private Date updateTime;
    /**
     * 创建人
     */
    @JsonIgnore
    private String createPerson;
    /**
     * 更新人
     */
    @JsonIgnore
    private String updatePerson;

    /**
     * 视图节点列表
     */
    private List<CmdbViewNode> viewNodeList;
}
