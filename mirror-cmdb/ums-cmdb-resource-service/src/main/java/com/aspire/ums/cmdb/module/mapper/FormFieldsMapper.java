package com.aspire.ums.cmdb.module.mapper;

import com.aspire.ums.cmdb.module.entity.FormFields;

public interface FormFieldsMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByformId(String id);

    int insert(FormFields record);

    int insertSelective(FormFields record);

    FormFields selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormFields record);

    int updateByPrimaryKey(FormFields record);
}