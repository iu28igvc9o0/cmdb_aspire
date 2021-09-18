package com.aspire.ums.cmdb.maintenance.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称: IHardWareUseService
 * 类描述: 硬件维保使用
 * 创建人: PJX
 * 创建时间: 2019/8/4 13:16
 * 版本: v1.0
 */
public interface IHardWareUseService {

    /**
     * 根据项目ID 查询硬件维保使用列表
     * @param projectId
     * @return 返回硬件维保List
     */
    List<LinkedHashMap> queryByProjectId(String projectId);

    /**
     * 新增
     * @param hardwareUse
     * @return
     */
    Map<String, Object> addHardwareUse(HardwareUse hardwareUse);

    /**
     * 批量新增
     * @param list
     * @return
     */
    Map<String, Object> batchInsertHardWareUse(List<HardwareUse> list);

    /**
     * 更新
     * @param hardwareUse
     * @return
     */
    Map<String, Object> update(HardwareUse hardwareUse);

    /**
     * 批量更新
     * @param hardwareUse ID用逗号隔开
     * @return
     */
    Map<String, Object> batchUpdate(HardwareUse hardwareUse);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Result<HardwareUse> selectHardWareUseByPage(HardwareUseRequest request);

    /**
     * 查询导出数据
     * @param sendRequest
     * @return
     */
    JSONObject queryExportData(Map<String,Object> sendRequest);

    /**
     * 查询可用硬件维保数据
     * @return
     */
    List<Map<String,String>> getHardwareTableList();

    /**
     * 批量删除
     * @param id 多个ID逗号隔开
     * @return
     */
    Map<String,Object> deleteHardwareUse(String id);

    /**
     * 根据项目绑定设备ID 查询硬件维保使用列表
     * @param projectInstanceId
     * @return 返回硬件维保使用记录List
     */
    List<HardwareUse> getHardWareUseByProjectInstanceId(String projectInstanceId);
}
