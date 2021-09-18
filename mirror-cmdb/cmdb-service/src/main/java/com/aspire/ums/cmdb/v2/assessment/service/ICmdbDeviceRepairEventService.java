package com.aspire.ums.cmdb.v2.assessment.service;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-25 19:56:44
*/
public interface ICmdbDeviceRepairEventService {
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDeviceRepairEvent> listByEntity(Integer pageNum, Integer pageSize, CmdbDeviceRepairEvent deviceRepairEvent);


    /**
     * 获取实例数量
     * @return 返回实例数量
     */
    Integer listCount(CmdbDeviceRepairEvent entity);
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDeviceRepairEvent get(CmdbDeviceRepairEvent entity);

    /**
     * 新增实例
     * @param data 实例数据
     * @return
     */
    void insert(JSONObject data);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDeviceRepairEvent entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDeviceRepairEvent entity);
}