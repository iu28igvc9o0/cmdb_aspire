package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:11
*/
@Mapper
public interface CmdbDeviceRepairEventMapper {

    /**
     * 所有实例数量
     * @return 所有实例数量
     */
    Integer listCount(CmdbDeviceRepairEvent repairEvent);
    /**
     * 获取所有实例
     * @param repairEvent 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbDeviceRepairEvent> listByEntity(@Param("pageNum") Integer pageNum,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("event") CmdbDeviceRepairEvent repairEvent);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDeviceRepairEvent get(CmdbDeviceRepairEvent entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDeviceRepairEvent entity);

    /**
     * 新增实例
     * @param entitis 实例数据
     * @return
     */
    void insertByBatch(List<CmdbDeviceRepairEvent> entitis);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDeviceRepairEvent entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDeviceRepairEvent entity);

    /**
     * 删除实例
     * @param ids 实例数据
     * @return
     */
    void deleteByBatch(List<String> ids);
}