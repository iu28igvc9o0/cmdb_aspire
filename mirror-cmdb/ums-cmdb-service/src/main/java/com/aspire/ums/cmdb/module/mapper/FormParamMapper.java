package com.aspire.ums.cmdb.module.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.entity.FormParam;

public interface FormParamMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByformId(String id);

    int insert(FormParam record);

    int insertSelective(FormParam record);

    FormParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormParam record);

    int updateByPrimaryKey(FormParam record);
    
    List<Map<String,String>> getParamsMapByFormId(String formId);
}