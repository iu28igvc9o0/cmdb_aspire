package com.aspire.ums.cmdb.maintain.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.RelationLog;
import com.aspire.ums.cmdb.maintain.mapper.RelationLogMapper;
import com.aspire.ums.cmdb.maintain.service.RelationLogService;

@Service
@Transactional
public class RelationLogServiceImpl implements RelationLogService {
	
	@Autowired
	private RelationLogMapper relationLogMapper;

    @Override
    public void insert(RelationLog relationLog) {
        relationLogMapper.insert(relationLog);
    }
	
}
