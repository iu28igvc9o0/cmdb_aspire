package com.aspire.ums.cmdb.v3.config.mapper;

import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-05-06 10:54:58
*/
@Mapper
public interface CmdbConfigMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbConfig> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbConfig> listByEntity(CmdbConfig entity);

    /**
     * 获取所有实例的数量
     * @param req 实例信息
     * @return 返回所有实例数据的数量
     */
    int listCountWithPage(CmdbConfigRequest req);

    /**
     * 获取所有实例的数量
     * @param req
     * @return 返回所有实例数据的数量
     */
    List<CmdbConfig> listWithPage(CmdbConfigRequest req);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbConfig get(CmdbConfig entity);

    /**
     * 根据配置编码 获取配置数据信息
     * @param configCode 配置编码
     * @return
     */
    CmdbConfig getByConfigCode(@Param("configCode") String configCode);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbConfig entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbConfig entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbConfig entity);
}