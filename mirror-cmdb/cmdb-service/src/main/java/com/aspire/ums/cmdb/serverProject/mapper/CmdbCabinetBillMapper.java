package com.aspire.ums.cmdb.serverProject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBill;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Mapper
public interface CmdbCabinetBillMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbCabinetBill> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCabinetBill> listByEntity(CmdbCabinetBill entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCabinetBill get(CmdbCabinetBill entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbCabinetBill entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbCabinetBill entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbCabinetBill entity);
}
