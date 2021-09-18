package com.aspire.mirror.template.server.biz.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.ItemSyncRequest;
import com.aspire.mirror.template.api.dto.TemplateDateSyncVo;
import com.aspire.mirror.template.api.dto.model.FunctionsDTO;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModel;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModelExt;
import com.aspire.mirror.template.common.entity.TemplateDataSyncTypeConstant;
import com.aspire.mirror.template.server.biz.TriggersBiz;
import com.aspire.mirror.template.server.dao.FunctionsDao;
import com.aspire.mirror.template.server.dao.ItemsDao;
import com.aspire.mirror.template.server.dao.TemplateDataSyncDao;
import com.aspire.mirror.template.server.dao.TriggersDao;
import com.aspire.mirror.template.server.dao.po.*;
import com.aspire.mirror.template.server.dao.po.transform.TriggersTransformer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 触发器业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    TriggersBizImpl.java
 * 类描述:    触发器业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
@Slf4j
public class TriggersBizImpl implements TriggersBiz {
    private static final String ITEM_PROTOTYPE_REL_PREFIX = "item_prototype_rel_";
    private static final String ZABBIX_DYNAMIC_SYCN_CACHE = "ZABBIX_DYNAMIC_SYCN_CACHE";
    //    private static ConcurrentHashMap<String, Map<String, String>> cacheMap = new ConcurrentHashMap<>();
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 新增数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 新增数据条数
     */
//    public int insert(final TriggersDTO triggersDTO) {
//        if (null == triggersDTO) {
//            LOGGER.error("method[insert] param[triggersDTO] is null");
//            throw new RuntimeException("param[triggersDTO] is null");
//        }
//        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
//        return triggersDao.insert(triggers);
//    }

    /**
     * 批量新增触发器数据
     *
     * @param listTriggersDTO 触发器DTO集合对象
     * @return int 新增数据条数
     */
    public List<TriggersDTO> insertByBatch(final List<TriggersDTO> listTriggersDTO) {
        if (CollectionUtils.isEmpty(listTriggersDTO)) {
            LOGGER.error("method[insertByBatch] param[listTriggersDTO] is null");
            throw new RuntimeException("param[listTriggersDTO] is null");
        }
        List<Functions> listFunction = Lists.newArrayList();
        List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
        for (TriggersDTO triggersDTO : listTriggersDTO) {
            String triggersId = UUID.randomUUID().toString();
            triggersDTO.setTriggerId(triggersId);
            if (!CollectionUtils.isEmpty(triggersDTO.getFunctionList())) {
                for (FunctionsDTO functionsDTO : triggersDTO.getFunctionList()) {
                    Functions functions = new Functions();
                    functions.setFunction(functionsDTO.getFunction());
                    functions.setParameter(functionsDTO.getParameter());
                    functions.setItemId(triggersDTO.getItemId());
                    functions.setTriggerId(triggersId);
                    functions.setFunctionId(UUID.randomUUID().toString());
                    listFunction.add(functions);
                }

            }
            //设置同步数据
            TemplateDataSync templateDataSync = new TemplateDataSync(triggersId, TemplateDataSyncTypeConstant
                    .SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.C.toString());
            templateDataSyncList.add(templateDataSync);
        }
        List<Triggers> listTriggers = TriggersTransformer.toPo(listTriggersDTO);
        triggersDao.insertByBatch(listTriggers);
        if (!CollectionUtils.isEmpty(listFunction)) {
            functionsDao.insertByBatch(listFunction);
        }
        //插入同步数据
        templateDataSyncDao.insertBatch(templateDataSyncList);
        return listTriggersDTO;
    }

