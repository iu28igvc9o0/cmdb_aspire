package com.aspire.ums.cmdb.maintenance.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IHardWareService {

    /**
     * 根据项目ID 查询硬件维保列表
     * @param projectId
     * @return 返回硬件维保List
     */
    List<LinkedHashMap> queryByProjectId(String projectId);

    Map<String, Object> queryIsExist(String deviceSerialNumber, String warrantyDate);

    String queryIdByProjectInstanceId(String projectInstanceId);

    /**
     * 更新
     * @param hardware
     * @return
     */
    Map<String, Object> update(Hardware hardware);

    /**
     * 批量更新
     * @param hardwareList
     * @return
     */
    Map<String, Object> batchUpdate(Hardware hardwareList);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Result<Hardware> selectHardWareByPage(HardwareRequest request);

    /**
     * 查询导出数据
     * @param sendRequest
     * @return
     */
    List<Map<String, Object>> queryExportData(HardwareRequest sendRequest);

    /*
     *  根据项目名称 和 序列号 来获取信息
     * */
    Map<String,Object> queryInfoByNameAndDeviceSn(String projectName,String deviceSn);
}
