package com.aspire.mirror.template.server.biz.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.template.api.dto.model.ItemExt;
import com.aspire.mirror.template.common.entity.TemplateDataSyncTypeConstant;
import com.aspire.mirror.template.api.dto.TemplateDateSyncVo;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.server.biz.ItemsBiz;
import com.aspire.mirror.template.server.clientservice.ThemeDataServiceClient;
import com.aspire.mirror.template.server.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.template.server.dao.ItemsDao;
import com.aspire.mirror.template.server.dao.TemplateDataSyncDao;
import com.aspire.mirror.template.server.dao.TriggersDao;
import com.aspire.mirror.template.server.dao.po.Items;
import com.aspire.mirror.template.server.dao.po.TemplateDataSync;
import com.aspire.mirror.template.server.dao.po.Triggers;
import com.aspire.mirror.template.server.dao.po.transform.ItemsTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 监控项业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    ItemsBizImpl.java
 * 类描述:    监控项业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:47
 */
@Service
@Transactional
public class ItemsBizImpl implements ItemsBiz {

    @Value("${elasticsearch.index.name}")
    private String indexName;
    public static final String SUB_INDEX_ITEM = "-item";

    /**
     * 新增数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 新增数据条数
     */
    @Transactional
    public String insert(final ItemsDTO itemsDTO) {
        if (null == itemsDTO) {
            LOGGER.error("method[insert] param[itemsDTO] is null");
            throw new RuntimeException("param[itemsDTO] is null");
        }
        Items items = ItemsTransformer.toPo(itemsDTO);
        String currUser = RequestAuthContext.getRequestHeadUserName();
        String itemId = UUID.randomUUID().toString();
        items.setItemId(itemId);
        items.setCreateTime(new Date());
        items.setCreater(currUser);
        itemsDao.insert(items);
        //插入同步数据
        TemplateDataSync templateDataSync = new TemplateDataSync();
        templateDataSync.setDataId(itemId);
        templateDataSync.setSyncDataType(TemplateDataSyncTypeConstant.SYNC_TYPE_ITEM);
        templateDataSync.setOperateType(TemplateDateSyncVo.Operate.C.toString());
        templateDataSyncDao.insert(templateDataSync);

        //添加任务
//        submitJob(items);
        return itemId;
    }

