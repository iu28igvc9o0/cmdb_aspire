package com.migu.tsg.microservice.atomicservice.rbac.dao;


import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassifyPageConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author menglinjie
 */
@Mapper
public interface UserClassifyPageConfigDao {
    int deleteByPrimaryKey(String uuid);

    int insert(UserClassifyPageConfig record);

    UserClassifyPageConfig selectByPrimaryKey(String uuid);

    List<UserClassifyPageConfig> selectAll();

    int updateByPrimaryKey(UserClassifyPageConfig record);

    void deleteByUserClassifyId(String userClassifyId);

    void updatePageConfig(@Param("customizedviewId")String customizedviewId, @Param("content")String content);
}