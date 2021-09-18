package com.aspire.ums.cmdb.deviceStatistic.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeResp;
 

public interface ConfigureChangeService {
    /**
     * 配置项变更统计
     */
    List<Map<String, Object>> selectConfigureChange(ConfigureChangeRequest configureChangeRequest);
}
