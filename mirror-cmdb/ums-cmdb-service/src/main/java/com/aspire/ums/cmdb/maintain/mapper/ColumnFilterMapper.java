package com.aspire.ums.cmdb.maintain.mapper;


import com.aspire.ums.cmdb.maintain.entity.ColumnFilter;

public interface ColumnFilterMapper {

	
    ColumnFilter getOne(ColumnFilter columnFilter);

	void insert(ColumnFilter columnFilter);

	void update(ColumnFilter columnFilter);

	void delete(String id);

}
