package com.aspire.ums.cmdb.maintain.service;

import java.util.List;

import com.aspire.ums.cmdb.maintain.entity.CasoptionsBean;


public interface CasoptionsService {
    List<CasoptionsBean> getCascaderOptions(String formId);
}
