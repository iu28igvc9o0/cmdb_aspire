package com.aspire.mirror.template.server.biz;

import com.aspire.mirror.template.api.dto.model.ActionsDTO;

import java.util.List;

/**
 * 动作业务层接口
 * <p>
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   ActionsBiz.java
 * 类描述:   动作业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface ActionsBiz {

    /**
     * 新增数据
     *
     * @param actionsDTO 动作DTO对象
     * @return String 数据ID
     */
    String insert(ActionsDTO actionsDTO);

    /**
     * 批量新增动作数据
     *
     * @param listActionsDTO 动作DTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<ActionsDTO> listActionsDTO);

    /**
     * 根据主键删除数据
     *
     * @param actionId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String actionId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param actionIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] actionIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 删除数据条数
     */
    int delete(ActionsDTO actionsDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ActionsDTO actionsDTO);

    /**
     * 根据主键更新数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ActionsDTO actionsDTO);

    /**
     * 根据主键查询
     *
     * @param actionId 主键
     * @return ActionsDTO 返回对象
     */
    ActionsDTO selectByPrimaryKey(String actionId);

    /**
     * 根据主键数组查询
     *
     * @param actionIdArrays 主键数组
     * @return List<ActionsDTO> 返回集合对象
     */
    List<ActionsDTO> selectByPrimaryKeyArrays(String[] actionIdArrays);

    /**
     * 根据dto实体查询列表
     *
     * @param actionsDTO 动作DTO对象
     * @return List<Actions>  返回集合
     */
    List<ActionsDTO> select(ActionsDTO actionsDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    int selectCount(ActionsDTO actionsDTO);

    List<ActionsDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] actionIdArray, String proxyIdentity);
} 
