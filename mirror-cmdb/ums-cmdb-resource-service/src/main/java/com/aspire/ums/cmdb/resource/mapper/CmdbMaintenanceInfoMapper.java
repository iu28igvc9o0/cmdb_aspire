package com.aspire.ums.cmdb.resource.mapper;

import com.aspire.ums.cmdb.resource.entity.CmdbMaintenanceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmdbMaintenanceInfoMapper {

    List<CmdbMaintenanceInfo> getMaintenanceInfo(@Param(value = "pId") String pId);
    //模板导出参考字段
    List<CmdbMaintenanceInfo> getMaintenanceInfo();
}
