package com.aspire.ums.cmdb.v2.module.mapper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper {

    /**
     * 查询所有module
     * @return
     */
    List<Module> getModuleList();
    /**
     * 查询模型树
     * @return
     */
    List<Module> getModuleTree(@Param("catalogId") String catalogId);

    /**
     * 根据id获取模型详情
     */
    Module getModuleDetail(@Param("moduleId") String moduleId);

    /**
     * 根据id获取模型详情
     */
    SimpleModule getSimpleModuleDetail(@Param("moduleId") String moduleId);

    /**
     * 根据id获取模型详情
     */
    Module getModuleByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 根据信息获取模型
     */
    List<Module>  getModuleSelective(Module module);

    /**
     * 根据信息获取模型
     */
    Module  selectLimitSort(@Param("sortType") String sortType,
                            @Param("moduleId") String moduleId,
                            @Param("parentCatalogId") String parentCatalogId);
    /**
     * 新增模型
     * @param module 模型数据
     * @return
     */
    void insert (Module module);

    /**
     * 修改模型
     * @param module 模型数据
     * @return
     */
    void update (Module module);
    /**
     * 删除模型
     * @param moduleId 模型数据
     * @return
             */
    void delete (String moduleId);
    /**
     * 查询最大index
     * @return
     */
    int selectMaxIndex();

    /**
     * 根据模型分组找到对应的模型信息
     * @param catalogId 模型分组ID
     * @return
     */
    Module getModuleDetailByCatalogId(@Param("catalogId") String catalogId);

    /**
     * 根据模型分组找到对应的模型信息
     * @param catalogId 模型分组ID
     * @return
     */
    SimpleModule getSimpleModuleByCatalogId(@Param("catalogId") String catalogId);

    List<Module> getChildByCatalogId(@Param("catalogId") String catalogId);

    /**
     * 根据码表id从关系表中查出相关模型
     * @param codeId 模型分组ID
     * @return
     */
    List<Module> getModuleByCodeId(@Param("codeId") String codeId);

    /**
     * 根据模型ID查询引用模型
     * @param moduleId 模型分组ID
     * @return
     */
    List<SimpleModule> getRefModuleByModuleId(@Param("moduleId") String moduleId);

    /**
     * 根据引用模型ID查询模型
     * @param moduleId 模型分组ID
     * @return
     */
    List<SimpleModule> getModuleByRefModuleId(@Param("moduleId") String moduleId);

    /**
     * 查询当前模型的所有引用模型包括自己
     * @param moduleId 模型分组ID
     * @return
     */
    List<Module> getCurRefModule(@Param("moduleId") String moduleId);

    /**
     * 根据设备类型, 获取模型的分组编码和模型编码
     * @param deviceType 设备类型
     * @return
     */
    Map<String, String> getModuleCodeAndCatalogCodeByDeviceType(@Param("deviceType") String deviceType);

    /**
     * 获取模型及父模型信息
     */
    Map<String, Object> getParentInfo(@Param("moduleId") String moduleId);

    /**
     * 获取资源池中文名称
     * @param cnName 资源名称
     * @return
     */
    String getIdcTypeIdByCNName(@Param("cnName") String cnName);

    /**
     * 获取POD池中文名称
     * @param cnName 资源名称
     * @return
     */
    String getPodIdByCNName(@Param("cnName") String cnName);

    /**
     * 获取项目名称中文名称
     * @param cnName 资源名称
     * @return
     */
    String getProjectIdByCNName(@Param("cnName") String cnName);

    /**
     * 获取业务系统中文名称
     * @param cnName 资源名称
     * @return
     */
    String getBizSystemIdByCNName(@Param("cnName") String cnName);

    Map<String, Object> getModuleIdByDeviceType(@Param("deviceType") String deviceType);

    Map<String, Object> getModuleIdByDeviceClass(@Param("deviceClass") String deviceClass);

    /**
     * 根据父模型ID, 获取子模型集合
     * @param parentModuleId 父模型ID
     * @return
     */
    List<Map<String, Object>> getChildModuleListByParentModuleId(@Param("parentModuleId") String parentModuleId);

    /**
     * 获取指定模型中, 依赖模型的数量
     * @param moduleId 模型ID
     * @return
     */
    Integer getOwnerModuleCount(@Param("moduleId") String moduleId);
}
