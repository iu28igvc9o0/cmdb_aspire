package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpArpPoolEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 存活ip-arp扫描网段地址
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 17:23
 */
@Repository
public interface CmdbIpArpPoolMapper {

    /**
     * 新增
     *
     * @param entity
     */
    void add(CmdbIpArpPoolEntity entity);

    /**
     * 批量插入
     *
     * @param entityList
     */
    void batchAdd(List<CmdbIpArpPoolEntity> entityList);

    /**
     * 根据InstanceId修改实例
     *
     * @param entity
     */
    void update(CmdbIpArpPoolEntity entity);

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
    List<CmdbIpArpPoolEntity> findByInstanceids(List<String> instanceIds);

    /**
     * 根据IP批量查询
     * @param ipList
     * @return
     */
    List<CmdbIpArpPoolEntity> findDataByIpList(List<String> ipList);
}
