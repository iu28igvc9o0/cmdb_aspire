package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.CasoptionsBean;
import com.aspire.ums.cmdb.maintain.mapper.CasoptionsMapper;
import com.aspire.ums.cmdb.maintain.service.CasoptionsService;


@Service
@Transactional
public class CasoptionsServiceImpl implements CasoptionsService {
	
    @Autowired
    private CasoptionsMapper casoptionsMapper;

    @Override
    public List<CasoptionsBean> getCascaderOptions(String formId) {
        return casoptionsMapper.getOptionBeanByFormId(formId);
    }
}
