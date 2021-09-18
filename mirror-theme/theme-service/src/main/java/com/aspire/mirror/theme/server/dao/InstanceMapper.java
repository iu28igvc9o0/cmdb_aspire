package com.aspire.mirror.theme.server.dao;

import com.aspire.mirror.theme.server.entity.InstanceBaseColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InstanceMapper {


    @SuppressWarnings("rawtypes")
    List<InstanceBaseColumn> getInstanceBaseInfoList(Map<String, Object> map);

}
