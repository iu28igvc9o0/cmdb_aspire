package com.aspire.ums.cmdb.v2.idc.mapper;

import com.aspire.ums.cmdb.v2.idc.entity.CmdbIdcManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:30
*/
@Mapper
public interface CmdbIdcManagerMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbIdcManager> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbIdcManager> listByEntity(CmdbIdcManager entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbIdcManager get(CmdbIdcManager entity);

    /**
     * 根据资源池编码 获取数据信息
     * @param idcCode 资源池编码
     * @return 返回实例信息的数据信息
     */
    CmdbIdcManager getByIdcCode(@Param("idcCode") String idcCode);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbIdcManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbIdcManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbIdcManager entity);
}