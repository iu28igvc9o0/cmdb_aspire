package com.aspire.mirror.template.server.dao;

import java.util.List;

import com.aspire.mirror.template.api.dto.vo.StandardDynamicModel;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModelExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Triggers;

/**
 * 触发器数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    TriggersDao.java
 * 类描述:    触发器数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface TriggersDao {

    /**
     * 新增数据
     *
     * @param triggers 触发器PO对象
     * @return int 新增数据条数
     */
    int insert(Triggers triggers);

    /**
     * 批量新增触发器数据
     *
     * @param listTriggers 触发器PO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Triggers> listTriggers);
    /**
     * 根据主键删除数据
     *
     * @param triggerId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "triggerId") String triggerId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param triggerIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] triggerIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param triggers  触发器PO对象
     * @return int 删除数据条数
     */
    int delete(Triggers triggers);

    /**
     * 根据参数选择性更新数据
     *
     * @param triggers 触发器PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Triggers triggers);

    /**
     * 根据主键更新数据
     *
     * @param triggers 触发器PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Triggers triggers);

    /**
     * 根据主键查询
     *
     * @param triggerId 主键
     * @return Triggers 返回对象
     */
    Triggers selectByPrimaryKey(@Param(value = "triggerId") String triggerId);

    /**
     * 根据主键数组查询
     *
     * @param triggerIdArrays 主键数组
     * @return List<Triggers> 返回集合对象
     */
    List<Triggers> selectByPrimaryKeyArrays(String[] triggerIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param triggers 触发器PO对象
     * @return List<Triggers>  返回集合
     */
    List<Triggers> select(Triggers triggers);

    /**
     * 根据po实体查询条数
     *
     * @param triggers 触发器PO对象
     * @return int 数据条数
     */
    int selectCount(Triggers triggers);

    /**
     * 根据监控项ID集合删除触发器数据
     * @param itemIdArrays 监控项ID集合
     */
    int deleteByItemIds(String[] itemIdArrays);

    /**
     * 根据模板ID集合删除触发器数据
     * @param templateIdArrays 模板ID集合
     * @return 影响数据条数
     */
    int deleteByTemplateIdArrays(String[] templateIdArrays);

    /**
     * 根据监控项ID集合查询
     * @param itemIdArrays 控项ID集合
     * @return 结果集
     */
    List<Triggers> selectByItemIdArrays(String[] itemIdArrays);

    /**
     * 根据模板ID查询触发器列表
     * @param templateId
     * @return 触发器列表
     */
    List<Triggers> listByTemplateId(@Param(value = "templateId") String templateId);

    void updateExpressionByItemIdionByItemId(Triggers triggers);

    List<Triggers> selectDynamicModelTriggerBySpecialParam(@Param("thridSystemId") String thridSystemId, @Param("zabbixItemId") String zabbixItemId, @Param("priority") String priority);

    void insertDynamicModel(StandardDynamicModel standardDynamicModel);

    void insertBatchDynamicModel(List<StandardDynamicModel> standardDynamicModel);

    StandardDynamicModel selectDynamicModel(@Param("triggerId") String triggerId, @Param("deviceItemId") String deviceItemId);

    void updateDynamicModelById(StandardDynamicModel standardDynamicModel);

    StandardDynamicModelExt selectDynamicModelExtByModelIds(@Param("modelIds") String modelIds);

    void updateDynamicModelExtById(StandardDynamicModelExt standardDynamicModelExt);

    void insertBatchDynamicModelExt(List<StandardDynamicModelExt> insertModelExtList);

    List<StandardDynamicModel> selectDynamicModelByModelIdArray(String[] modelIdArray);

    List<StandardDynamicModel> selectDynamicModelByModelIdArrayAndProxyIdentity(@Param("modelIdArray") String[] modelIdArray, @Param("thridSystemId") String thridSystemId);

    List<Triggers> selectByPrimaryKeyArraysAndProxyIdentity(@Param("triggerIdArray") String[] triggerIdArray, @Param("proxyIdentity") String proxyIdentity);

    List<String> selecAlltDynamicModelId();

    void deleteDynamicModelByModelIdList(List<String> existModelIdList);
}
