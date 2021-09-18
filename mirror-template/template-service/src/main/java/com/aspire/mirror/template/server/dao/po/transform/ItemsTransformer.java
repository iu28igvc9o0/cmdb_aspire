package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Items;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 监控项对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    ItemsTransformer.java
 * 类描述:    监控项对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:47
 */
public final class ItemsTransformer {

    private ItemsTransformer() {
    }

    /**
     * 将监控项PO实体转换为监控项DTO实体
     *
     * @param items 监控项PO实体
     * @return ItemsDTO 监控项DTO实体
     */
    public static ItemsDTO fromPo(final Items items) {
        if (null == items) {
            return null;
        }

        ItemsDTO itemsDTO = new ItemsDTO();
        itemsDTO.setItemId(items.getItemId());
        itemsDTO.setName(items.getName());
        itemsDTO.setType(items.getType());
        itemsDTO.setTemplateId(items.getTemplateId());
        itemsDTO.setKey(items.getKey());
        itemsDTO.setDelay(items.getDelay());
        itemsDTO.setHistory(items.getHistory());
        itemsDTO.setStatus(items.getStatus());
        itemsDTO.setValueType(items.getValueType());
        itemsDTO.setUnits(items.getUnits());
        itemsDTO.setError(items.getError());
        itemsDTO.setDataType(items.getDataType());
        itemsDTO.setSysType(items.getSysType());
        itemsDTO.setCalcType(items.getCalcType());
        itemsDTO.setBizCalcExp(items.getBizCalcExp());
        itemsDTO.setBizCalcObj(items.getBizCalcObj());
        itemsDTO.setBizIsZero(items.getBizIsZero());
        itemsDTO.setBizIndex(items.getBizIndex());
        itemsDTO.setBizThemeId(items.getBizThemeId());
        itemsDTO.setBizGroup(items.getBizGroup());
        itemsDTO.setBizThemeExp(items.getBizThemeExp());
        itemsDTO.setGroupFlag(items.getGroupFlag());
        if (!StringUtils.isEmpty(items.getTemplateName())) {
            itemsDTO.setTemplateName(items.getTemplateName());
        }
        itemsDTO.setCreater(items.getCreater());
        itemsDTO.setCreateTime(items.getCreateTime());
        itemsDTO.setItemGroup(items.getItemGroup());
        itemsDTO.setZabbixItemId(items.getZabbixItemId());
        itemsDTO.setItemExt(items.getItemExt());
        return itemsDTO;
    }

    /**
     * 将监控项业务实体对象集合转换为监控项持久化对象集合
     *
     * @param listItems 监控项业务实体对象集合
     * @return List<ItemsDTO> 监控项持久化对象集合
     */
    public static List<ItemsDTO> fromPo(final List<Items> listItems) {
        if (CollectionUtils.isEmpty(listItems)) {
            return Lists.newArrayList();
        }
        List<ItemsDTO> listItemsDTO = Lists.newArrayList();

        for (Items items : listItems) {
            listItemsDTO.add(ItemsTransformer.fromPo(items));
        }
        return listItemsDTO;
    }

    /**
     * 将监控项DTO实体转换为监控项PO实体
     *
     * @param itemsDTO 监控项DTO实体类
     * @return Items 监控项PO实体
     */
    public static Items toPo(final ItemsDTO itemsDTO) {
        if (null == itemsDTO) {
            return null;
        }

        Items items = new Items();
        items.setItemId(itemsDTO.getItemId());
        items.setName(itemsDTO.getName());
        items.setType(itemsDTO.getType());
        items.setTemplateId(itemsDTO.getTemplateId());
        items.setPrototypeId(itemsDTO.getPrototypeId());
        items.setKey(itemsDTO.getKey());
        items.setDelay(itemsDTO.getDelay());
        items.setHistory(itemsDTO.getHistory());
        items.setStatus(itemsDTO.getStatus());
        items.setValueType(itemsDTO.getValueType());
        items.setUnits(itemsDTO.getUnits());
        items.setError(itemsDTO.getError());
        items.setDataType(itemsDTO.getDataType());
        items.setSysType(itemsDTO.getSysType());
        items.setCalcType(itemsDTO.getCalcType());
        items.setBizCalcExp(itemsDTO.getBizCalcExp());
        items.setBizCalcObj(itemsDTO.getBizCalcObj());
        items.setBizIsZero(itemsDTO.getBizIsZero());
        items.setBizIndex(itemsDTO.getBizIndex());
        items.setBizThemeId(itemsDTO.getBizThemeId());
        items.setBizGroup(itemsDTO.getBizGroup());
        items.setBizThemeExp(itemsDTO.getBizThemeExp());
        items.setGroupFlag(itemsDTO.getGroupFlag());
        items.setCreater(itemsDTO.getCreater());
        items.setCreateTime(itemsDTO.getCreateTime());
        items.setItemGroup(itemsDTO.getItemGroup());
        items.setZabbixItemId(itemsDTO.getZabbixItemId());
        items.setItemExt(itemsDTO.getItemExt());
        return items;
    }

    /**
     * 将监控项业务实体对象集合转换为监控项持久化对象集合
     *
     * @param listItemsDTO 监控项业务实体对象集合
     * @return List<Items> 监控项持久化对象集合
     */
    public static List<Items> toPo(final List<ItemsDTO> listItemsDTO) {
        if (CollectionUtils.isEmpty(listItemsDTO)) {
            return Lists.newArrayList();
        }
        List<Items> listItems = Lists.newArrayList();

        for (ItemsDTO itemsdTO : listItemsDTO) {
            listItems.add(ItemsTransformer.toPo(itemsdTO));
        }
        return listItems;
    }

    /**
     * 将监控项业务实体对象集合转换为Map
     *
     * @param listItemsDTO 监控项业务实体对象集合
     * @return Map<String ,   ItemsDTO> Map[key=String|value=ItemsDTO]
     */
    public static Map<String, List<ItemsDTO>> toDTOMap(final List<ItemsDTO> listItemsDTO) {
        if (CollectionUtils.isEmpty(listItemsDTO)) {
            return Maps.newHashMap();
        }
        Map<String, List<ItemsDTO>> itemsDTOMaps = Maps.newHashMap();
        for (ItemsDTO itemsDTO : listItemsDTO) {
            if (null == itemsDTOMaps.get(itemsDTO.getTemplateId())) {
                List<ItemsDTO> listItem = new ArrayList<ItemsDTO>();
                listItem.add(itemsDTO);
                itemsDTOMaps.put(itemsDTO.getTemplateId(), listItem);
            } else {
                List<ItemsDTO> listItem = itemsDTOMaps.get(itemsDTO.getTemplateId());
                listItem.add(itemsDTO);
            }
        }
        return itemsDTOMaps;
    }

    /**
     * 将监控项业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listItemsDTO 监控项业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ItemsDTO> listItemsDTO) {
        if (CollectionUtils.isEmpty(listItemsDTO)) {
            return null;
        }
        int size = listItemsDTO.size();
        String[] itemIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            itemIdArrays[i] = listItemsDTO.get(i).getItemId();
        }
        return itemIdArrays;
    }
} 
