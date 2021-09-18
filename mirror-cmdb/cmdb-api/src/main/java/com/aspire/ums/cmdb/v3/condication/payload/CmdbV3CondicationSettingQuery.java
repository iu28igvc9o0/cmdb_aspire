package com.aspire.ums.cmdb.v3.condication.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbV3CondicationSettingQuery
 * Author:   zhu.juwang
 * Date:     2020/1/17 14:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CondicationSettingQuery {
    /**
     * 条件名称
     */
    private String condicationName;
    /**
     * 条件类型 页面/API
     */
    private String condicationType;
    /**
     * 当前页数
     */
    private Integer currentPage;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
}
