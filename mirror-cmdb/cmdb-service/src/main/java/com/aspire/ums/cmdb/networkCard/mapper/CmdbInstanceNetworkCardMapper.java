package com.aspire.ums.cmdb.networkCard.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-09-18 11:26:39
*/
@Mapper
public interface CmdbInstanceNetworkCardMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstanceNetworkCard> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbInstanceNetworkCard> listByEntity(CmdbInstanceNetworkCard entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbInstanceNetworkCard get(CmdbInstanceNetworkCard entity);

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
     * @param idList id的集合
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