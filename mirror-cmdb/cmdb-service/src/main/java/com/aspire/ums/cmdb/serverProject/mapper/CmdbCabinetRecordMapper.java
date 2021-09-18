package com.aspire.ums.cmdb.serverProject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetRecord;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Mapper
public interface CmdbCabinetRecordMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbCabinetRecord> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCabinetRecord> listByEntity(CmdbCabinetRecord entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCabinetRecord get(CmdbCabinetRecord entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbCabinetRecord entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbCabinetRecord entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbCabinetRecord entity);
}
