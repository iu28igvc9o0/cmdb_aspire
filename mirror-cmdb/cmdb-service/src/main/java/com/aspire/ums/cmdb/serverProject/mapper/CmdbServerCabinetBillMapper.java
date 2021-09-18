package com.aspire.ums.cmdb.serverProject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.serverProject.payload.CmdbServerCabinetBill;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:14
 */
@Mapper
public interface CmdbServerCabinetBillMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbServerCabinetBill> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbServerCabinetBill> listByEntity(CmdbServerCabinetBill entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbServerCabinetBill get(CmdbServerCabinetBill entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbServerCabinetBill entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbServerCabinetBill entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbServerCabinetBill entity);
}
