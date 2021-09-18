package com.aspire.ums.cmdb.v3.module.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleAuthorization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Mapper
public interface CmdbV3ModuleAuthorizationMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleAuthorization> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleAuthorization> listByEntity(CmdbV3ModuleAuthorization entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleAuthorization get(CmdbV3ModuleAuthorization entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3ModuleAuthorization entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3ModuleAuthorization entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3ModuleAuthorization entity);
}