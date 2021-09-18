package com.aspire.ums.cmdb.networkCard.service;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
/**
* 描述：
* @author
* @date 2019-09-18 11:26:39
*/
public interface ICmdbInstanceNetworkCardService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstanceNetworkCard> list();

    /**
     * 获取实例根据InstanceId
     * @return 返回所有实例数据
     */
    List<CmdbInstanceNetworkCard> listByInstanceId(String instanceId);

    /**
     * 根据名称 获取数据信息
     * @param name 名称
     * @return 返回实例信息的数据信息
     */
    CmdbInstanceNetworkCard get(String name);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbInstanceNetworkCard entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbInstanceNetworkCard entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbInstanceNetworkCard entity);

    /**
     * 逻辑删除
     * @param idList 实例数据
     * @return
     */
    void deleteByLogic(List idList);

    /**
     * 获取所有导出实例 依据instanceId
     * @param instanceId 网卡ID
     * @return 返回所有实例数据
     */
    List<Map<String,Object>> exportList(String instanceId);
}