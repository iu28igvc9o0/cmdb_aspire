package com.aspire.ums.cmdb.v2.module.mapper;

import com.aspire.ums.cmdb.module.payload.ModuleTag;

import java.util.List;

public interface ModuleTagMapper {

    int deleteByPrimaryKey(String id);

    int deleteTagSelective(ModuleTag tag);

    int insert(ModuleTag record);

    int insertSelective(ModuleTag record);

    ModuleTag selectByPrimaryKey(String id);

    List<ModuleTag> selectByModuleId(String id);

    List<String> selectTagIdByModuleId(String id);

    int updateByPrimaryKeySelective(ModuleTag record);

    int updateByPrimaryKey(ModuleTag record);
}
