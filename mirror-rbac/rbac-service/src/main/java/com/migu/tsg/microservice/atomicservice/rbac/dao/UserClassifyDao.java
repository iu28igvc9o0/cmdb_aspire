package com.migu.tsg.microservice.atomicservice.rbac.dao;


import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassify;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author menglinjie
 */
@Mapper
public interface UserClassifyDao {
    int deleteByPrimaryKey(String uuid);

    int insert(UserClassify record);

    UserClassify selectByPrimaryKey(String uuid);

    List<UserClassify> selectAll();

    int updateByPrimaryKey(UserClassify record);

    List<Map> findListBySystemId(String systemId);

    List<UserClassifyReq > findListById(@Param("userClassifyIdList") List<String> userClassifyIdList);
}