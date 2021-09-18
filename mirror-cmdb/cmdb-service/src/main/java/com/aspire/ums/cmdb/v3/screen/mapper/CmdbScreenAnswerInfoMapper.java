package com.aspire.ums.cmdb.v3.screen.mapper;

import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:18
*/
@Mapper
public interface CmdbScreenAnswerInfoMapper {

    /*
    *  依据问题ID,查询回答列表，用于一对多的查询关联
    * */
    List<CmdbScreenAnswerInfo> listByProblemId(@Param("problemId") String problemId);

     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbScreenAnswerInfo> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbScreenAnswerInfo> listByEntity(CmdbScreenAnswerInfo entity);

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
    void insert(CmdbScreenAnswerInfo entity);

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