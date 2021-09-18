package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Triggers;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;

import java.util.List;
import java.util.Map;

/**
 * 触发器对象转换类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    TriggersTransformer.java
 * 类描述:    触发器对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class TriggersTransformer  {

    private TriggersTransformer(){
    }

   /**
    * 将触发器PO实体转换为触发器DTO实体
    *
    * @param  triggers 触发器PO实体
    * @return TriggersDTO 触发器DTO实体
    */
    public static TriggersDTO fromPo(final Triggers triggers) {
        if (null == triggers) {
            return null;
        }
        
        TriggersDTO triggersDTO = new TriggersDTO();
        triggersDTO.setTriggerId(triggers.getTriggerId());
        triggersDTO.setName(triggers.getName());
        triggersDTO.setExpression(triggers.getExpression());
        triggersDTO.setUrl(triggers.getUrl());
        triggersDTO.setStatus(triggers.getStatus());
        triggersDTO.setValue(triggers.getValue());
        triggersDTO.setPriority(triggers.getPriority());
        triggersDTO.setItemId(triggers.getItemId());
        triggersDTO.setParam(triggers.getParam());
        triggersDTO.setType(triggers.getType());
        return triggersDTO;
    }

    /**
     * 将触发器业务实体对象集合转换为触发器持久化对象集合
     *
     * @param listTriggers 触发器业务实体对象集合
     * @return List<TriggersDTO> 触发器持久化对象集合
     */
    public static List<TriggersDTO> fromPo(final List<Triggers> listTriggers) {
        if (CollectionUtils.isEmpty(listTriggers)) {
            return Lists.newArrayList();
        }
        List<TriggersDTO> listTriggersDTO = Lists.newArrayList();

        for (Triggers triggers : listTriggers) {
            listTriggersDTO.add(TriggersTransformer.fromPo(triggers));
        }
        return listTriggersDTO;
    }

   /**
    * 将触发器DTO实体转换为触发器PO实体
    *
    * @param  triggersDTO 触发器DTO实体类
    * @return Triggers 触发器PO实体
    */
    public static Triggers toPo(final TriggersDTO triggersDTO) {
        if (null == triggersDTO) {
            return null;
        }

        Triggers triggers = new Triggers();
        triggers.setTriggerId(triggersDTO.getTriggerId());
        triggers.setName(triggersDTO.getName());
        triggers.setExpression(triggersDTO.getExpression());
        triggers.setUrl(triggersDTO.getUrl());
        triggers.setStatus(triggersDTO.getStatus());
        triggers.setValue(triggersDTO.getValue());
        triggers.setPriority(triggersDTO.getPriority());
        triggers.setItemId(triggersDTO.getItemId());
        triggers.setParam(triggersDTO.getParam());
        triggers.setType(triggersDTO.getType());
        return triggers;
    }

    /**
     * 将触发器业务实体对象集合转换为触发器持久化对象集合
     *
     * @param listTriggersDTO 触发器业务实体对象集合
     * @return List<Triggers> 触发器持久化对象集合
     */
    public static List<Triggers> toPo(final List<TriggersDTO> listTriggersDTO) {
        if (CollectionUtils.isEmpty(listTriggersDTO)) {
            return Lists.newArrayList();
        }
        List<Triggers> listTriggers = Lists.newArrayList();

        for (TriggersDTO triggersdTO : listTriggersDTO) {
            listTriggers.add(TriggersTransformer.toPo(triggersdTO));
        }
        return listTriggers;
    }
    /**
     * 将触发器业务实体对象集合转换为Map
     *
     * @param listTriggersDTO 触发器业务实体对象集合
     * @return Map<String, TriggersDTO> Map[key=String|value=TriggersDTO]
     */
    public static Map<String, TriggersDTO> toDTOMap(final List<TriggersDTO> listTriggersDTO) {
        if (CollectionUtils.isEmpty(listTriggersDTO)) {
            return Maps.newHashMap();
        }
        Map<String, TriggersDTO> triggersDTOMaps = Maps.newHashMap();

        for (TriggersDTO triggersDTO : listTriggersDTO) {
            triggersDTOMaps.put(triggersDTO.getTriggerId(), triggersDTO);
        }
        return triggersDTOMaps;
    }

    /**
     * 将触发器业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listTriggersDTO 触发器业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<TriggersDTO> listTriggersDTO) {
        if (CollectionUtils.isEmpty(listTriggersDTO)) {
            return null;
        }
        int size = listTriggersDTO.size();
        String[] triggerIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            triggerIdArrays[i] = listTriggersDTO.get(i).getTriggerId();
        }
        return triggerIdArrays;
        }
} 
