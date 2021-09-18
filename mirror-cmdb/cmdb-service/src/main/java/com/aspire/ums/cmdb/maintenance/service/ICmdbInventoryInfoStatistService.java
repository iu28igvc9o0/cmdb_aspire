package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;

import java.util.List;
import java.util.Map;

/*
 *  维保设备信息与cmdb存量信息比对 服务层接口
 * */
public interface ICmdbInventoryInfoStatistService {

    // 页面第一层接口
    List<Map<String,Object>> firstLayer();

    // 页面第二层接口
    Result<Map<String,Object>> secondLayer(CmdbInventoryInfoStatistRequest req);

    // 页面第三层接口
    Result<Map<String,Object>> thirdLayer(CmdbInventoryInfoStatistRequest req);
}
