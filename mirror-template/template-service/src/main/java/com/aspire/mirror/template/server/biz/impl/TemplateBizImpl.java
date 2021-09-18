package com.aspire.mirror.template.server.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.TemplateDateSyncVo;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.api.dto.model.TemplateDTO;
import com.aspire.mirror.template.api.dto.vo.ItemsVO;
import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import com.aspire.mirror.template.api.dto.vo.ZabbixTemplateSyncVo;
import com.aspire.mirror.template.common.entity.TemplateDataSyncTypeConstant;
import com.aspire.mirror.template.server.biz.TemplateBiz;
import com.aspire.mirror.template.server.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.template.server.dao.*;
import com.aspire.mirror.template.server.dao.po.*;
import com.aspire.mirror.template.server.dao.po.transform.ItemsTransformer;
import com.aspire.mirror.template.server.dao.po.transform.TemplateObjectTransformer;
import com.aspire.mirror.template.server.dao.po.transform.TemplateTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    TemplateBizImpl.java
 * 类描述:    monitor_template业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
@Transactional
public class TemplateBizImpl implements TemplateBiz {
    private static final String ZABBIX_TEMPLATE_SYCN_CACHE = "ZABBIX_TEMPLATE_SYCN_CACHE";
    //    private static ConcurrentHashMap<String, List<String>> cacheMap = new ConcurrentHashMap<>();
    @Autowired(required = false)
    private RedissonClient redissonClient;

