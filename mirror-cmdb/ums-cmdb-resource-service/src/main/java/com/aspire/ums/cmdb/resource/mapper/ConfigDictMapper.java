package com.aspire.ums.cmdb.resource.mapper;


import java.util.List;
import java.util.Map;

public interface ConfigDictMapper {

    List<Map> getDictConfig(Map<String, Object> params);

}
