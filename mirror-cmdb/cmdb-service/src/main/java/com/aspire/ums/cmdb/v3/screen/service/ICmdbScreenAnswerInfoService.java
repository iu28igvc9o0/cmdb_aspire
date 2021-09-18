package com.aspire.ums.cmdb.v3.screen.service;

import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:18
*/
public interface ICmdbScreenAnswerInfoService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbScreenAnswerInfo> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbScreenAnswerInfo get(CmdbScreenAnswerInfo entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> insert(CmdbScreenAnswerInfo entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbScreenAnswerInfo entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbScreenAnswerInfo entity);
}