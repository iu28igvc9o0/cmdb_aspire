package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbBusinessContact;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Mapper
public interface CmdbBusinessContactMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbBusinessContact> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbBusinessContact> listByEntity(CmdbBusinessContact entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbBusinessContact get(CmdbBusinessContact entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbBusinessContact entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbBusinessContact entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbBusinessContact entity);
}
