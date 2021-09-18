package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 虚拟ip Mapper
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 17:51
 */
@Repository
public interface CmdbVipCollectMapper {

    void add(CmdbVipCollectEntity entity);

    void batchAdd(@Param("entityList") List<CmdbVipCollectEntity> entityList);

    void update(CmdbVipCollectEntity entity);

    void updateByInstanceId(Map<String, Object> params);

    void deleteByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 逻辑删除
     *
     * @param instanceIdList
     */
    void batchDeleteByInstanceId(@Param("instanceIdList") List<String> instanceIdList);

    /**
     * 物理删除
     * @param instanceIdList
     */
    void batchDeleteByInstanceId2(@Param("instanceIdList") List<String> instanceIdList);

    /**
     * 获取资源池列表
     *
     * @return
     */
    List<String> findResourceList();

    /**
     * 获取使用类型列表
     *
     * @return
     */
    List<String> findUseTypeList();

    /**
     * 根据条件查询列表
     *
     * @param request
     * @return
     */
    List<CmdbVipCollectEntity> findPage(CmdbVipCollectRequest request);

    /**
     * 根据条件统计
     *
     * @param request
     * @return
     */
    int findPageCount(CmdbVipCollectRequest request);

    /**
     * 查询所有instanceId
     *
     * @return
     */
    List<String> getAllInstanceId();

    /**
     * 根据instanceId集合查询
     *
     * @param instanceIdList
     * @return
     */
    List<CmdbVipCollectEntity> findByInstanceIdList(List<String> instanceIdList);

    /**
     * 查询所有vip
     * @return
     */
    List<String> findAllVip();

}
