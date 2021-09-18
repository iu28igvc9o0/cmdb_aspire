package com.aspire.mirror.template.server.biz;

import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.ItemSyncRequest;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModel;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModelExt;

import java.util.List;

/**
 * 触发器业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   TriggersBiz.java
 * 类描述:   触发器业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface TriggersBiz {

    /**
     * 新增数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 新增数据条数
     */
//    int insert(TriggersDTO triggersDTO);

    /**
     * 批量新增触发器数据
     *
     * @param listTriggersDTO 触发器DTO集合对象
     * @return list 新增数据
     */
    List<TriggersDTO> insertByBatch(List<TriggersDTO> listTriggersDTO);
    /**
     * 根据主键删除数据
     *
     * @param triggerId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String triggerId);

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
     * @param triggersDTO  触发器DTO对象
     * @return int 删除数据条数
     */
    int delete(TriggersDTO triggersDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
//    int updateByPrimaryKeySelective(TriggersDTO triggersDTO);

    /**
     * 根据主键更新数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TriggersDTO triggersDTO);

    /**
     * 根据主键查询
     *
     * @param triggerId 主键
     * @return TriggersDTO 返回对象
     */
    TriggersDTO selectByPrimaryKey(String triggerId);

    /**
     * 根据主键数组查询
     *
     * @param triggerIdArrays 主键数组
     * @return List<TriggersDTO> 返回集合对象
     */
    List<TriggersDTO> selectByPrimaryKeyArrays(String[] triggerIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param triggersDTO 触发器DTO对象
     * @return List<Triggers>  返回集合
     */
    List<TriggersDTO> select(TriggersDTO triggersDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
    int selectCount(TriggersDTO triggersDTO);

    /**
     * 根据监控项ID删除触发器
     * @param itemIdArrays
     * @return 受影响数据条数
     */
    int deleteByItemIdArrays(String[] itemIdArrays);

    /**
     * 根据监控项ID集合查询
     * @param itemIdArrays 监控项集合
     * @return 结果集
     */
    List<TriggersDTO> listByItemIdArrays(String[] itemIdArrays);

    /**
     * 根据模板ID查询触发器信息
     * @param templateId
     * @return
     */
    List<TriggersDTO> listByTemplateId(String templateId);

    void updateExpressionByItemIdionByItemId(TriggersDTO triggersDTO);

    GeneralResponse batchDynamicModelData(List<StandardDynamicModelExt> standardDynamicModelExtList, String indexType);

    List<StandardDynamicModel> selectDynamicModelByModelIdArrayAndProxyIdentity(String[] modelIdArray, String proxyIdentity);

    GeneralResponse zabbixItemAndPrototypeRelationSync(ItemSyncRequest itemSyncRequest);

    List<TriggersDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] triggerIdArray, String proxyIdentity);
}
