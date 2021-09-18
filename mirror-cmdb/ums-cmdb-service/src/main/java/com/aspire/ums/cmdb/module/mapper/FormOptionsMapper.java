package com.aspire.ums.cmdb.module.mapper;

import java.util.List;

import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.OptionBean;

public interface FormOptionsMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByformId(String id);

    int insert(FormOptions record);

    int insertSelective(FormOptions record);

    FormOptions selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormOptions record);

    int updateByPrimaryKey(FormOptions record);
    
    List<String> getOptionsMapByFormId(String formId);
    
    List<OptionBean> getOptionBeanByFormId(String formId);
    
    List<FormOptions> getByFormId(String id);
}