package com.aspire.ums.cmdb.code.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCodeQuery
 * Author:   zhu.juwang
 * Date:     2019/5/17 16:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCodeQuery {
    private Integer currentPage;
    private Integer pageSize;
    private String codeCode;
    private String codeName;
    private String moduleCatalogId;
}