    /**
     * 根据主键删除数据
     *
     * @param triggerId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String triggerId) {
        if (StringUtils.isEmpty(triggerId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[triggerId] is null");
            throw new RuntimeException("param[triggerId] is null");
        }
        int result = triggersDao.deleteByPrimaryKey(triggerId);
        // 同步数据表新增
        TemplateDataSync templateDataSync = new TemplateDataSync(triggerId,
                TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.D.toString());
        templateDataSyncDao.insert(templateDataSync);
        return result;
    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param triggerIdArrays 主键数组
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKeyArrays(final String[] triggerIdArrays) {
        if (ArrayUtils.isEmpty(triggerIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[triggerIdArrays] is null");
            throw new RuntimeException("param[triggerIdArrays] is null");
        }
        return triggersDao.deleteByPrimaryKeyArrays(triggerIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 删除数据条数
     */
    public int delete(final TriggersDTO triggersDTO) {
        if (null == triggersDTO) {
            LOGGER.error("method[delete] param[triggersDTO] is null");
            throw new RuntimeException("param[triggersDTO] is null");
        }
        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
        return triggersDao.delete(triggers);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
//    public int updateByPrimaryKeySelective(final TriggersDTO triggersDTO) {
//        if (null == triggersDTO) {
//            LOGGER.error("method[updateByPrimaryKey] param[triggersDTO] is null");
//            throw new RuntimeException("param[triggersDTO] is null");
//        }
//        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
//        return triggersDao.updateByPrimaryKeySelective(triggers);
//    }

    /**
     * 根据主键更新数据
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final TriggersDTO triggersDTO) {
        if (null == triggersDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[triggersDTO] is null");
            throw new RuntimeException("param[triggersDTO] is null");
        }
        if (StringUtils.isEmpty(triggersDTO.getTriggerId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[triggerId] is null");
            throw new RuntimeException("param[triggerId] is null");
        }
        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
        int result = triggersDao.updateByPrimaryKey(triggers);
        //插入同步数据
        TemplateDataSync templateDataSync = new TemplateDataSync(triggersDTO.getTriggerId(),
                TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.U.toString());
        templateDataSyncDao.insert(templateDataSync);
        return result;
    }

    /**
     * 根据主键查询
     *
     * @param triggerId 主键
     * @return TriggersDTO 返回对象
     */
    public TriggersDTO selectByPrimaryKey(final String triggerId) {
        Triggers triggers = triggersDao.selectByPrimaryKey(triggerId);
        if (StringUtils.isEmpty(triggerId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[triggerId] is null");
            return null;
        }
        return TriggersTransformer.fromPo(triggers);
    }

    /**
     * 根据主键数组查询
     *
     * @param triggerIdArrays 主键数组
     * @return List<TriggersDTO> 返回集合对象
     */
    public List<TriggersDTO> selectByPrimaryKeyArrays(final String[] triggerIdArrays) {
        if (ArrayUtils.isEmpty(triggerIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[triggerIdArrays] is null");
            return Collections.emptyList();
        }
        List<Triggers> listTriggers = triggersDao.selectByPrimaryKeyArrays(triggerIdArrays);
        return TriggersTransformer.fromPo(listTriggers);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param triggersDTO 触发器DTO对象
     * @return List<Triggers>  返回集合
     */
    public List<TriggersDTO> select(final TriggersDTO triggersDTO) {
        if (null == triggersDTO) {
            LOGGER.warn("select Object triggersDTO is null");
            return Collections.emptyList();
        }
        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
        List<Triggers> listTriggers = triggersDao.select(triggers);
        return TriggersTransformer.fromPo(listTriggers);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param triggersDTO 触发器DTO对象
     * @return int 数据条数
     */
    public int selectCount(final TriggersDTO triggersDTO) {
        if (null == triggersDTO) {
            LOGGER.warn("selectCount Object triggersDTO is null");
        }
        Triggers triggers = TriggersTransformer.toPo(triggersDTO);
        return triggersDao.selectCount(triggers);
    }

    /**
     * 根据监控项ID删除触发器
     *
     * @param itemIdArrays
     * @return 受影响数据条数
     */
    @Override
    public int deleteByItemIdArrays(String[] itemIdArrays) {
        List<Triggers> listTriggers = triggersDao.selectByItemIdArrays(itemIdArrays);

        functionsDao.deleteByItemIdArrays(itemIdArrays);
        int result = triggersDao.deleteByItemIds(itemIdArrays);

        // 同步数据表新增
        if (!CollectionUtils.isEmpty(listTriggers)) {
            List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
            for (Triggers triggers : listTriggers) {
                TemplateDataSync templateDataSync = new TemplateDataSync(triggers.getTriggerId(),
                        TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.D.toString());
                templateDataSyncList.add(templateDataSync);
            }
            templateDataSyncDao.insertBatch(templateDataSyncList);
        }
        return result;
    }

    /**
     * 根据监控项ID集合查询
     *
     * @param itemIdArrays 监控项集合
     * @return 结果集
     */
    @Override
    public List<TriggersDTO> listByItemIdArrays(String[] itemIdArrays) {
        List<Triggers> listTriggers = triggersDao.selectByItemIdArrays(itemIdArrays);
        return TriggersTransformer.fromPo(listTriggers);
    }

    /**
     * 根据模板ID查询列表
     *
     * @param templateId 模板ID
     * @return 触发器列表
     */
    @Override
    public List<TriggersDTO> listByTemplateId(String templateId) {
        List<Triggers> listTriggers = triggersDao.listByTemplateId(templateId);
        return TriggersTransformer.fromPo(listTriggers);
    }

    @Override
    public void updateExpressionByItemIdionByItemId(TriggersDTO triggersDTO) {
        triggersDao.updateExpressionByItemIdionByItemId(TriggersTransformer.toPo(triggersDTO));
    }

    // 动态阈值批量同步
    @Override
    public GeneralResponse batchDynamicModelData(List<StandardDynamicModelExt> standardDynamicModelExtList, String indexType) {
        try {
            List<StandardDynamicModel> insertModelList = Lists.newArrayList();
            List<StandardDynamicModelExt> insertModelExtList = Lists.newArrayList();
            List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
            Date now = new Date();
            List<String> handDynamicModelIds = Lists.newArrayList();
            for (StandardDynamicModelExt standardDynamicModelExt : standardDynamicModelExtList) {
                List<StandardDynamicModel> dynamicModelList = standardDynamicModelExt.getDynamicModelList();
                List<String> modelIds = Lists.newArrayList();
                for (StandardDynamicModel standardDynamicModel : dynamicModelList) {
                    if (StringUtils.isEmpty(standardDynamicModel.getZabbixItemId())) {
//                        RBucket<Map<String, String>> bucket = redissonClient.getBucket(ITEM_PROTOTYPE_REL_PREFIX + standardDynamicModel.getThridSystemId());
                        Map<String, String> resultMap = redissonClient.getMap(ITEM_PROTOTYPE_REL_PREFIX + standardDynamicModel.getThridSystemId());
                        if (resultMap.get(standardDynamicModel.getDeviceItemId()) != null) {
                            standardDynamicModel.setZabbixItemId(getItemPrototypeId(resultMap.get(standardDynamicModel.getDeviceItemId()), resultMap));
                        } else {
                            LOGGER.error("TriggersBizImpl[batchDynamicModelData] device_item_id {} item is not associated with prototype ", standardDynamicModel.getDeviceItemId());
                            continue;
                        }
                    }
                    List<Triggers> triggers = triggersDao.selectDynamicModelTriggerBySpecialParam(standardDynamicModel.getThridSystemId(), standardDynamicModel.getZabbixItemId(), standardDynamicModel.getPriority());
                    if (CollectionUtils.isEmpty(triggers)) {
                        // 新增触发器
                        String triggersId = addDynamicTrigger(standardDynamicModel);
                        if (triggersId != null) {
                            // 添加动态阈值数据
                            addModelItem(insertModelList, now, modelIds, standardDynamicModel, triggersId, templateDataSyncList);
                            // 设置已经处理的模型ID缓存依据
                            handDynamicModelIds.add(standardDynamicModel.getModelId());
                        }
//                    triggersDao.insertDynamicModel(standardDynamicModel);
                    } else {
                        StandardDynamicModel resultModel = triggersDao.selectDynamicModel(triggers.get(0).getTriggerId(), standardDynamicModel.getDeviceItemId());
                        if (resultModel != null) {
                            // 设置已经处理的模型ID缓存依据
                            handDynamicModelIds.add(resultModel.getModelId());
                            modelIds.add(resultModel.getModelId());
                            if (!resultModel.equals(standardDynamicModel)) {
                                standardDynamicModel.setModelId(resultModel.getModelId());
                                standardDynamicModel.setUpdateTime(new Date());
                                // 添加同步数据
                                triggersDao.updateDynamicModelById(standardDynamicModel);
                                TemplateDataSync templateDataSync = new TemplateDataSync(standardDynamicModel.getModelId(), TemplateDataSyncTypeConstant
                                        .SYNC_TYPE_TRIGGER_MODEL, TemplateDateSyncVo.Operate.U.toString());
                                templateDataSyncDao.insert(templateDataSync);
                            }
                        } else {
                            addModelItem(insertModelList, now, modelIds, standardDynamicModel, triggers.get(0).getTriggerId(), templateDataSyncList);
                        }
                    }
                }
                if (!modelIds.isEmpty()) {
                    Collections.sort(modelIds);
                    StandardDynamicModelExt resultModelExt = triggersDao.selectDynamicModelExtByModelIds(JSON.toJSONString(modelIds));
                    standardDynamicModelExt.setModelIds(JSON.toJSONString(modelIds));
                    if (resultModelExt == null) {
                        insertModelExtList.add(standardDynamicModelExt);
                    } else {
                        if (!resultModelExt.equals(standardDynamicModelExt)) {
                            standardDynamicModelExt.setId(resultModelExt.getId());
                            triggersDao.updateDynamicModelExtById(standardDynamicModelExt);
                        }
                    }
                } else {
                    log.warn("====modelExt modelIds is empty===={}", standardDynamicModelExt);
                }
            }
            if (!insertModelList.isEmpty()) {
                triggersDao.insertBatchDynamicModel(insertModelList);
                templateDataSyncDao.insertBatch(templateDataSyncList);
            }
            if (!insertModelExtList.isEmpty()) {
                triggersDao.insertBatchDynamicModelExt(insertModelExtList);
            }
            RBucket<List<String>> bucket = redissonClient.getBucket(ZABBIX_DYNAMIC_SYCN_CACHE);
            if (indexType.equals("START")) {
                if (bucket.get() != null) {
                    bucket.delete();
                }
                bucket.set(handDynamicModelIds);
            } else if (indexType.equals("END")) {
                List<String> modelIdList = bucket.get();
                modelIdList.addAll(handDynamicModelIds);
                bucket.set(modelIdList);
                List<String> existModelIdList = triggersDao.selecAlltDynamicModelId();
                existModelIdList.removeAll(bucket.get());
                if (existModelIdList.size() > 0) {
                    triggersDao.deleteDynamicModelByModelIdList(existModelIdList);
                }
                bucket.delete();
            } else {
                List<String> modelIdList = bucket.get();
                modelIdList.addAll(handDynamicModelIds);
                bucket.set(modelIdList);
            }
        } catch (Exception e) {
            LOGGER.error("TriggersBizImpl[batchDynamicModelData] is failed！", e);
            return new GeneralResponse(false, "批量信息动态模型失败！");
        }
        return new GeneralResponse();
    }

    private String getItemPrototypeId(String parentItemId, Map<String, String> resultMap) {
        if (StringUtils.isEmpty(resultMap.get(parentItemId))) {
            return parentItemId;
        } else {
            LOGGER.debug("continue find item prototype id, current parentItemId is {}", resultMap.get(parentItemId));
            return getItemPrototypeId(resultMap.get(parentItemId), resultMap);
        }
    }

    @Override
    public List<StandardDynamicModel> selectDynamicModelByModelIdArrayAndProxyIdentity(String[] modelIdArray, String proxyIdentity) {
        return triggersDao.selectDynamicModelByModelIdArrayAndProxyIdentity(modelIdArray, proxyIdentity);
    }

    private void addModelItem(List<StandardDynamicModel> insertModelList, Date now, List<String> modelIds, StandardDynamicModel standardDynamicModel, String triggerId, List<TemplateDataSync> templateDataSyncList) {
        String modelId = UUID.randomUUID().toString();
        modelIds.add(modelId);
        standardDynamicModel.setTriggerId(triggerId);
        standardDynamicModel.setModelId(modelId);

        standardDynamicModel.setCreateTime(now);
        standardDynamicModel.setUpdateTime(now);
        insertModelList.add(standardDynamicModel);

        TemplateDataSync templateDataSync = new TemplateDataSync(standardDynamicModel.getModelId(), TemplateDataSyncTypeConstant
                .SYNC_TYPE_TRIGGER_MODEL, TemplateDateSyncVo.Operate.C.toString());
        templateDataSyncList.add(templateDataSync);
    }

    private String addDynamicTrigger(StandardDynamicModel standardDynamicModel) {
        Triggers trigger = new Triggers();
        trigger.setExpression("");
        Items item = itemsDao.selectItemsBySpecialParam(standardDynamicModel.getThridSystemId(), standardDynamicModel.getZabbixItemId());
        if (item != null) {
            String triggersId = UUID.randomUUID().toString();
            trigger.setItemId(item.getItemId());
            trigger.setName(item.getName());
            trigger.setPriority(standardDynamicModel.getPriority());
            trigger.setStatus("ON");
            trigger.setTriggerId(UUID.randomUUID().toString());
            // 动态阈值类型
            trigger.setType("2");
            trigger.setParam("");
            this.triggersDao.insert(trigger);
            TemplateDataSync templateDataSync = new TemplateDataSync(triggersId, TemplateDataSyncTypeConstant
                    .SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.C.toString());
            templateDataSyncDao.insert(templateDataSync);
            return triggersId;
        } else {
            log.error("The item prototype is not exist, deviceItemid is  " + standardDynamicModel.getDeviceItemId() + "and zabbix_item_id is " + standardDynamicModel.getZabbixItemId());
        }
        return null;
    }

    @Override
    public GeneralResponse zabbixItemAndPrototypeRelationSync(ItemSyncRequest itemSyncRequest) {
        // 设置缓存
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletableFuture.runAsync(() -> {
            if (itemSyncRequest.getItemIdMap() != null) {
                for (String key : itemSyncRequest.getItemIdMap().keySet()) {
                    redissonClient.getMap(ITEM_PROTOTYPE_REL_PREFIX + itemSyncRequest.getProxyIdentity()).put(key, itemSyncRequest.getItemIdMap().get(key));
                }
            }
            log.info("TriggerBizImpl[zabbixItemAndPrototypeRelationSync] bucket is {}, value map size is {}", ITEM_PROTOTYPE_REL_PREFIX + itemSyncRequest.getProxyIdentity(), redissonClient.getMap(ITEM_PROTOTYPE_REL_PREFIX + itemSyncRequest.getProxyIdentity()).size());
        }, executor);
        return new GeneralResponse();
    }

    @Override
    public List<TriggersDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] triggerIdArray, String proxyIdentity) {
        if (ArrayUtils.isEmpty(triggerIdArray)) {
            LOGGER.warn("method[selectByPrimaryKeyArraysAndProxyIdentity] param[triggerIdArray] is null");
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(proxyIdentity)) {
            LOGGER.warn("method[selectByPrimaryKeyArraysAndProxyIdentity] param[proxyIdentity] is null");
            return Collections.emptyList();
        }
        List<Triggers> listTriggers = triggersDao.selectByPrimaryKeyArraysAndProxyIdentity(triggerIdArray, proxyIdentity);
        return TriggersTransformer.fromPo(listTriggers);
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggersBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private TriggersDao triggersDao;

    @Autowired
    private FunctionsDao functionsDao;

    @Autowired
    private ItemsDao itemsDao;

    @Autowired
    private TemplateDataSyncDao templateDataSyncDao;
} 
