package com.aspire.ums.cmdb.view.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewQuery
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
public class CmdbViewQuery {
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
     * 页容量
     */
    private Integer pageSize;
    /**
     * 当前页码
     */
    private Integer currentPage;
}
