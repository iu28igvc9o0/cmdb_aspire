package com.aspire.ums.cmdb.deviceStatistic.mapper;

import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeResp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

 

@Mapper
public interface ConfigureChangeMapper {
    /**
     * 配置项变更统计
     * @param   
     */
    List<ConfigureChangeResp> selectConfigureChange(Map<String, Object> hashMap);
}
