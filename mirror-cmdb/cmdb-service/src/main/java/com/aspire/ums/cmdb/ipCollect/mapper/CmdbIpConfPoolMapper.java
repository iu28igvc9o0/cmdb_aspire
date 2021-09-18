package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpAddressPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpConfPoolEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 存活ip-网络设备配置文件解析
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 15:57
 */
@Repository
public interface CmdbIpConfPoolMapper {

    /**
     * 新增
     *
     * @param entity
     */
    void add(CmdbIpConfPoolEntity entity);

    /**
     * 批量插入
     *
     * @param entityList
     */
    void batchAdd(List<CmdbIpConfPoolEntity> entityList);

    /**
     * 根据InstanceId修改实例
     *
     * @param entity
     */
    void update(CmdbIpConfPoolEntity entity);

    /**
     * 根据InstanceId修改实例（推送处理）
     *
     * @param params
     */
    void updateByInstanceId(Map<String, Object> params);

    /**
     * 逻辑删除
     *
     * @param instanceIdList
     */
    void batchDeleteByInstanceId(List<String> instanceIdList);

    /**
     * 物理删除
     *
     * @param instanceIdList
     */
    void batchDeleteByInstanceIdw(List<String> instanceIdList);

    /**
     * 查询所有instanceId
     *
     * @return
     */
    List<String> getAllInstanceId();

    /**
     * 根据instanceId集合查询
     *
     * @param instanceIds
     * @return
     */
    List<CmdbIpConfPoolEntity> findByInstanceids(List<String> instanceIds);

    /**
     * 根据IP批量查询
     *
     * @param ipList
     * @return
     */
    List<CmdbIpConfPoolEntity> findDataByIpList(List<String> ipList);
}
