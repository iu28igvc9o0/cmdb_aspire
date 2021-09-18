package com.aspire.ums.cmdb.v3.config.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-05-06 10:54:58
*/
public interface ICmdbConfigService {
    /**
     * 获取所有实例分页
     * @return 返回所有实例数据
     */
    Result<CmdbConfig> listByPage(CmdbConfigRequest req);

     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbConfig> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbConfig get(CmdbConfig entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> insert(CmdbConfig entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> update(CmdbConfig entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> delete(CmdbConfig entity);

    /**
     * 根据配置编码获取配置信息
     * @param configCode 配置编码
     * @return
     */
    CmdbConfig getConfigByCode(String configCode);

    /**
     * 根据配置编码获取配置信息
     * @param configCode 配置编码
     * @param defaultValue 默认值
     * @return
     */
    CmdbConfig getConfigByCode(String configCode, Object defaultValue);
}