    /**
     * 批量新增监控项数据
     *
     * @param listItemsDTO 监控项DTO集合对象
     * @return int 新增数据条数
     */
    @Transactional
    public List<ItemsDTO> insertByBatch(final List<ItemsDTO> listItemsDTO) {
        if (CollectionUtils.isEmpty(listItemsDTO)) {
            LOGGER.error("method[insertByBatch] param[listItemsDTO] is null");
            throw new RuntimeException("param[listItemsDTO] is null");
        }
        List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date now = new Date();
        List<ItemExt> itemExts = Lists.newArrayList();
        for (ItemsDTO itemsDTO : listItemsDTO) {
            String uuid = UUID.randomUUID().toString();
            itemsDTO.setItemId(uuid);
            if (itemsDTO.getItemExt() != null) {
                itemsDTO.getItemExt().setItemId(uuid);
                itemExts.add(itemsDTO.getItemExt());
            }
            TemplateDataSync templateDataSync = new TemplateDataSync();
            templateDataSync.setDataId(uuid);
            templateDataSync.setSyncDataType(TemplateDataSyncTypeConstant.SYNC_TYPE_ITEM);
            templateDataSync.setOperateType(TemplateDateSyncVo.Operate.C.toString());
            templateDataSyncList.add(templateDataSync);
        }
        List<Items> listItems = ItemsTransformer.toPo(listItemsDTO);
        listItems.stream().forEach(item -> {
            item.setCreater(currUser);
            item.setCreateTime(now);
        });
        if (!CollectionUtils.isEmpty(listItems)) {
            itemsDao.insertByBatch(listItems);
        }
        if (!CollectionUtils.isEmpty(itemExts)) {
            itemsDao.batchInsertExt(itemExts);
        }
        //插入同步数据
        templateDataSyncDao.insertBatch(templateDataSyncList);
        return listItemsDTO;
    }
    /**
     * 根据主键删除数据
     *
     * @param itemId 主键
     * @return int 删除数据条数
     */
//    public int deleteByPrimaryKey(final String itemId){
//        if (StringUtils.isEmpty(itemId)) {
//            LOGGER.error("method[eleteByPrimaryKey] param[itemId] is null");
//            throw new RuntimeException("param[itemId] is null");
//        }
//        return itemsDao.deleteByPrimaryKey(itemId);
//    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param itemIdArrays 主键数组
     * @return int 删除数据条数
     */
    @Transactional
    public int deleteByPrimaryKeyArrays(final String[] itemIdArrays) {
        if (ArrayUtils.isEmpty(itemIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[itemIdArrays] is null");
            throw new RuntimeException("param[itemIdArrays] is null");
        }
        List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
        List<Triggers> triggersList = triggerDao.selectByItemIdArrays(itemIdArrays);
        if (!CollectionUtils.isEmpty(triggersList)) {
            for (Triggers trigger : triggersList) {
                TemplateDataSync templateDataSync = new TemplateDataSync(trigger.getTriggerId(),
                        TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.D.toString());
                templateDataSyncList.add(templateDataSync);
            }
        }
        for (String itemId : itemIdArrays) {
            TemplateDataSync templateDataSync = new TemplateDataSync(itemId, TemplateDataSyncTypeConstant
                    .SYNC_TYPE_ITEM, TemplateDateSyncVo.Operate.D.toString());
            templateDataSyncList.add(templateDataSync);
        }

        //首先删除触发器信息
        triggerDao.deleteByItemIds(itemIdArrays);

        int result = itemsDao.deleteByPrimaryKeyArrays(itemIdArrays);
        itemsDao.deleteExtByItemIdArrays(itemIdArrays);
        //插入同步数据
        templateDataSyncDao.insertBatch(templateDataSyncList);
        return result;
    }

    /**
     * 根据条件删除数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 删除数据条数
     */
//    public int delete(final ItemsDTO itemsDTO) {
//        if (null == itemsDTO) {
//            LOGGER.error("method[delete] param[itemsDTO] is null");
//            throw new RuntimeException("param[itemsDTO] is null");
//        }
//        Items items = ItemsTransformer.toPo(itemsDTO);
//        int result = itemsDao.delete(items);
//        return result;
//    }

    /**
     * 根据参数选择性更新数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
//    public int updateByPrimaryKeySelective(final ItemsDTO itemsDTO){
//        if(null == itemsDTO){
//            LOGGER.error("method[updateByPrimaryKey] param[itemsDTO] is null");
//            throw new RuntimeException("param[itemsDTO] is null");
//        }
//        Items items = ItemsTransformer.toPo(itemsDTO);
//        return itemsDao.updateByPrimaryKeySelective(items);
//    }

    /**
     * 根据主键更新数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final ItemsDTO itemsDTO) {
        if (null == itemsDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[itemsDTO] is null");
            throw new RuntimeException("param[itemsDTO] is null");
        }
        if (StringUtils.isEmpty(itemsDTO.getItemId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[itemId] is null");
            throw new RuntimeException("param[itemId] is null");
        }
        Items items = ItemsTransformer.toPo(itemsDTO);

        int result = itemsDao.updateByPrimaryKeySelective(items);
        ItemExt itemExt = itemsDao.getItemExtByItemId(itemsDTO.getItemId());
        if (items.getItemExt() != null) {
            items.getItemExt().setItemId(itemsDTO.getItemId());
            if (itemExt != null) {
                itemsDao.updateItemExtByItemId(items.getItemExt());
            } else {
                List<ItemExt> itemExtList = Lists.newArrayList();
                itemExtList.add(items.getItemExt());
                itemsDao.batchInsertExt(itemExtList);
            }
        } else {
        	if (itemExt != null) {
        		itemsDao.deleteExtByItemIdArrays(new String[]{itemsDTO.getItemId()});
        	}
        }
        
        //插入同步数据
        TemplateDataSync templateDataSync = new TemplateDataSync(items.getItemId(), TemplateDataSyncTypeConstant
                .SYNC_TYPE_ITEM, TemplateDateSyncVo.Operate.U.toString());
        templateDataSyncDao.insert(templateDataSync);
        //添加任务
//        submitJob(items);
        return result;
    }

    /**
     * 根据主键查询
     *
     * @param itemId 主键
     * @return ItemsDTO 返回对象
     */
    public ItemsDTO selectByPrimaryKey(final String itemId) {
        Items items = itemsDao.selectByPrimaryKey(itemId);
        if (StringUtils.isEmpty(itemId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[itemId] is null");
            return null;
        }
        return ItemsTransformer.fromPo(items);
    }

    /**
     * 根据主键数组查询
     *
     * @param itemIdArrays 主键数组
     * @return List<ItemsDTO> 返回集合对象
     */
    public List<ItemsDTO> selectByPrimaryKeyArrays(final String[] itemIdArrays) {
        if (ArrayUtils.isEmpty(itemIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[itemIdArrays] is null");
            return Collections.emptyList();
        }
        List<Items> listItems = itemsDao.selectByPrimaryKeyArrays(itemIdArrays);
        return ItemsTransformer.fromPo(listItems);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param itemsDTO 监控项DTO对象
     * @return List<Items>  返回集合
     */
    public List<ItemsDTO> select(final ItemsDTO itemsDTO) {
        if (null == itemsDTO) {
            LOGGER.warn("select Object itemsDTO is null");
            return Collections.emptyList();
        }
        Items items = ItemsTransformer.toPo(itemsDTO);
        List<Items> listItems = itemsDao.select(items);
        return ItemsTransformer.fromPo(listItems);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
    public int selectCount(final ItemsDTO itemsDTO) {
        if (null == itemsDTO) {
            LOGGER.warn("selectCount Object itemsDTO is null");
        }
        Items items = ItemsTransformer.toPo(itemsDTO);
        return itemsDao.selectCount(items);
    }

    /**
     * 分页查询
     *
     * @param pageRequest page请求
     * @return 返回page结果对象
     */
    @Override
    public PageResponse<ItemsDTO> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = itemsDao.pageListCount(page);
        PageResponse<ItemsDTO> pageResponse = new PageResponse<ItemsDTO>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Items> listItem = itemsDao.pageList(page);
            List<ItemsDTO> listDTO = ItemsTransformer.fromPo(listItem);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    /**
     * 获取最新监控值
     *
     * @param itemId  监控项ID
     * @param sysCode 业务编码
     * @return
     */
    @Override
    public String findLastUpValueByItemId(String itemId, String sysCode) {
        return themeDataService.getItemValueByItemId(itemId, sysCode);
    }

    /**
     * 获取主题计算结果
     *
     * @param itemId       监控项ID
     * @param startTimeStr 计算开始时间
     * @param endTimeStr   计算结束时间
     * @return 主题计算结果
     */
    @Override
    public List<Map<String, Object>> getThemeCalcResult(String itemId, String startTimeStr, String endTimeStr) {
        Items item = itemsDao.selectByPrimaryKey(itemId);
        if (item == null || !item.getSysType().equals("THEME_CALC")) {
            LOGGER.warn("ItemBizImpl[getThemeCalcResult] not find item by param itemId : {}", itemId);
            return null;
        }
        final long startTime, endTime;
        Date now = new Date();
        Integer delay = Integer.parseInt(item.getDelay());
        if (StringUtils.isEmpty(startTimeStr)) {
            startTime = now.getTime() - delay * 60 * 1000;
        } else {
            startTime = DateUtil.getDate(startTimeStr, DateUtil.DATE_TIME_FORMAT).getTime();
        }
        if (StringUtils.isEmpty(endTimeStr)) {
            endTime = now.getTime() - 1;
        } else {
            endTime = DateUtil.getDate(endTimeStr, DateUtil.DATE_TIME_FORMAT).getTime();
        }
        JSONArray bizGroupArray = JSONArray.parseArray(item.getBizGroup());
        JSONArray bizThemeExpArray = JSONArray.parseArray(item.getBizThemeExp());

        Map<String, String> nameMap = Maps.newLinkedHashMap();
        for (int i = 0; i < bizGroupArray.size(); i++) {
            JSONObject itemObj = bizGroupArray.getJSONObject(i);
            nameMap.put(itemObj.getString("code"), itemObj.getString("name"));
        }
        String sortName = bizThemeExpArray.getJSONObject(0).getString("code");
        for (int i = 0; i < bizThemeExpArray.size(); i++) {
            JSONObject itemObj = bizThemeExpArray.getJSONObject(i);
            nameMap.put(itemObj.getString("code"), itemObj.getString("name"));
        }
        List<Map<String, Object>> result = themeDataService.getThemeCalcResult(itemId, item.getBizThemeId(),
                startTime, endTime, sortName, JSON.toJSONString(nameMap));
        return result;
    }

    @Override
    public List<ItemsDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] itemIdArray, String proxyIdentity) {
        if (ArrayUtils.isEmpty(itemIdArray)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[itemIdArrays] is null");
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(proxyIdentity)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[proxyIdentity] is null");
            return Collections.emptyList();
        }
        List<Items> listItems = itemsDao.selectByPrimaryKeyArraysAndProxyIdentity(itemIdArray, proxyIdentity);
        return ItemsTransformer.fromPo(listItems);
    }

    public List<ItemsDTO> selectByTemplateIdArrays(String[] strings) {
        return ItemsTransformer.fromPo(itemsDao.selectByTemplateIdArrays(strings));
    }
    /**
     * 计算主题指标结果入库
     */

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private ItemsDao itemsDao;

    @Autowired
    private TriggersDao triggerDao;

    @Autowired
    private TemplateDataSyncDao templateDataSyncDao;

    @Autowired
    private ThemeDataServiceClient themeDataService;

}
