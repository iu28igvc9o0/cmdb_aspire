package com.aspire.ums.cmdb.ipCollect.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 18:04
 */
public interface BaseCollectService<T> {

    /**
     * 批量新增
     *
     * @param data
     */
    void batchAdd(List<T> data);

    /**
     * 修改
     *
     * @param data
     */
    void modify(T data);

    /**
     * 根据InstanceId更新
     * @param param
     */
    void updateByInstanceId(Map<String,Object> param);

    /**
     * 根据实例ID批量逻辑删除
     * @param instanceIds
     */
    void batchDeleteByInstanceId(List<String> instanceIds);

    /**
     * 根据实例ID批量物理删除
     * @param instanceIds
     */
    void batchDeleteByInstanceIdw(List<String> instanceIds);

    /**
     * 查询所有instanceId
     * @return
     */
    List<String> getAllInstanceId();

    /**
     * 根据instanceId集合查询
     * @param instanceIdList
     * @return
     */
    List<T> findByInstanceIdList(List<String> instanceIdList);

    /**
     * 根据instanceId集合查询
     * @param instanceIdList
     * @return
     */
    List<T> findDataByIpList(List<String> instanceIdList);

}
