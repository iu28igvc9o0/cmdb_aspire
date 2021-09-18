package com.aspire.ums.cmdb.demand.mapper;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:20
*/
@Mapper
public interface CmdbDemandResourceTypeMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandResourceType> list();

    /**
     * 根据主键ID 获取数据信息
     * @param parentTypeId 父节点ID
     * @return 返回实例信息的数据信息
     */
    List<CmdbDemandResourceType> getTypeList(@Param(value = "parentTypeId") String parentTypeId);
    
    /**
     * 根据父节点ID 获取数据信息
     * @param parentTypeId 父节点ID 不能为空
     * @return 返回实例信息的数据信息
     */
    List<Map<String, String>> getTypeAndValueList(@Param(value = "parentTypeId") String parentTypeId);
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDemandResourceType get(CmdbDemandResourceType entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDemandResourceType entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDemandResourceType entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDemandResourceType entity);
    
    String getIdByCode(@Param("code") String code);

    /**
     * 获取所有的大分类
     * @return
     */
    List<CmdbDemandResourceType> getResourceOwnerList();

    /**
     * 根据大分类 获取所有小分类
     * @param owner
     * @return
     */
    List<CmdbDemandResourceType> getTypeListByOwner(@Param("owner") String owner);
}