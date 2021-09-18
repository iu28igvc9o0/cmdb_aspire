package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao <br>
 * 类名称: RoleSchemaDao.java <br>
 * 类描述: 资源模式DAO接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午11:03:31<br>
 * 版本: v1.0
 */
@Mapper
public interface ResourceSchemaDao {

    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 集合
     */
    public List<ResourceSchema> fetchResourceSchemaList();

    /**fetchChildrenResourceSchemaList
     * 获取子级列表资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 集合
     */
    public List<ResourceSchema> fetchChildrenResourceSchemaList(@Param("parentResource")String parentResource);

    /**
     * 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
     * @param resourceType 资源类型
     * @return 对象
     */
    public ResourceSchema fetchResourceSchemaDetail(@Param("resourceType")String resourceType,@Param("actionType")String actionType);
    

    void insert(ResourceSchema resourceSchema);

    void updateName(ResourceSchema resourceSchema);

    ResourceSchema selectByResource(String resource);

    void deleteByResource(String resource);
}
