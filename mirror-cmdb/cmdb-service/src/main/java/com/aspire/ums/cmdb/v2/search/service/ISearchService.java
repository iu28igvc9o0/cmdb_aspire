package com.aspire.ums.cmdb.v2.search.service;

import com.aspire.ums.cmdb.search.payload.ColumnFilter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ISearchService
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:14
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ISearchService {

    /**
     * 查询过滤项
     * @param columnFilter
     * @return
     */
    ColumnFilter getOne(ColumnFilter columnFilter);
    /**
     * 新增过滤项
     * @param columnFilter
     */
    void insert(ColumnFilter columnFilter);

    /**
     * 修改过滤项
     * @param columnFilter
     */
    void update(ColumnFilter columnFilter);

    /**
     * 删除过滤项
     * @param id 过滤项ID
     */
    void delete(String id);
}
