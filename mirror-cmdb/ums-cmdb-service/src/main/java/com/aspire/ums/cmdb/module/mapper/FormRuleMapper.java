package com.aspire.ums.cmdb.module.mapper;

import java.util.List;

import com.aspire.ums.cmdb.module.entity.FormRule;

public interface FormRuleMapper {
    int deleteByPrimaryKey(String id);

    int insert(FormRule record);

    int insertSelective(FormRule record);

    FormRule selectByPrimaryKey(String id);

    FormRule selectByCode(String code);

    List<FormRule> selectAllRule();
    
    int updateByPrimaryKeySelective(FormRule record);

    int updateByPrimaryKey(FormRule record);
}