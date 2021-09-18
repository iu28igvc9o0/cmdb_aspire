package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceServiceNum;

import java.util.List;

public interface CmdbMaintenanceServiceNumMapper {
    List<CmdbMaintenanceServiceNum> getServiceNumByProjectId(String projectId);

    void insertBatch(List<CmdbMaintenanceServiceNum> serviceNums);

    void delete(String projectId);
}
