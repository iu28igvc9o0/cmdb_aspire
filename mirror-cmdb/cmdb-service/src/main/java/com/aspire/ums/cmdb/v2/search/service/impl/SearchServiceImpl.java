package com.aspire.ums.cmdb.v2.search.service.impl;

import com.aspire.ums.cmdb.search.payload.ColumnFilter;
import com.aspire.ums.cmdb.v2.search.mapper.SearchMapper;
import com.aspire.ums.cmdb.v2.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SearchServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:15
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private SearchMapper searchMapper;
    @Override
    public ColumnFilter getOne(ColumnFilter columnFilter) {
        return searchMapper.getOne(columnFilter);
    }

    @Override
    public void insert(ColumnFilter columnFilter) {
        searchMapper.insert(columnFilter);
    }

    @Override
    public void update(ColumnFilter columnFilter) {
        searchMapper.update(columnFilter);
    }

    @Override
    public void delete(String id) {
        searchMapper.delete(id);
    }

}
