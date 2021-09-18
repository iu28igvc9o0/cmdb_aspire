package com.migu.tsg.microservice.atomicservice.composite.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.ResourceProject;

/**
 * 
 * 数据库表Resources_resource的DAO操作
 * Project Name:composite-service
 * File Name:ResourceProjectDao.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.dao
 * ClassName: ResourceProjectDao <br/>
 * date: 2017年10月3日 下午5:31:37 <br/>
 * 数据库表Resources_resource的DAO操作
 * @author baiwp
 * @version 
 * @since JDK 1.6
 */
@Mapper
public interface ResourceProjectDao {

    void removeResourceProjectByProjectUuid(@Param("projectUuid") String projectUuid,
                                            @Param("resourceUuid") String resourceUuid);
    
    void insertResourceProject(ResourceProject... resProjArr);
}
