package com.aspire.ums.cmdb.module.mapper;

import java.util.List;

import com.aspire.ums.cmdb.module.entity.FormScript;

public interface FormScriptMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByFormId(String id);
    
    int deleteByTagId(String id);
    
    int insert(FormScript record);

    int insertSelective(FormScript record);

    FormScript selectByPrimaryKey(String id);
    
    List<FormScript> selectScriptByTag(String tag);
    
    FormScript selectScriptByformId(String fromId);
    
    int updateByPrimaryKeySelective(FormScript record);

    int updateByPrimaryKeyWithBLOBs(FormScript record);

    int updateByPrimaryKey(FormScript record);
}