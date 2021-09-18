package com.aspire.ums.cmdb.view.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewData
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdbViewData {
    /**
     * id
     */
    private String id;
    /**
     * 视图编码
     */
    private String moduleId;
    /**
     * 视图编码
     */
    private String viewCode;
    /**
     * 视图名称
     */
    private String viewName;

    private List<CmdbViewNodeData> nodeList;

}
