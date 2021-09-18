package com.aspire.ums.cmdb.v3.condication.mapper;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Mapper
public interface CmdbV3CondicationSettingRelationMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSettingRelation> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSettingRelation> listByEntity(CmdbV3CondicationSettingRelation entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationSettingRelation get(CmdbV3CondicationSettingRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CondicationSettingRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CondicationSettingRelation entity);

    /**
     * 删除实例
     * @param relationId 关系ID
     * @return
     */
    void deleteById(@Param("id") String relationId);

    /**
     * 删除实例
     * @param condicationSettingId 查询条件ID
     * @return
     */
    void deleteByCondicationSettingId(@Param("condicationSettingId") String condicationSettingId);

    /**
     * 根据查询条件ID 查询配置关系
     * @param condicationId 查询ID
     * @return
     */
    List<CmdbV3CondicationSettingRelation> getSettingRelationByCondicationId(@Param("condicationId") String condicationId);

    String findContainCodeIdByCode(Map<String,String> param);

    String findCmdbCodeByIdList(@Param("list") List<String> list);
}