package com.aspire.ums.cmdb.v2.icon.mapper;

import com.aspire.ums.cmdb.icon.payload.CmdbIcon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CmdbIconMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmdbIcon record);

    int insertSelective(CmdbIcon record);

    List<Map> selectIconAll(CmdbIcon icon);

    Integer selectMaxSortIndex();

    CmdbIcon selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmdbIcon record);

    int updateByPrimaryKey(CmdbIcon record);
}
