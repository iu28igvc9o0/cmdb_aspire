package com.aspire.ums.cmdb.module.mapper;

import java.util.List;

import com.aspire.ums.cmdb.module.entity.FormTag;

public interface FormTagMapper {
    int deleteByPrimaryKey(String id);

    int insert(FormTag record);

    int insertSelective(FormTag record);

    FormTag selectByPrimaryKey(String id);
    
    List<FormTag> selectByModuleId(String id);
    
    List<String> selectTagIdByModuleId(String id);

    int updateByPrimaryKeySelective(FormTag record);

    int updateByPrimaryKey(FormTag record);
}