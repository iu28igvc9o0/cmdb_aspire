package com.aspire.ums.cmdb.v2.instance.mapper;

import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-04 09:51:05
*/
@Mapper
public interface CmdbInstanceIpManagerMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstanceIpManager> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbInstanceIpManager> listByEntity(CmdbInstanceIpManager entity);

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
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(@Param("list") List<CmdbInstanceIpManager> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbInstanceIpManager entity);

    /**
     * 修改实例
     * @param entities 实例数据
     * @return
     */
    void updateByBatch(List<CmdbInstanceIpManager> entities);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbInstanceIpManager entity);

    /**
     * 获取所有的IP信息
     * @return
     */
    List<Map<String, Object>> getAllIpManagerList(@Param("instanceId") String instanceId);
}