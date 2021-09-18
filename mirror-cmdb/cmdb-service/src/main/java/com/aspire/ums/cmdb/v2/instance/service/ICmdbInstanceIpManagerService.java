package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-04 09:51:05
*/
public interface ICmdbInstanceIpManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstanceIpManager> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbInstanceIpManager get(CmdbInstanceIpManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbInstanceIpManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbInstanceIpManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbInstanceIpManager entity);

    /**
     * 批量新增ip信息
     * @param ipManagers
     */
    void insertByBatch(String instanceId, List<CmdbInstanceIpManager> ipManagers);

    /**
     * 获取所有的IP信息
     * @return
     */
    List<Map<String, Object>> getAllIpManagerList(String instanceId);
}