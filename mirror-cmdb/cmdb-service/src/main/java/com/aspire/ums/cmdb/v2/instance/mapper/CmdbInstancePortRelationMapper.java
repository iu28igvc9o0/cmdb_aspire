package com.aspire.ums.cmdb.v2.instance.mapper;

import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-09-05 10:26:54
*/
@Mapper
public interface CmdbInstancePortRelationMapper {

    /**
     * 根据资源池获取ci信息
     */
    List<Map<String, String>> getInstanceIpByPool(@Param("idcType") String pool);

    /**
     * 根据端口和id信息确认唯一
     */
    List<CmdbInstancePortRelation> selectByPortAndId(CmdbInstancePortRelation portRelation);
    /**
     * 获取所有实例数量
     * @return 返回所有实例数据
     */
    Integer listCount(CmdbInstancePortQuery entity);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbInstancePortRelation> listByEntity(CmdbInstancePortQuery entity);

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
     * @param entitys 实例对象集合
     */
    void insertByBatch(List<CmdbInstancePortRelation> entitys);
}