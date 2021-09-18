package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaintenStatusInfoStatistMapper {
    // 维保项目在建、已建、过保、即将过保的4个维度去统计维保数量和维保设备数量
    List<Map<String,Object>> statistMaintenStatusInfo(MaintenStatusInfoStatistRequest request);
    // 查看统计的维保项目
    List<Map<String,Object>> getMaintenProjectList(MaintenStatusInfoStatistRequest request);
    // 统计维保项目的数量
    Integer countMaintenProjectList(MaintenStatusInfoStatistRequest request);
}
