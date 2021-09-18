package com.aspire.ums.cmdb.v3.screen.mapper;

import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:19
*/
@Mapper
public interface CmdbScreenProblemInfoMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbScreenProblemInfo> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbScreenProblemInfo> listByEntity(CmdbScreenProblemInfoRequest entity);

    /**
     * 获取查询实例数量
     * @return 返回所有实例数据
     */
    int listByEntityCount(CmdbScreenProblemInfoRequest entity);


    /**
     * 根据主键ID 获取数据信息
     * @param id 实例ID
     * @return 返回实例信息的数据信息
     */
    CmdbScreenProblemInfo get(@Param("id") String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbScreenProblemInfo entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbScreenProblemInfo entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbScreenProblemInfo entity);
}