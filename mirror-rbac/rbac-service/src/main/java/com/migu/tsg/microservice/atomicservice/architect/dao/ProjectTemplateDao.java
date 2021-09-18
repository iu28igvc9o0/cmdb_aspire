package com.migu.tsg.microservice.atomicservice.architect.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplate;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao <br>
* 类名称: ProjectTemplateDao.java <br>
* 类描述: 项目模版持久层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午4:09:21 <br>
* 版本: v1.0
*/
@Mapper
public interface ProjectTemplateDao {

    /**
     * 查询项目模板集合
     * @param uuidList UUID集合
     * @return 项目模板集合
     */
    public List<ProjectTemplate> listProjectTemplateByUuids(@Param("uuidList") List<String> uuidList);
    
    /**
     * 根据模板名称查询项目数量
     * @param name 项目模板名称
     * @return 总数
     */
    public int countProjectTemplateByName(String name);
    
    /**
     * 查询单个项目模板对象
     * @param name 项目模板名称
     * @return 项目模板对象
     */
    public ProjectTemplate getProjectTemplateByName(String name);
    
    /**
     * 删除项目模板
     * @param uuid 项目模板UUID
     * @return 影响行数
     */
    public int deleteProjectTemplateByUuid(String uuid);
    
    /**
     * 创建项目模板
     * @param projectTemplate 项目模板对象
     * @return 影响行数
     */
    public int insertProjectTemplate(ProjectTemplate projectTemplate);

}
