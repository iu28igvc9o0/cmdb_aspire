package com.aspire.ums.cmdb.v3.module.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroup;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroupRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
@Mapper
public interface CmdbV3ModuleGroupMapper {

    /**
     * 获取模型分组列表
     */
    List<CmdbV3ModuleGroup> getModuleGroupList(@Param("moduleId") String moduleId);

    /**
     * 获取父分组下所有的分组信息
     * @param parentGroupId 父分组ID
     * @return 子模型分组列表
     */
    List<CmdbV3ModuleGroup> getChildModuleGroupList(@Param("moduleId") String moduleId,
                                                    @Param("tableHeaderCode") String tableHeaderCode,
                                                    @Param("parentGroupId") String parentGroupId);

    /**
     * 获取分组下所有的码表配置信息
     * @param groupId 分组ID
     * @return 所有的分组码表关系
     */
    List<CmdbV3ModuleGroupRelation> getAllGroupCodeList(@Param("groupId") String groupId);

//     /**
//     * 获取所有实例
//     * @return 返回所有实例数据
//     */
//    List<CmdbV3ModuleGroup> list();
//
//    /**
//     * 获取所有实例
//     * @param entity 实例信息
//     * @return 返回所有实例数据
//     */
//    List<CmdbV3ModuleGroup> listByEntity(CmdbV3ModuleGroup entity);
//
//    /**
//     * 根据主键ID 获取数据信息
//     * @param entity 实例信息
//     * @return 返回实例信息的数据信息
//     */
//    CmdbV3ModuleGroup get(CmdbV3ModuleGroup entity);
//
//    /**
//     * 新增实例
//     * @param entity 实例数据
//     * @return
//     */
//    void insert(CmdbV3ModuleGroup entity);
//
//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    void update(CmdbV3ModuleGroup entity);
//
//    /**
//     * 删除实例
//     * @param entity 实例数据
//     * @return
//     */
//    void delete(CmdbV3ModuleGroup entity);
}