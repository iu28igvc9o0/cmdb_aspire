package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:12
*/
@Mapper
public interface CmdbDeviceTypeMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDeviceType> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbDeviceType> listByEntity(CmdbDeviceType entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDeviceType get(CmdbDeviceType entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDeviceType entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDeviceType entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDeviceType entity);
}