    /**
     * 新增数据
     *
     * @param templateDTO 模板DTO对象
     * @return int 新增数据条数
     */
    @Transactional
    public String insert(final TemplateDTO templateDTO) {
        if (null == templateDTO) {
            LOGGER.error("method[insert] param[templateDTO] is null");
            throw new RuntimeException("param[templateDTO] is null");
        }
        Template existTemp = templateDao.selectByName(templateDTO.getName());
        if (existTemp != null) {
            LOGGER.error("method[insert] The template name is already exist");
            throw new RuntimeException("模板名称已经存在！");
        }

//        String[] deviceArrays = templateDTO.getDevices().split(",");
//        List<TemplateObject> deviceList = Lists.newArrayList();
        String templateId = UUID.randomUUID().toString();
//        for(String device: deviceArrays){
//            TemplateObject templateDevice = new TemplateObject();
//            templateDevice.setDeviceId(device);
//            templateDevice.setTemplateDeviceId(UUID.randomUUID().toString());
//            templateDevice.setTemplateId(templateId);
//            deviceList.add(templateDevice);
//        }
//        templateDeviceDao.insertByBatch(deviceList);
        Template template = TemplateTransformer.toPo(templateDTO);
        Date now = new Date();
        template.setCreateTime(now);
        template.setUpdateTime(now);
        String userName = RequestAuthContext.getRequestHeadUserName();
        template.setCreater(userName);
        template.setUpdater(userName);
        template.setTemplateId(templateId);
        if (StringUtils.isEmpty(template.getStatus())) {
            // 如果状态为空，设置为正式数据
            template.setStatus("1");
        }
        templateDao.insert(template);
        //插入同步数据
        TemplateDataSync templateDataSync = new TemplateDataSync();
        templateDataSync.setDataId(templateId);
        templateDataSync.setSyncDataType(TemplateDataSyncTypeConstant.SYNC_TYPE_TEMPLATE);
        templateDataSync.setOperateType(TemplateDateSyncVo.Operate.C.toString());
        templateDataSyncDao.insert(templateDataSync);
        return templateId;
    }

//    /**
//     * 批量新增monitor_template数据
//     *
//     * @param listTemplateDTO 模板DTO集合对象
//     * @return int 新增数据条数
//     */
//    public int insertByBatch(final List<TemplateDTO> listTemplateDTO) {
//        if (CollectionUtils.isEmpty(listTemplateDTO)) {
//            LOGGER.error("method[insertByBatch] param[listTemplateDTO] is null");
//            throw new RuntimeException("param[listTemplateDTO] is null");
//        }
//        List<Template> listTemplate = TemplateTransformer.toPo(listTemplateDTO);
//        return templateDao.insertByBatch(listTemplate);
//    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param templateIdArrays 主键数组
     * @return int 删除数据条数
     */
    @Transactional
    public int deleteByPrimaryKeyArrays(final String[] templateIdArrays) {
        if (ArrayUtils.isEmpty(templateIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[templateIdArrays] is null");
            throw new RuntimeException("param[templateIdArrays] is null");
        }

        List<TemplateDataSync> templateDataSyncList = Lists.newArrayList();
        // 同步数据拼装 (监控项、触发器)
        List<TemplateDataSync> itemDataSyncList = Lists.newArrayList();
        List<TemplateDataSync> triggerDataSyncList = Lists.newArrayList();
        List<Items> itemsList = itemsDao.selectByTemplateIdArrays(templateIdArrays);
        if (!CollectionUtils.isEmpty(itemsList)) {
            String[] itemIdArray = new String[itemsList.size()];
            for (int i = 0; i < itemsList.size(); i++) {
                itemIdArray[i] = itemsList.get(i).getItemId();
                TemplateDataSync templateDataSync = new TemplateDataSync(itemsList.get(i).getItemId(),
                        TemplateDataSyncTypeConstant.SYNC_TYPE_ITEM, TemplateDateSyncVo.Operate.D.toString());
                itemDataSyncList.add(templateDataSync);
            }
            List<Triggers> triggersList = triggersDao.selectByItemIdArrays(itemIdArray);
            if (!CollectionUtils.isEmpty(triggersList)) {
                for (Triggers triggers : triggersList) {
                    TemplateDataSync templateDataSync = new TemplateDataSync(triggers.getTriggerId(),
                            TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER, TemplateDateSyncVo.Operate.D.toString());
                    triggerDataSyncList.add(templateDataSync);
                }
            }
        }

        // 删除操作
        triggersDao.deleteByTemplateIdArrays(templateIdArrays);
        itemsDao.deleteByTemplateIdArrays(templateIdArrays);
        int result = templateDao.deleteByPrimaryKeyArrays(templateIdArrays);

        //同步数据插入
        templateDataSyncList.addAll(triggerDataSyncList);
        templateDataSyncList.addAll(itemDataSyncList);
        for (String templateId : templateIdArrays) {
            TemplateDataSync templateDataSync = new TemplateDataSync(templateId,
                    TemplateDataSyncTypeConstant.SYNC_TYPE_TEMPLATE, TemplateDateSyncVo.Operate.D.toString());
            templateDataSyncList.add(templateDataSync);
        }
        templateDataSyncDao.insertBatch(templateDataSyncList);
        return result;
    }

    /**
     * 根据主键更新数据
     *
     * @param templateDTO monitor_templateDTO对象
     * @return int 数据条数
     */
    @Transactional
    public int updateByPrimaryKey(final TemplateDTO templateDTO) {
        if (null == templateDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[templateDTO] is null");
            throw new RuntimeException("param[templateDTO] is null");
        }
        if (StringUtils.isEmpty(templateDTO.getTemplateId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[templateId] is null");
            throw new RuntimeException("param[templateId] is null");
        }
//        templateDeviceDao.deleteByTemplateId(templateDTO.getTemplateId());
//        if(!StringUtils.isEmpty(templateDTO.getDevices())){
//            String[] deviceList = templateDTO.getDevices().split(",");
//            List<TemplateObject> templateDeviceList = Lists.newArrayList();
//            for(String device : deviceList) {
//                TemplateObject templateDevice = new TemplateObject();
//                templateDevice.setTemplateId(templateDTO.getTemplateId());
//                templateDevice.setTemplateDeviceId(UUID.randomUUID().toString());
//                templateDevice.setDeviceId(device);
//                templateDeviceList.add(templateDevice);
//            }
//            templateDeviceDao.insertByBatch(templateDeviceList);
//        }
        Template template = TemplateTransformer.toPo(templateDTO);
        template.setUpdateTime(new Date());
        String userName = RequestAuthContext.getRequestHeadUserName();
        template.setUpdater(userName);
        int result = templateDao.updateByPrimaryKeySelective(template);
        // 插入同步数据
        TemplateDataSync templateDataSync = new TemplateDataSync(templateDTO.getTemplateId(), TemplateDataSyncTypeConstant.SYNC_TYPE_TEMPLATE, TemplateDateSyncVo.Operate.U.toString());
        templateDataSyncDao.insert(templateDataSync);
        return result;
    }

    /**
     * 根据主键查询
     *
     * @param templateId 主键
     * @return TemplateDTO 返回对象
     */
    public TemplateDTO selectByPrimaryKey(final String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[templateId] is null");
            return null;
        }
        Template template = templateDao.selectByPrimaryKey(templateId);
        if (template == null) {
            return null;
        }
        TemplateDTO templateDTO = TemplateTransformer.fromPo(template);
        Items item = new Items();
        item.setTemplateId(templateId);
        List<Items> itemList = itemsDao.select(item);
        List<ItemsDTO> itemsDTOS = ItemsTransformer.fromPo(itemList);
        templateDTO.setItemList(itemsDTOS);
        TemplateObject templateDevice = new TemplateObject();
        templateDevice.setTemplateId(templateId);
//        int deviceNum = templateDeviceDao.selectCount(templateDevice);
//        templateDTO.setDeviceNum(deviceNum);
        return templateDTO;
    }

    /**
     * 根据主键数组查询
     *
     * @param templateIdArrays 主键数组
     * @return List<TemplateDTO> 返回集合对象
     */
    public List<TemplateDTO> selectByPrimaryKeyArrays(final String[] templateIdArrays) {
        if (ArrayUtils.isEmpty(templateIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[templateIdArrays] is null");
            return Collections.emptyList();
        }
        List<Template> listTemplate = templateDao.selectByPrimaryKeyArrays(templateIdArrays);
        List<TemplateDTO> result = Lists.newArrayList();
        for (Template template : listTemplate) {
            TemplateDTO templateDTO = TemplateTransformer.fromPo(template);
            List<TemplateObject> templateObjects = templateObjectDao.selectByTemplateId(template.getTemplateId());
            templateDTO.setTemplateObjectList(TemplateObjectTransformer.fromPo(templateObjects));
            result.add(templateDTO);
        }
        return result;
    }

    /**
     * 根据page对象查询列表信息
     *
     * @param pageRequest page查询对象
     * @return
     */
    @Override
    public PageResponse<TemplateDTO> pageList(PageRequest pageRequest) {
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        Page page = PageUtil.convert(pageRequest);
        page.getParams().put("resFilterMap", resFilterConfig);
        int count = templateDao.pageListCount(page);
        PageResponse<TemplateDTO> pageResponse = new PageResponse<TemplateDTO>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Template> listTemplate = templateDao.pageList(page);
            if (!CollectionUtils.isEmpty(listTemplate)) {
                String[] templateIdArray = new String[listTemplate.size()];
                for (int i = 0; i < listTemplate.size(); i++) {
                    templateIdArray[i] = listTemplate.get(i).getTemplateId();
                }
//            String paramTemplateIds = Joiner.on(",").join(templateIdSet);
                List<Items> itemList = itemsDao.selectByTemplateIdArrays(templateIdArray);
                Map<String, List<ItemsDTO>> itemsDTOMap = ItemsTransformer.toDTOMap(ItemsTransformer.fromPo(itemList));

                List<TemplateDTO> listDTO = TemplateTransformer.fromPo(listTemplate);

                for (TemplateDTO templateDTO : listDTO) {
                    templateDTO.setItemList(itemsDTOMap.get(templateDTO.getTemplateId()));
                }
                pageResponse.setResult(listDTO);
            }
        }
        return pageResponse;
    }

    /**
     * 复制模板
     *
     * @param templateId 模板Id
     * @return TemplateDTO 模板对象
     */
    @Override
    @Transactional
    public TemplateDTO copy(String templateId) {
        Template template = templateDao.selectByPrimaryKey(templateId);
//        TemplateCreateRequest templateCreateRequest = jacksonBaseParse(TemplateCreateRequest.class,
// templateDetailResponse);
        template.setName(template.getName() + "-副本" + DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT));
        Date now = new Date();
        template.setCreateTime(now);
        template.setUpdateTime(now);
        String newTemplateId = UUID.randomUUID().toString();
        template.setTemplateId(newTemplateId);
        templateDao.insert(template);
        //  List<ItemsCreateRequest> itemsCreateRequestList = Lists.newArrayList();
        TemplateObject param = new TemplateObject();
        param.setTemplateId(templateId);
        List<TemplateObject> listTemplateObject = templateObjectDao.select(param);
        //插入模板关联对象
        if (!CollectionUtils.isEmpty(listTemplateObject)) {
            for (TemplateObject templateObject : listTemplateObject) {
                templateObject.setTemplateId(newTemplateId);
                templateObject.setTemplateObjectId(UUID.randomUUID().toString());
            }
            templateObjectDao.insertByBatch(listTemplateObject);
        }
        String[] templateIdArray = {templateId};
        List<Items> listItem = itemsDao.selectByTemplateIdArrays(templateIdArray);
        if (!CollectionUtils.isEmpty(listItem)) {
            Map<String, String> itemIdMap = Maps.newHashMap();
            for (Items item : listItem) {
                item.setTemplateId(newTemplateId);
                String newItemId = UUID.randomUUID().toString();
                itemIdMap.put(item.getItemId(), newItemId);
                item.setItemId(newItemId);
//                itemsCreateRequestList.add(itemsCreateRequest);
            }
            itemsDao.insertByBatch(listItem);
//            ItemsBatchCreateRequest itemsBatchCreateRequest = new ItemsBatchCreateRequest();
//            itemsBatchCreateRequest.setListItem(itemsCreateRequestList);
            if (!CollectionUtils.isEmpty(itemIdMap)) {
                String[] itemIdArrays = new String[itemIdMap.size()];
                itemIdArrays = itemIdMap.keySet().toArray(itemIdArrays);
                List<Triggers> triggerList = triggersDao.selectByItemIdArrays(itemIdArrays);
                for (Triggers triggers : triggerList) {
                    triggers.setItemId(itemIdMap.get(triggers.getItemId()));
                    triggers.setTriggerId(UUID.randomUUID().toString());
                }
                if (!CollectionUtils.isEmpty(triggerList)) {
                    triggersDao.insertByBatch(triggerList);
                }
            }

        }
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setTemplateId(newTemplateId);
        templateDTO.setName(template.getName());
        return templateDTO;
    }

