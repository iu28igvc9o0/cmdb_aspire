package com.aspire.ums.cmdb.v3.authorization.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:17
*/
@Mapper
public interface CmdbV3AuthorizationMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3Authorization> list(@Param("authOwner") String authOwner);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3Authorization> listByEntity(CmdbV3Authorization entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3Authorization get(CmdbV3Authorization entity);

    /**
     * 根据主键ID 获取数据信息
     * @param moduleId 实例信息
     * @return 返回实例信息的数据信息
     */
    List<CmdbV3Authorization> getByModuleId(@Param("id") String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3Authorization entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3Authorization entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3Authorization entity);
}