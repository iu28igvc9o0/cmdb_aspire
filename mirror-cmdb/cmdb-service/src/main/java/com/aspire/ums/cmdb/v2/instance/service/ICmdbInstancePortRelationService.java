package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-09-05 10:26:54
*/
public interface ICmdbInstancePortRelationService {

    /**
     * 获取当前资源池下所有ip
     * @param pool 资源池
     */
    List<Map<String, String>> getInstanceIpByPool(String pool);

    /**
     * 根据端口和id信息确认唯一
     * @param portRelation 查询参数
     */
    List<CmdbInstancePortRelation> selectByPortAndId(CmdbInstancePortRelation portRelation);
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
     Result<CmdbInstancePortRelation> list(CmdbInstancePortQuery instancePortQuery);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbInstancePortRelation get(CmdbInstancePortRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbInstancePortRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbInstancePortRelation entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbInstancePortRelation entity);

    /**
     * 批量新增
     * @param entitys 对象集合
     */
    void insertByBatch(List<CmdbInstancePortRelation> entitys);
}