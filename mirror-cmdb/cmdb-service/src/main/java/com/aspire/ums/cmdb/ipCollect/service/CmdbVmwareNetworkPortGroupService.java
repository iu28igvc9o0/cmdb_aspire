package com.aspire.ums.cmdb.ipCollect.service;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVmwareNetworkPortGroup;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-25 15:06
 * @description
 */
public interface CmdbVmwareNetworkPortGroupService {

    CmdbVmwareNetworkPortGroup findById(String id);

    CmdbVmwareNetworkPortGroup findByInstanceId(String instanceId);

    List<CmdbVmwareNetworkPortGroup> findByInstanceIdList(List<String> instanceIdList);

    void batchAdd(List<CmdbVmwareNetworkPortGroup> entityList);

    void add(CmdbVmwareNetworkPortGroup entity);

    void modify(CmdbVmwareNetworkPortGroup entity);

    /**
     * 根据实例ID更新数据
     */
    void updateByInstanceId(Map<String, Object> params);

    void delete(String id);

    /**
     * 根据实例ID删除
     * @param instanceId
     */
    void deleteByInstanceId(String instanceId);

    /**
     * 获取所有的实例ID
     */
    List<String> getAllInstanceId();

    /**
     * 根据实例ID批量删除
     * @param instanceIdList 实例ID列表
     */
    void batchDeleteByInstanceId(List<String> instanceIdList);

    /**
     * 构建并新增网段-端口组实例到cmdb数据库
     */
    void buildAndCreateInstance(String eventId,String instanceId);

    /**
     * 全量同步网段-端口组实例
     */
    void synAllNetworkPortGroup();
}
