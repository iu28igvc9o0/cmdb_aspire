package com.aspire.ums.cmdb.view.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNode
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
public class CmdbViewNode {
    /**
     * 视图节点id
     */
    private String id;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点图标
     */
    private String iconDictId;
    /**
     * 节点图标
     */
    private String icon;
    /**
     * 视图节点id
     */
    private String viewId;
    /**
     * 父级节点id
     */
    private String parentNodeId;
    /**
     * 节点名称
     */
    private String parentNodeName;
    /**
     * 是否查询（部分节点为文本即可）
     */
    private Integer enableQuery;
    /**
     * 使用配置项
     */
    private Integer enableUseCode;
    /**
     * 配置项id
     */
    private String useCodeId;
    /**
     * 对应实例属性
     */
    private String toQueryFiled;
    /**
     * sql配置
     */
    private String configSql;
    /**
     * 显示子节点数量
     */
    private Integer enableShowCount;
    /**
     * 多数据显示分隔符
     */
    private String showSep;

    /**
     * 节点显示信息列表
     */
    private List<CmdbViewNodeShow> nodeShowList;
    /**
     * 子节点列表
     */
    private List<CmdbViewNode> subNodeList;
}
