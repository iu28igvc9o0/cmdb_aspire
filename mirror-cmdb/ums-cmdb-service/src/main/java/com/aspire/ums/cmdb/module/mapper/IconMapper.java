package com.aspire.ums.cmdb.module.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.entity.Icon;

public interface IconMapper {
    int deleteByPrimaryKey(String id);

    int insert(Icon record);

    int insertSelective(Icon record);
    
    List<Map> selectIconAll(Icon icon);
    
    Integer selectMaxSortIndex();
    
    Icon selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Icon record);

    int updateByPrimaryKey(Icon record);
}