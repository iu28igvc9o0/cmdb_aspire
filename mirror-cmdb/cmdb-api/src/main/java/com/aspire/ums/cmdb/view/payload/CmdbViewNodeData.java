package com.aspire.ums.cmdb.view.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeData
 * Author:   hangfang
 * Date:     2020/5/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdbViewNodeData {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点图标
     */
    private String icon;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点名称
     */
    private String showInfo;
    /**
     * 提供查询信息
     */
    private LinkedHashMap<String, String> queryInfo;
    /**
     * 提供后台查询信息
     */
    private LinkedHashMap<String, String> queryToData;
    /**
     * 节点元数据
     */
    private Map<String, Object> metaData;
    /**
     * 子级
     */
    private List<CmdbViewNodeData> subNodeList;
}
