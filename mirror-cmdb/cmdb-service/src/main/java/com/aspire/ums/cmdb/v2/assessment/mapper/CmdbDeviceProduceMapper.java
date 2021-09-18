package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:11
*/
@Mapper
public interface CmdbDeviceProduceMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDeviceProduce> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbDeviceProduce> listByEntity(CmdbDeviceProduce entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDeviceProduce get(CmdbDeviceProduce entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDeviceProduce entity);

    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbDeviceProduce> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDeviceProduce entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDeviceProduce entity);
}