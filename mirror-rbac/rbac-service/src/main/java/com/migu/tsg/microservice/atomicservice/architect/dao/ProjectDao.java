package com.migu.tsg.microservice.atomicservice.architect.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.architect.dao.po.Project;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao <br>
* 类名称: ProjectDao.java <br>
* 类描述: 项目持久层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午4:09:21 <br>
* 版本: v1.0
*/
@Mapper
public interface ProjectDao {

    /**
     * 查询项目集合
     * @param uuidList UUID集合
     * @return 项目集合
     */
    public List<Project> listProjectByUuids(@Param("uuidList") List<String> uuidList);
    
    
    /**
     * 查询单个项目详情
     * @param uuid 项目UUID
     * @return 项目对象
     */
    public Project getProjectByUuid(String uuid);
    
    /**
     * 查询项目数量
     * @param namespace 根账号
     * @param name 项目名称
     * @return 总数
     */
    public int countProjectByName(@Param("namespace") String namespace, @Param("name") String name);
    
    /**
     * 删除项目
     * @param uuid 项目UUID
     * @return 影响行数
     */
    public int deleteProjectByUuid(String uuid);
    
    /**
     * 修改项目
     * @param uuid 项目UUID
     * @return 影响行数
     */
    public int updateProjectByUuid(String uuid);
    
    /**
     * 创建项目
     * @param project 项目对象
     * @return 影响行数
     */
    public int insertProject(Project project);

}
