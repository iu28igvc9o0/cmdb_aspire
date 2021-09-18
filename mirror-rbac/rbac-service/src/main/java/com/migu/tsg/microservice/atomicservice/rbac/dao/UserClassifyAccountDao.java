package com.migu.tsg.microservice.atomicservice.rbac.dao;


import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassifyAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author menglinjie
 */
@Mapper
public interface UserClassifyAccountDao {
    int insert(UserClassifyAccount record);

    List<UserClassifyAccount> selectAll();

    void saveList(List<UserClassifyAccount> userClassifyAccountList);

    void deleteByUserClassifyId(String userClassifyId);

    List<UserClassifyAccount> findByUserId(String userId);

    void insertBatch(List<UserClassifyAccount> classList);
}