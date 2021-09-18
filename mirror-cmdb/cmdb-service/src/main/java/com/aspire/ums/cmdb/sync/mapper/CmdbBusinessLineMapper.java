package com.aspire.ums.cmdb.sync.mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Mapper
public interface CmdbBusinessLineMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbBusinessLine> list();

    /**
     * 获取父级为空的实例
     * @return
     */
    List<CmdbBusinessLine> aloneList();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbBusinessLine> listByEntity(CmdbBusinessLine entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbBusinessLine get(CmdbBusinessLine entity);

    CmdbBusinessLine getByBusinessCode(String businessCode);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbBusinessLine entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbBusinessLine entity);

    void updateByBusinessCode(CmdbBusinessLine entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbBusinessLine entity);

    void deleteByBusinessCode(String businessCode);

    List<CmdbBusinessLine> getAllBusiness(@Param("pid") String pid);

    /**
     * 获取所有的独立业务子模块
     */
    List<CmdbBusinessLine> subBusinessList();
}
