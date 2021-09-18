package com.aspire.ums.cmdb.resource.mapper;


import java.util.List;
import java.util.Map;

public interface FinancialBusinessMapper {

    List<Map> findCmdbBusiness1Info(Map<String, Object> params);
    
}