package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;

import com.aspire.ums.cmdb.maintain.entity.CasoptionsBean;

public interface CasoptionsMapper {
    
    List<CasoptionsBean> getOptionBeanByFormId(String formId);
}