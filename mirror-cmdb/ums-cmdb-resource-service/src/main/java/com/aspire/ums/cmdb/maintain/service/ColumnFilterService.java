package com.aspire.ums.cmdb.maintain.service;


import com.aspire.ums.cmdb.maintain.entity.ColumnFilter;

public interface ColumnFilterService {
	
    ColumnFilter getOne(ColumnFilter columnFilter);

    void insert(ColumnFilter columnFilter);

    void update(ColumnFilter columnFilter);

    void delete(String id);
}
