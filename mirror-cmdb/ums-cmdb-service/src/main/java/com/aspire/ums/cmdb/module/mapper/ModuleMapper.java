package com.aspire.ums.cmdb.module.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.entity.FormTag;
import com.aspire.ums.cmdb.module.entity.Module;
import org.apache.ibatis.annotations.Param;

public interface ModuleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Module record);

    int insertSelective(Module record);
    
    int selectMaxIndex();
    
    Module selectByPrimaryKey(String id);
    
    List<Module> selectModule();
    
    List<Module> selectSelective(Module module);

    int updateByPrimaryKeySelective(Module record);

    int updateByPrimaryKey(Module record);
    
    Module getModuleByModuleName(String moduleName);
    
    List<FormTag> selectTagByModuleId(String id);

    /**
     * 根据parentId 查询模型列表
     * @param parentId 父模型ID
     * @return 子模型列表
     */
    List<Module> selectModuleByParentId(@Param("parentId") String parentId);
}