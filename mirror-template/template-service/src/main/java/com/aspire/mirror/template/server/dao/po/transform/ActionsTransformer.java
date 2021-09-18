package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Actions;
import com.aspire.mirror.template.api.dto.model.ActionsDTO;

import java.util.List;
import java.util.Map;

/**
 * 动作对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    ActionsTransformer.java
 * 类描述:    动作对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class ActionsTransformer {

    private ActionsTransformer() {
    }

    /**
     * 将动作PO实体转换为动作DTO实体
     *
     * @param actions 动作PO实体
     * @return ActionsDTO 动作DTO实体
     */
    public static ActionsDTO fromPo(final Actions actions) {
        if (null == actions) {
            return null;
        }

        ActionsDTO actionsDTO = new ActionsDTO();
        actionsDTO.setActionId(actions.getActionId());
        actionsDTO.setName(actions.getName());
        actionsDTO.setEventSource(actions.getEventSource());
        actionsDTO.setEvalType(actions.getEvalType());
        actionsDTO.setStatus(actions.getStatus());
        actionsDTO.setType(actions.getType());
        actionsDTO.setDealer(actions.getDealer());
        actionsDTO.setTriggerId(actions.getTriggerId());
        actionsDTO.setEventType(actions.getEventType());

        return actionsDTO;
    }

    /**
     * 将动作业务实体对象集合转换为动作持久化对象集合
     *
     * @param listActions 动作业务实体对象集合
     * @return List<ActionsDTO> 动作持久化对象集合
     */
    public static List<ActionsDTO> fromPo(final List<Actions> listActions) {
        if (CollectionUtils.isEmpty(listActions)) {
            return Lists.newArrayList();
        }
        List<ActionsDTO> listActionsDTO = Lists.newArrayList();

        for (Actions actions : listActions) {
            listActionsDTO.add(ActionsTransformer.fromPo(actions));
        }
        return listActionsDTO;
    }

    /**
     * 将动作DTO实体转换为动作PO实体
     *
     * @param actionsDTO 动作DTO实体类
     * @return Actions 动作PO实体
     */
    public static Actions toPo(final ActionsDTO actionsDTO) {
        if (null == actionsDTO) {
            return null;
        }

        Actions actions = new Actions();
        actions.setActionId(actionsDTO.getActionId());
        actions.setName(actionsDTO.getName());
        actions.setEventSource(actionsDTO.getEventSource());
        actions.setEvalType(actionsDTO.getEvalType());
        actions.setStatus(actionsDTO.getStatus());
        actions.setType(actionsDTO.getType());
        actions.setDealer(actionsDTO.getDealer());
        actions.setTriggerId(actionsDTO.getTriggerId());
        actions.setEventType(actionsDTO.getEventType());

        return actions;
    }

    /**
     * 将动作业务实体对象集合转换为动作持久化对象集合
     *
     * @param listActionsDTO 动作业务实体对象集合
     * @return List<Actions> 动作持久化对象集合
     */
    public static List<Actions> toPo(final List<ActionsDTO> listActionsDTO) {
        if (CollectionUtils.isEmpty(listActionsDTO)) {
            return Lists.newArrayList();
        }
        List<Actions> listActions = Lists.newArrayList();

        for (ActionsDTO actionsdTO : listActionsDTO) {
            listActions.add(ActionsTransformer.toPo(actionsdTO));
        }
        return listActions;
    }

    /**
     * 将动作业务实体对象集合转换为Map
     *
     * @param listActionsDTO 动作业务实体对象集合
     * @return Map<String ,   ActionsDTO> Map[key=String|value=ActionsDTO]
     */
    public static Map<String, ActionsDTO> toDTOMap(final List<ActionsDTO> listActionsDTO) {
        if (CollectionUtils.isEmpty(listActionsDTO)) {
            return Maps.newHashMap();
        }
        Map<String, ActionsDTO> actionsDTOMaps = Maps.newHashMap();

        for (ActionsDTO actionsDTO : listActionsDTO) {
            actionsDTOMaps.put(actionsDTO.getActionId(), actionsDTO);
        }
        return actionsDTOMaps;
    }

    /**
     * 将动作业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listActionsDTO 动作业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ActionsDTO> listActionsDTO) {
        if (CollectionUtils.isEmpty(listActionsDTO)) {
            return null;
        }
        int size = listActionsDTO.size();
        String[] actionIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            actionIdArrays[i] = listActionsDTO.get(i).getActionId();
        }
        return actionIdArrays;
    }
} 
