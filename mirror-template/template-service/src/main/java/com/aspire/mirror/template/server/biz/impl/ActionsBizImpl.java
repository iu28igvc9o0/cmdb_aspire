package com.aspire.mirror.template.server.biz.impl;

import com.aspire.mirror.template.api.dto.model.ActionsDTO;
import com.aspire.mirror.template.server.biz.ActionsBiz;
import com.aspire.mirror.template.server.dao.ActionsDao;
import com.aspire.mirror.template.server.dao.po.Actions;
import com.aspire.mirror.template.server.dao.po.transform.ActionsTransformer;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 动作业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    ActionsBizImpl.java
 * 类描述:    动作业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class ActionsBizImpl implements ActionsBiz {

    /**
     * 新增数据
     *
     * @param actionsDTO 动作DTO对象
     * @return String 数据ID
     */
    public String insert(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.error("method[insert] param[actionsDTO] is null");
            throw new RuntimeException("param[actionsDTO] is null");
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        String actionId = UUID.randomUUID().toString();
        actions.setActionId(actionId);
        actionsDao.insert(actions);
        return actionId;
    }

    /**
     * 批量新增动作数据
     *
     * @param listActionsDTO 动作DTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<ActionsDTO> listActionsDTO) {
        if (CollectionUtils.isEmpty(listActionsDTO)) {
            LOGGER.error("method[insertByBatch] param[listActionsDTO] is null");
            throw new RuntimeException("param[listActionsDTO] is null");
        }
        List<Actions> listActions = ActionsTransformer.toPo(listActionsDTO);
        return actionsDao.insertByBatch(listActions);
    }

    /**
     * 根据主键删除数据
     *
     * @param actionId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String actionId) {
        if (StringUtils.isEmpty(actionId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[actionId] is null");
            throw new RuntimeException("param[actionId] is null");
        }
        return actionsDao.deleteByPrimaryKey(actionId);
    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param actionIdArrays 主键数组
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKeyArrays(final String[] actionIdArrays) {
        if (ArrayUtils.isEmpty(actionIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[actionIdArrays] is null");
            throw new RuntimeException("param[actionIdArrays] is null");
        }
        return actionsDao.deleteByPrimaryKeyArrays(actionIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 删除数据条数
     */
    public int delete(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.error("method[delete] param[actionsDTO] is null");
            throw new RuntimeException("param[actionsDTO] is null");
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        return actionsDao.delete(actions);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[actionsDTO] is null");
            throw new RuntimeException("param[actionsDTO] is null");
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        return actionsDao.updateByPrimaryKeySelective(actions);
    }

    /**
     * 根据主键更新数据
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[actionsDTO] is null");
            throw new RuntimeException("param[actionsDTO] is null");
        }
        if (StringUtils.isEmpty(actionsDTO.getActionId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[actionId] is null");
            throw new RuntimeException("param[actionId] is null");
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        return actionsDao.updateByPrimaryKey(actions);
    }

    /**
     * 根据主键查询
     *
     * @param actionId 主键
     * @return ActionsDTO 返回对象
     */
    public ActionsDTO selectByPrimaryKey(final String actionId) {
        Actions actions = actionsDao.selectByPrimaryKey(actionId);
        if (StringUtils.isEmpty(actionId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[actionId] is null");
            return null;
        }
        return ActionsTransformer.fromPo(actions);
    }

    /**
     * 根据主键数组查询
     *
     * @param actionIdArrays 主键数组
     * @return List<ActionsDTO> 返回集合对象
     */
    public List<ActionsDTO> selectByPrimaryKeyArrays(final String[] actionIdArrays) {
        if (ArrayUtils.isEmpty(actionIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[actionIdArrays] is null");
            return Collections.emptyList();
        }
        List<Actions> listActions = actionsDao.selectByPrimaryKeyArrays(actionIdArrays);
        return ActionsTransformer.fromPo(listActions);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param actionsDTO 动作DTO对象
     * @return List<Actions>  返回集合
     */
    public List<ActionsDTO> select(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.warn("select Object actionsDTO is null");
            return Collections.emptyList();
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        List<Actions> listActions = actionsDao.select(actions);
        return ActionsTransformer.fromPo(listActions);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param actionsDTO 动作DTO对象
     * @return int 数据条数
     */
    public int selectCount(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            LOGGER.warn("selectCount Object actionsDTO is null");
        }
        Actions actions = ActionsTransformer.toPo(actionsDTO);
        return actionsDao.selectCount(actions);
    }

    @Override
    public List<ActionsDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] actionIdArray, String proxyIdentity) {
        if (ArrayUtils.isEmpty(actionIdArray)) {
            LOGGER.warn("method[selectByPrimaryKeyArraysAndProxyIdentity] param[actionIdArray] is null");
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(proxyIdentity)) {
            LOGGER.warn("method[proxyIdentity] param[proxyIdentity] is null");
            return Collections.emptyList();
        }
        List<Actions> listActions = actionsDao.selectByPrimaryKeyArraysAndProxyIdentity(actionIdArray, proxyIdentity);
        return ActionsTransformer.fromPo(listActions);
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private ActionsDao actionsDao;

} 
