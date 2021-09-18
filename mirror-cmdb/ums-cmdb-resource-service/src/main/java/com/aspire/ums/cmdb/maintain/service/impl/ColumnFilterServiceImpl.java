package com.aspire.ums.cmdb.maintain.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.ColumnFilter;
import com.aspire.ums.cmdb.maintain.mapper.ColumnFilterMapper;
import com.aspire.ums.cmdb.maintain.service.ColumnFilterService;

@Service
@Transactional
public class ColumnFilterServiceImpl implements ColumnFilterService {
	
	@Autowired
	private ColumnFilterMapper columnFilterMapper;

    @Override
    public ColumnFilter getOne(ColumnFilter columnFilter) {
        // TODO Auto-generated method stub
        return columnFilterMapper.getOne(columnFilter);
    }

    @Override
    public void insert(ColumnFilter columnFilter) {
        // TODO Auto-generated method stub
        columnFilterMapper.insert(columnFilter);
    }

    @Override
    public void update(ColumnFilter columnFilter) {
        // TODO Auto-generated method stub
        columnFilterMapper.update(columnFilter);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        columnFilterMapper.delete(id);
    }

}
