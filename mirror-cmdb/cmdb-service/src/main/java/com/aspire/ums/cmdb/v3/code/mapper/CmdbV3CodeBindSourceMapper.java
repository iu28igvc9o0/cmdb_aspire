package com.aspire.ums.cmdb.v3.code.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Mapper
public interface CmdbV3CodeBindSourceMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeBindSource> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeBindSource> listByEntity(CmdbV3CodeBindSource entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeBindSource get(CmdbV3CodeBindSource entity);

    /**
     * 根据主键ID 获取数据信息
     * @param codeId 码表ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeBindSource getByCodeId(@Param("codeId") String codeId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeBindSource entity);
    /**
     * 新增实例
     * @param bindSources 实例数据
     * @return
             */
    void insertByBatch(List<CmdbV3CodeBindSource> bindSources);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeBindSource entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeBindSource entity);

    /**
     * 删除实例
     * @param codeId 码表ID
     * @return
     */
    void deleteByCodeId(@Param("codeId") String codeId);
}