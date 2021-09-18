package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;

import java.util.List;
import java.util.Map;

public interface MaintenStatusInfoStatistService {
    List<Map<String,Object>> statistMaintenStatusInfo(MaintenStatusInfoStatistRequest request);

    Result<Map<String,Object>> getMaintenProjectList(MaintenStatusInfoStatistRequest request);
}
