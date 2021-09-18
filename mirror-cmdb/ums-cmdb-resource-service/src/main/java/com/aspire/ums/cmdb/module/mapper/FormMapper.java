package com.aspire.ums.cmdb.module.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.entity.Module;

public interface FormMapper {
	
    int deleteByPrimaryKey(String id);
    
    int deleteByModuleId(String moduleid);
    
    int insert(Form record);

    int insertSelective(Form record);
    
    Form selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Form record);

    int updateByPrimaryKey(Form record);
    
    List<Form> getFormsByModuleId(String moduleId);
    
    List<FormBean> selectFormBeanByModuleId(Module module);
    
    List<String> selectFormIdByModule(String id);
    
    List<Map<String, String>> getBussiness(@Param(value = "id") List<String> id);

	List<Map<String, String>> findBusCodeAndName(@Param(value = "id") List<String> ids);

}