package com.migu.tsg.microservice.atomicservice.architect.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplateItem;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao <br>
* 类名称: ProjectTemplateItemDao.java <br>
* 类描述: 项目模版声明的资源持久层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午4:09:21 <br>
* 版本: v1.0
*/
@Mapper
public interface ProjectTemplateItemDao {

    /**
     * 删除项目模板声明的资源
     * @param templateUuid 项目模板UUID
     * @return 影响行数
     */
    public int deleteProjectTemplateItemByTemplateUuid(String templateUuid);

    /**
     * 批量创建项目模板声明的资源
     * @param projectTemplateItemList 项目模板对象
     * @return 影响行数
     */
    public int insertProjectTemplateItem(
            @Param("projectTemplateItemList") List<ProjectTemplateItem> projectTemplateItemList);

}
