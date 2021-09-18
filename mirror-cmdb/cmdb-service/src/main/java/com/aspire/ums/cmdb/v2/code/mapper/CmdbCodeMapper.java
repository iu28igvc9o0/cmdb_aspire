package com.aspire.ums.cmdb.v2.code.mapper;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:38
*/
@Mapper
public interface CmdbCodeMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCode> list(CmdbCodeQuery query);

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    Integer listCount(CmdbCodeQuery query);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCode> listByEntity(CmdbCode entity);

    /**
     * 根据主键ID 获取数据信息
     * @param codeId 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCode getById(@Param("codeId") String codeId);

    /**
     * 根据主键ID 获取简单的码表信息
     * @param codeId 主键ID
     * @return
     */
    CmdbSimpleCode getSimpleCodeById(@Param("codeId") String codeId);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCode get(CmdbCode entity);

    CmdbSimpleCode getByEntity(CmdbCode entity);
    /**
     * 根据主键IDs数组 获取数据信息
     * @param ids 根据ids获取实例信息
     * @return 返回实例信息的数据信息
     */
    List<CmdbCode> listByIds(List<String> ids);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbCode entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbCode entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbCode entity);

    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbCode> getCodeListByModuleId(@Param("moduleId") String moduleId);



    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    CmdbSimpleCode getSimpleCodeByCodeAndModuleId(@Param("moduleId") String moduleId,@Param("filedCode") String filedCode);

    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbCode> getSelfCodeListByModuleId(@Param("moduleId") String moduleId);

    /**
     * 根据分组id 查询分组下所有的码表字段
     * @param groupId 分组id
     * @return
     */
    List<CmdbCode> getCodeListByGroupId(@Param("groupId") String groupId,
                                        @Param("moduleId") String moduleId);

    /**
     * 获取码表编码及名称集合
     * @return
     */
    List<Map<String, String>> getDistinctCodeList();

    /**
     * 根据实体类信息查询列表
     * @param cmdbCode 实体类
     * @return
     */
    List<CmdbSimpleCode> simpleCodeListByEntity(CmdbCode cmdbCode);

    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbSimpleCode> getSimpleCodeListByModuleId(@Param("moduleId") String moduleId);

    /**
     * 验证码表 编码和模型分组 的唯一性
     * @param filedCode 码表编码
     * @param moduleCatalogId 模型分组ID
     * @return
     */
    Integer validateCmdbCodeUnique(@Param("filedCode") String filedCode,
                                   @Param("moduleCatalogId") String moduleCatalogId);

    /**
     * 验证码表 编码和模型分组 的唯一性
     * @param codeId 码表编码
     * @return
     */
    LinkedList<CmdbCode> getCasParentCodes(@Param("codeId") String codeId);

    List<Map<String,String>> getCodeIdByNameAndModuleId(Map<String,Object> param);
}
