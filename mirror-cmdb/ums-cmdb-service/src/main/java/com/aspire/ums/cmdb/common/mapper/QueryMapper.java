package com.aspire.ums.cmdb.common.mapper;

import com.aspire.ums.cmdb.common.entity.QueryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QueryMapper {

    /**
     * 根据模型ID和用户 获取用户定义的所有自定义查询条件
     * @param moduleId 模型ID
     * @param user 用户
     * @return 自定义条件列表
     */
    List<QueryEntity> getQueryConditionByModuleIdAndUserAndMenuType(@Param("moduleId") String moduleId,
                                                         @Param("user") String user,
                                                         @Param("menuType") String menuType);

    /**
     * 新增自定义条件
     * @param queryEntity 自定义条件VO
     */
    void insertVO(QueryEntity queryEntity);

    /**
     * 修改自定义条件
     * @param queryEntity 自定义条件VO
     */
    void updateVO(QueryEntity queryEntity);

    /**
     * 删除自定义条件
     * @param queryEntity 自定义条件VO
     */
    void deleteVO(QueryEntity queryEntity);
}