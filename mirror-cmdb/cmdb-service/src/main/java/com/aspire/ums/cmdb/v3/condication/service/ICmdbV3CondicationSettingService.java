package com.aspire.ums.cmdb.v3.condication.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
public interface ICmdbV3CondicationSettingService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSetting> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationSetting get(CmdbV3CondicationSetting entity);

    /**
     * 根据配置编码及接入用户获取条件配置信息
     * @param indicationCode 配置编码
     * @param accessUserId 接入用户ID
     * @return
     */
    Map<String, Object> getSettingByCodeAndAccessUserId(String indicationCode, String accessUserId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    Map<String, String> insert(CmdbV3CondicationSetting entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    Map<String, String> update(CmdbV3CondicationSetting entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    Map<String, String> delete(CmdbV3CondicationSetting entity);

    /**
     * 查询条件列表
     * @param settingQuery
     * @return
     */
    Result<CmdbV3CondicationSetting> getCondicationSettingList(CmdbV3CondicationSettingQuery settingQuery);

    /**
     * 根据码表ID, 查询所有的查询条件
     * @param codeId 编码ID
     * @return
     */
    List<CmdbV3CondicationSetting> getConditionListByCodeId(String codeId);

    /**
     * 转化查询条件
     * @param params 查询条件
     * @return
     */
    Map<String, Object> parseQuery(Map<String, Object> params);

    /**
     * 验证条件编码的唯一 以及 条件编码和条件地址的唯一性
     * @param code 配置编码
     * @param name 配置地址
     * @return
     */
    JSONObject validConditionUnique(String code,String name);
}