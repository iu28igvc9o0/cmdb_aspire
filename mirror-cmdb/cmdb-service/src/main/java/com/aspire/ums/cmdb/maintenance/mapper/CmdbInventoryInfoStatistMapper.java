package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/*
*  维保设备信息与cmdb存量信息比对 数据层接口
* */
@Mapper
public interface CmdbInventoryInfoStatistMapper {

    // 页面第一层接口
    List<Map<String,Object>> firstLayer();

    // 获取设备区域为多个时的维保未关联数量
    List<Map<String,Object>> getFailCountByDeviceArea();

    // 页面第二层接口
    List<Map<String,Object>> secondLayer(CmdbInventoryInfoStatistRequest req);

    // 页面第三层接口 searchType:maintenance
    List<Map<String,Object>> maintenThirdLayer(CmdbInventoryInfoStatistRequest req);

    // 页面第三层的数量 searchType:maintenance
    Integer getMaintenThirdLayerCount(CmdbInventoryInfoStatistRequest req);

    // 页面第三层接口 searchType:instance
    List<Map<String,Object>> instanceThirdLayer(CmdbInventoryInfoStatistRequest req);

    // 页面第三层的数量 searchType:instance
    Integer getInstanceThirdLayerCount(CmdbInventoryInfoStatistRequest req);
}
