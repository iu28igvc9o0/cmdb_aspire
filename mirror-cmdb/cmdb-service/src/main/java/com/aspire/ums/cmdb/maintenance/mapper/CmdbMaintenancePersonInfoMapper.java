package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenancePersonInfo;
import org.apache.ibatis.annotations.Param;

public interface CmdbMaintenancePersonInfoMapper {
    // 依据资源池 和 设备分类 查询维护人员信息
    CmdbMaintenancePersonInfo getOne(@Param("resourcePool") String resourcePool,@Param("deviceClass") String deviceClass);
}
