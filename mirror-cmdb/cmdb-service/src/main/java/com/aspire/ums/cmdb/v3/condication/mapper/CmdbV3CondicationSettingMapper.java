package com.aspire.ums.cmdb.v3.condication.mapper;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Mapper
public interface CmdbV3CondicationSettingMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSetting> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSetting> listByEntity(CmdbV3CondicationSetting entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationSetting get(CmdbV3CondicationSetting entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CondicationSetting entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CondicationSetting entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CondicationSetting entity);

    /**
     * 获取符合查询条件的查询条件配置数量
     * @param settingQuery 实例数据
     * @return
     */
    Integer getCondicationSettingListCount(CmdbV3CondicationSettingQuery settingQuery);

    /**
     * 获取符合查询条件的查询条件配置
     * @param settingQuery 实例数据
     * @return
     */
    List<CmdbV3CondicationSetting> getCondicationSettingList(CmdbV3CondicationSettingQuery settingQuery);

    /**
     * 根据码表ID, 查询所有的查询条件
     * @param codeId 编码ID
     * @return
     */
    List<CmdbV3CondicationSetting> getConditionListByCodeId(@Param("codeId") String codeId);

    /**
     * 验证条件编码的唯一 以及 条件编码和条件地址的唯一性
     * @param code 配置编码
     * @param name 配置地址
     * @return
     */
    Integer validConditionUnique(@Param("code") String code,@Param("name") String name);
}