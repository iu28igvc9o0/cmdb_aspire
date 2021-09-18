package com.aspire.ums.cmdb.v2.process.service;

import com.aspire.ums.cmdb.process.payload.CmdbProcessHandlerManager;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-04-14 09:08:55
*/
public interface ICmdbProcessHandlerManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbProcessHandlerManager> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbProcessHandlerManager get(CmdbProcessHandlerManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbProcessHandlerManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbProcessHandlerManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbProcessHandlerManager entity);
}