    @Override
    public TemplateDTO selectByTemplateName(String templateName) {
        Template param = new Template();
        param.setName(templateName);
        List<Template> templateList = templateDao.select(param);
        if (!CollectionUtils.isEmpty(templateList)) {
            return TemplateTransformer.fromPo(templateList.get(0));
        }
        return null;
    }

    @Override
    public GeneralResponse zabbixTemplateSync(ZabbixTemplateSyncVo templateSyncVo) {
        String indexType = templateSyncVo.getIndexType();
        // 拼模板名称，避免重复
        String templateId;
        String name = templateSyncVo.getName() + "_" + templateSyncVo.getProxyIdentity() + "_" + templateSyncVo.getZabbixTemplateId();
        templateSyncVo.setName(name);
        Template existTemp = templateDao.selectByZabbixTemplateIdAndProxyIdentity(templateSyncVo.getZabbixTemplateId(), templateSyncVo.getProxyIdentity());
        if (existTemp != null) {
            templateId = existTemp.getTemplateId();
            boolean templateObjUpdateFlag = true;
            List<String> newHostList = Lists.newArrayList();
            List<String> oldHostList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(templateSyncVo.getHosts())) {
                templateSyncVo.getHosts().stream().forEach(item -> {
                    newHostList.add(item.getPool() + ":" + item.getIp());
                });
            }
            List<MirrorHostVO> hostList = hostDao.selectByTemplateId(templateId);
            if (!CollectionUtils.isEmpty(hostList)) {
                hostList.stream().forEach(item -> {
                    oldHostList.add(item.getPool() + ":" + item.getIp());
                });
            }
            if (newHostList.containsAll(oldHostList) && oldHostList.containsAll(newHostList)) {
                templateObjUpdateFlag = false;
            }

            if (!existTemp.equals(templateSyncVo) || templateObjUpdateFlag) {
                existTemp.setName(name);
                existTemp.setDescription(templateSyncVo.getDescription());
                existTemp.setUpdateTime(new Date());
                templateDao.updateByPrimaryKeySelective(existTemp);
                // 插入模板对象
                templateObjectDao.deleteByTemplateIds(new String[]{templateId});
                if (!newHostList.isEmpty()) {
                    addTemplateObject(templateSyncVo, templateId);
                }
                //插入同步数据
                TemplateDataSync templateDataSync = new TemplateDataSync(existTemp.getTemplateId(), TemplateDataSyncTypeConstant.SYNC_TYPE_TEMPLATE, TemplateDateSyncVo.Operate.U.toString());
                templateDataSyncDao.insert(templateDataSync);
            }
            List<ItemsVO> itemList = templateSyncVo.getItems();

//            if (!CollectionUtils.isEmpty(itemList)) {
//                itemList.stream().forEach(item -> {
//                    ItemsDTO itemsDTO = new ItemsDTO();
//                    BeanUtils.copyProperties(item, itemsDTO);
//                    itemsDTO.setTemplateId(existTemp.getTemplateId());
//                    itemsDTOList.add(itemsDTO);
//                });
//            }
            List<ItemsDTO> existItems = itemsBiz.selectByTemplateIdArrays(new String[]{existTemp.getTemplateId()});
            List<ItemsDTO> itemsDTOList = Lists.newArrayList();
            List<String> responseItemIdList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(itemList)) {
                for (ItemsVO itemsVO : itemList) {
                    ItemsDTO itemsDTO = new ItemsDTO();
                    BeanUtils.copyProperties(itemsVO, itemsDTO);
                    ItemsDTO param = new ItemsDTO();
                    param.setTemplateId(existTemp.getTemplateId());
                    param.setKey(itemsVO.getKey());
                    param.setZabbixItemId(itemsVO.getZabbixItemId());
                    List<ItemsDTO> itemsDTOS = itemsBiz.select(param);
                    if (CollectionUtils.isEmpty(itemsDTOS)) {
                        itemsDTO.setTemplateId(templateId);
                        itemsDTO.setType("INDEX");
                        itemsDTOList.add(itemsDTO);
                    } else {
                        // 修改指标
                        itemsDTO.setItemId(itemsDTOS.get(0).getItemId());
                        // 设置指标id缓存数据
                        responseItemIdList.add(itemsDTOS.get(0).getItemId());
                        if (!itemsDTOS.get(0).equals(itemsDTO)) {
                            itemsBiz.updateByPrimaryKey(itemsDTO);
                        }
                    }
                }
            }
            // 批量删除
            List<String> existItemIds = existItems.stream().map(ItemsDTO::getItemId).collect(Collectors.toList());
            existItemIds.removeAll(responseItemIdList);
            if (existItemIds.size() > 0) {
                itemsBiz.deleteByPrimaryKeyArrays(existItemIds.toArray(new String[0]));
            }
            // 批量新增
            if (itemsDTOList.size() > 0) {
                List<ItemsDTO> respItemList = itemsBiz.insertByBatch(itemsDTOList);
                responseItemIdList.addAll(respItemList.stream().map(ItemsDTO::getItemId).collect(Collectors.toList()));
            }
        } else {
            // 创建模板
            TemplateDTO template = new TemplateDTO();
            BeanUtils.copyProperties(templateSyncVo, template);
            template.setName(name);
            templateId = insert(template);
            List<ItemsVO> itemList = templateSyncVo.getItems();
            if (!CollectionUtils.isEmpty(itemList)) {
                List<ItemsDTO> itemsDTOList = Lists.newArrayList();
                itemList.stream().forEach(item -> {
                    ItemsDTO itemsDTO = new ItemsDTO();
                    BeanUtils.copyProperties(item, itemsDTO);
                    itemsDTO.setTemplateId(templateId);
                    itemsDTO.setType("INDEX");
                    itemsDTOList.add(itemsDTO);
                });
                List<ItemsDTO> respItemList = itemsBiz.insertByBatch(itemsDTOList);
//                responseItemIdList.addAll(respItemList.stream().map(ItemsDTO::getItemId).collect(Collectors.toList()));
            }
            if (!CollectionUtils.isEmpty(templateSyncVo.getHosts())) {
                addTemplateObject(templateSyncVo, templateId);
            }
        }
        RBucket<List<String>> bucket = redissonClient.getBucket(ZABBIX_TEMPLATE_SYCN_CACHE);
        if (indexType.equals("START")) {
            if (bucket.get() != null) {
                bucket.delete();
            }
            List<String> tempList = Lists.newArrayList(templateId);
            bucket.set(tempList);
        }  else if (indexType.equals("END")) {
            List<String> tempList = bucket.get();
            tempList.add(templateId);
            bucket.set(tempList);
            List<String> templateIdCache = bucket.get();
            Template queryParam = new Template();
            List<Template> existTemplateList = templateDao.selectZabbixSyncTemplate();
            List<String> existTemplateIdList = existTemplateList.stream().map(Template::getTemplateId).collect(Collectors.toList());
            existTemplateIdList.removeAll(templateIdCache);
            if (existTemplateIdList.size() > 0) {
                deleteByPrimaryKeyArrays(existTemplateIdList.toArray(new String[0]));
            }
            bucket.delete();
        } else {
            List<String> tempList = bucket.get();
            tempList.add(templateId);
            bucket.set(tempList);
        }
        return new GeneralResponse();
    }

    @Override
    public List<TemplateDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] templateIdArray, String proxyIdentity) {
        if (ArrayUtils.isEmpty(templateIdArray)) {
            LOGGER.warn("method[selectByPrimaryKeyArraysAndProxyIdentity] param[templateIdArrays] is null");
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(proxyIdentity)) {
            LOGGER.warn("method[selectByPrimaryKeyArraysAndProxyIdentity] param[proxyIdentity] is null");
            return Collections.emptyList();
        }
        List<Template> listTemplate = templateDao.selectByPrimaryKeyArraysAndProxyIdentity(templateIdArray, proxyIdentity);
        List<TemplateDTO> result = Lists.newArrayList();
        for (Template template : listTemplate) {
            TemplateDTO templateDTO = TemplateTransformer.fromPo(template);
            List<TemplateObject> templateObjects = templateObjectDao.selectByTemplateId(template.getTemplateId());
            templateDTO.setTemplateObjectList(TemplateObjectTransformer.fromPo(templateObjects));
            result.add(templateDTO);
        }
        return result;
    }

    private void addTemplateObject(ZabbixTemplateSyncVo templateSyncVo, String templateId) {
        List<TemplateObject> addObjectList = Lists.newArrayList();
        for (MirrorHostVO host : templateSyncVo.getHosts()) {
            MirrorHostVO respHost = hostDao.selectByIpAndPool(host.getIp(), host.getPool());
            if (respHost != null) {
                TemplateObject templateObject = new TemplateObject();
                templateObject.setTemplateId(templateId);
                templateObject.setObjectType("3");
                templateObject.setObjectId(respHost.getId().toString());
                templateObject.setTemplateObjectId(UUID.randomUUID().toString());
                addObjectList.add(templateObject);
            }
        }
        if (!addObjectList.isEmpty()) {
            templateObjectDao.insertByBatch(addObjectList);
        }
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private TemplateDao templateDao;

//    @Autowired
//    private TemplateObjectDao templateDeviceDao;

    @Autowired
    private ItemsDao itemsDao;

    @Autowired
    private TriggersDao triggersDao;

    @Autowired
    private TemplateDataSyncDao templateDataSyncDao;

    @Autowired
    private TemplateObjectDao templateObjectDao;

    @Autowired
    private ItemsBizImpl itemsBiz;

    @Autowired
    private HostDao hostDao;

} 
