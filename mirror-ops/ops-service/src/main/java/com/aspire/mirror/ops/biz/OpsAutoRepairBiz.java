/**
 * 项目名： ops-service
 * <p/>
 * <p>
 * 文件名:  OpsAutoRepairBiz.java
 * <p/>
 * <p>
 * 功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @date 2020年2月19日
 * @version V1.0 <p/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 */
package com.aspire.mirror.ops.biz;

import static com.aspire.mirror.ops.api.domain.OpsGroupObjectTypeEnum.AP_SCHEME;
import static com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO.BIZ_CLASSIFY_AUTO_REPAIR;
import static com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO.VALUE_TYPE_STRING;
import static com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO.SYMBOL_CONTAIN;
import static com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO.SYMBOL_EQUAL;
import static com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO.SYMBOL_NOT_EQUAL;
import static com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO.SYMBOL_SINGLE_EQUAL;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult;
import com.aspire.mirror.ops.api.domain.OpsAutoRepairStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsTriggerWayEnum;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO.OpsAutoRepairItemTypeQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO.SchemePipeExecStatusEnum;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO.OpsApExecHistoryQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO.OpsAutoRepairSchemeQueryModel;
import com.aspire.mirror.ops.biz.model.AutoRepairAlertMessage;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail.AspNodeResultFlagEnum;
import com.aspire.mirror.ops.biz.model.OpsGroupObjectHandler;
import com.aspire.mirror.ops.biz.model.OpsPipelineExecuteTimeoutEvent;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.OpsAutoRepairDao;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.OpsApItem;
import com.aspire.mirror.ops.domain.OpsApItemType;
import com.aspire.mirror.ops.domain.OpsApSchemeItem;
import com.aspire.mirror.ops.domain.OpsAutoRepairExecuteLog;
import com.aspire.mirror.ops.domain.OpsStepInstance;
import com.aspire.mirror.ops.exception.OpsServiceException;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 项目名称: ops-service
 * <p/>
 *
 * 类名: OpsAutoRepairBiz
 * <p/>
 *
 * 类功能描述: 故障自愈业务类
 * <p/>
 *
 * @author pengguihua
 *
 * @date 2020年2月18日
 *
 * @version V1.0
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有
 *
 */
@Slf4j
@Service
@Transactional
public class OpsAutoRepairBiz {
    public static final String AUTO_REPAIR_BUCKET_PLACE = "apItemGroup:scheme_%s_agent_%s-%s";
    public static final String AUTO_REPAIR_LOCK_PLACE = "lock:scheme_%s_agent_%s-%s";
    @Autowired
    private OpsActionBiz opsActionBiz;
    @Autowired
    private AgentHostDataBiz agentHostBiz;
    @Autowired
    private OpsBaseDataBiz baseDataBiz;
    @Autowired
    private OpsAutoRepairDao dao;
    @Autowired
    private OpsGroupObjectHandler opsGroupObjectHandler;
    @Autowired(required = false)
    private RedissonClient redissonClient;

    /**
     * 功能描述: 同步自愈指标
     * <p>
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void syncApItemTypeList(List<OpsApItemTypeDTO> apItemTypeList) {
        apItemTypeList.forEach(apItemType -> {
            try {
                syncApItemTypeOnebyOne(apItemType);
            } catch (Exception e) {
                log.error(null, e);
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void syncApItemTypeOnebyOne(OpsApItemTypeDTO apItemTypeDto) {
        List<OpsApItem> schemeReferApItemList = dao.queryReferApItemListByItemTypeKeys(apItemTypeDto.getSourceMark(), apItemTypeDto.getApItemType());

        if (OpsApItemTypeDTO.MANAGE_TYPE_DELETE.equals(apItemTypeDto.getManageType()) && CollectionUtils.isEmpty(schemeReferApItemList)) {
            dao.deleteAllOpsApItemsByApTypeKeys(apItemTypeDto.getSourceMark(), apItemTypeDto.getApItemType());
            dao.deleteApItemTypeByUqKeys(apItemTypeDto.getSourceMark(), apItemTypeDto.getApItemType());
            return;
        }

        OpsApItemType updateData = OpsApItemType.parse(apItemTypeDto);
        OpsApItemType existApItemType = dao.queryApItemTypeByUqKeys(apItemTypeDto.getSourceMark(), apItemTypeDto.getApItemType());

        if (OpsApItemTypeDTO.MANAGE_TYPE_UPDATE.equals(apItemTypeDto.getManageType())
                || OpsApItemTypeDTO.MANAGE_TYPE_ALL.equals(apItemTypeDto.getManageType())) {
            if (existApItemType == null) {
                updateData.setFirstSyncTime(new Date());
                dao.insertOpsApItemType(updateData);
            } else {
                updateData.setId(existApItemType.getId());
                updateData.setLastUpdateTime(new Date());
                dao.updateOpsApItemType(updateData);
            }
        }

        syncChildApItemList(updateData, schemeReferApItemList);
    }

    private void syncChildApItemList(OpsApItemType apItemType, List<OpsApItem> schemeReferApItemList) {
        List<OpsApItem> syncApItemList = OpsApItem.parse(apItemType.getApItemList());

        for (OpsApItem apItem : syncApItemList) {
            if (OpsApItemTypeDTO.MANAGE_TYPE_DELETE.equals(apItem.getManageType())) {
                if (!schemeReferApItemList.contains(apItem)) {
                    dao.deleteOpsApItemByUqKeys(apItemType.getId(), apItem.getApItem());
                }
                continue;
            }

            OpsApItem existApItem = dao.queryOpsAutoRepairItemByUqKeys(apItemType.getSourceMark(), apItemType.getApItemType(), apItem.getApItem());
            if (existApItem == null) {
                apItem.setFirstSyncTime(new Date());
                dao.insertOpsApItem(apItem);
                continue;
            }

            apItem.setId(existApItem.getId());
            apItem.setLastUpdateTime(new Date());
            dao.updateOpsApItem(apItem);
        }

        // 如果为全量同步, 同步之后缺失掉的apItem需要删除
        if (OpsApItemTypeDTO.MANAGE_TYPE_ALL.equals(apItemType.getManageType())) {
            List<OpsApItem> dbApItemList = dao.queryOpsAutoRepairItemListByItemTypeId(apItemType.getId());
            for (int i = dbApItemList.size() - 1; i >= 0; i--) {
                OpsApItem dbApItem = dbApItemList.get(i);
                if (!syncApItemList.contains(dbApItem) && !schemeReferApItemList.contains(dbApItem)) {
                    dao.deleteOpsApItemByUqKeys(dbApItem.getApItemTypeId(), dbApItem.getApItem());
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsApItemTypeDTO> queryOpsAutoRepairItemTypeList(OpsAutoRepairItemTypeQueryModel queryParam) {
        List<OpsApItemType> queryList = dao.queryOpsAutoRepairItemTypeList(queryParam);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageListQueryResult<OpsApItemTypeDTO>(0, new ArrayList<OpsApItemTypeDTO>());
        }
        Integer totalCount = dao.queryOpsAutoRepairItemTypeTotalSize(queryParam);
        List<OpsApItemTypeDTO> resultList = queryList.stream().map(itemType -> {
            return OpsApItemTypeDTO.class.cast(itemType);
        }).collect(Collectors.toList());
        return new PageListQueryResult<OpsApItemTypeDTO>(totalCount, resultList);
    }

    @Transactional(readOnly = true)
    public List<OpsApItemDTO> queryOpsAutoRepairItemListByItemType(Long itemTypeId) {
        List<OpsApItem> itemList = dao.queryOpsAutoRepairItemListByItemTypeId(itemTypeId);
        return itemList.stream().map(item -> {
            return OpsApItemDTO.class.cast(item);
        }).collect(Collectors.toList());
    }


    /**
     * 功能描述: 保存自愈方案
     * <p>
     * @param dto
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public GeneralResponse saveOpsAutoRepairScheme(OpsAutoRepairSchemeDTO dto) {
        // 如果方案关联的 指标或作业存在删除, 则先删除
        if (CollectionUtils.isNotEmpty(dto.getRemoveApItemList())) {
            dto.getRemoveApItemList().forEach(schemeItemId -> {
                dao.deleteOpsApSchemeItemById(schemeItemId);
            });
        }
        if (CollectionUtils.isNotEmpty(dto.getRemovePipelineList())) {
            dto.getRemovePipelineList().forEach(schemePipelineId -> {
                dao.deleteOpsApSchemePipelineById(schemePipelineId);
            });
        }

        Pair<Boolean, String> checkResult = validateSchemeData(dto);
        if (!checkResult.getKey().booleanValue()) {
            // 数据验证失败, 显式回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GeneralResponse(checkResult.getLeft(), checkResult.getRight());
        }

        String currUser = RequestAuthContext.getRequestHeadUserName();
        if (dto.getSchemeId() == null) {
            dto.setCreater(currUser);
            dto.setCreateTime(new Date());
            dao.insertAutoRepairScheme(dto);
        } else {
            dto.setUpdater(currUser);
            dto.setUpdateTime(new Date());
            dao.updateAutoRepairScheme(dto);
        }
        for (OpsApSchemeItemDTO referItem : dto.getReferApItemList()) {
            referItem.setSchemeId(dto.getSchemeId());
            if (referItem.getId() == null) {
                dao.insertOpsApSchemeItem(OpsApSchemeItem.parse(referItem));
            } else {
                dao.updateOpsApSchemeItem(OpsApSchemeItem.parse(referItem));
            }
        }
        for (OpsApSchemePipelineDTO referPipe : dto.getReferApPipelineList()) {
            referPipe.setSchemeId(dto.getSchemeId());
            if (referPipe.getId() == null) {
                dao.insertOpsApSchemePipeline(referPipe);
            } else {
                dao.updateOpsApSchemePipeline(referPipe);
            }
        }
        opsGroupObjectHandler.saveOpsGroup(dto, dto.getSchemeId(), AP_SCHEME.getStatusCode());
        return new GeneralResponse(true, null, dto);
    }

    /**
     * 功能描述: 验证自愈方案数据
     * <p>
     * @param dto
     * @return
     */
    private Pair<Boolean, String> validateSchemeData(OpsAutoRepairSchemeDTO dto) {
        // 检查方案名称是否已经存在
        OpsAutoRepairSchemeDTO existScheme = dao.queryAutoRepairSchemeByName(dto.getSchemeName());
        if (existScheme != null) {
            if (dto.getSchemeId() == null ||
                    (dto.getSchemeId() != null
                            && !dto.getSchemeId().equals(existScheme.getSchemeId()))) {
                return Pair.of(false, "The auto repair scheme name is already exists.");
            }
        }

        if (CollectionUtils.isEmpty(dto.getReferApItemList())) {
            return Pair.of(false, "No auto repair items are configed.");
        }
        if (CollectionUtils.isEmpty(dto.getReferApPipelineList())) {
            return Pair.of(false, "No pipelines are configed.");
        }

        // 经过与需求确认, 同1个指标不能被2个方案引用, 此处检查自愈指标是否已经被其它方案引用过
        for (OpsApSchemeItemDTO referItem : dto.getReferApItemList()) {
            OpsApSchemeItemDTO existItem = dao.querySchemeReferItemByUqKeys(referItem.getApItemTypeId(), referItem.getApItem());
            if (existItem != null && !existItem.getSchemeId().equals(referItem.getSchemeId())) {
                String tip = format("The item '%s' has already refered by the scheme with name %s.",
                        existItem.getApItem(), existItem.getSchemeName());
                return Pair.of(false, tip);
            }
        }

        if (dto.getSchemeId() == null) {
            return Pair.of(true, null);
        }

        // 检查同一个方案是否重复引用同一个作业
        for (OpsApSchemePipelineDTO referPipe : dto.getReferApPipelineList()) {
            OpsApSchemePipelineDTO existPipe = dao.queryReferPipelineByUqKeys(dto.getSchemeId(), referPipe.getPipelineId());
            if (existPipe == null) {
                continue;
            }
            if (!existPipe.getId().equals(referPipe.getId())) {
                String tip = format("The pipeline of name '%s' is already configured for the scheme.", existPipe.getPipelineName());
                return Pair.of(false, tip);
            }
        }
        return Pair.of(true, null);
    }

    @Transactional
    public GeneralResponse removeOpsAutoRepairScheme(List<Long> schemeIdList) {
        for (Long schemeId : schemeIdList) {
            dao.deleteAllOpsApSchemeItemsBySchemeId(schemeId);
            dao.deleteAllOpsApSchemePipelineBySchemeId(schemeId);
            dao.removeAutoRepairSchemeById(schemeId);
            //删除分组信息
            opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(schemeId, AP_SCHEME.getStatusCode());
        }
        return new GeneralResponse();
    }

    public PageListQueryResult<OpsAutoRepairSchemeDTO> queryOpsAutoRepairSchemeList(OpsAutoRepairSchemeQueryModel queryParam) {
        List<OpsAutoRepairSchemeDTO> queryList = dao.queryAutoRepairSchemeList(queryParam);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageListQueryResult<OpsAutoRepairSchemeDTO>(0, queryList);
        }
        Integer totalCount = dao.queryAutoRepairSchemeTotalSize(queryParam);
        queryList.forEach(scheme -> {
            scheme.setReferApPipelineList(dao.queryReferPipelineListBySchemeId(scheme.getSchemeId()));
            List<OpsApSchemeItem> referItemList = dao.queryReferItemListBySchemeId(scheme.getSchemeId());
            scheme.setReferApItemList(OpsApSchemeItem.reverseParse(referItemList));
        });
        return new PageListQueryResult<OpsAutoRepairSchemeDTO>(totalCount, queryList);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OpsAutoRepairExecuteLog saveOpsAutoRepairExecuteResult(final OpsAutoRepairExecuteLog autoRepairResult) {
        if (autoRepairResult.getId() == null) {
            dao.insertOpsAutoRepairExecuteResult(autoRepairResult);
        } else {
            dao.updateOpsAutoRepairExecuteResult(autoRepairResult);
        }
        return autoRepairResult;
    }

    @Transactional(readOnly = true)
    public Integer queryOpsAutoRepairExecCount(OpsApExecHistoryQueryModel queryParam) {
        return dao.queryOpsAutoRepairExecHistoryListTotalSize(queryParam);
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsAutoRepairExecuteLogDTO> queryOpsAutoRepairExecHistory(OpsApExecHistoryQueryModel queryParam) {
        List<OpsAutoRepairExecuteLog> queryList = dao.queryOpsAutoRepairExecHistoryList(queryParam);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageListQueryResult<OpsAutoRepairExecuteLogDTO>(0, null);
        }
        Integer totalCount = dao.queryOpsAutoRepairExecHistoryListTotalSize(queryParam);
        List<OpsAutoRepairExecuteLogDTO> parseList = queryList.stream().map(execLog -> {
            return OpsAutoRepairExecuteLogDTO.class.cast(execLog);
        }).collect(Collectors.toList());
        return new PageListQueryResult<OpsAutoRepairExecuteLogDTO>(totalCount, parseList);
    }

    @Transactional(readOnly = true)
    public OpsApSchemePipelineDTO queryReferPipelineBySchemeIdAndOrder(Long schemeId, Integer pipeOrder) {
        return dao.queryReferPipelineBySchemeIdAndOrder(schemeId, pipeOrder);
    }

    /**
     * 功能描述: 告警消息自愈处理
     * <p>
     * @param alertMsg
     */
    @Transactional
    public void handleAlertMessage4AutoRepair(AutoRepairAlertMessage alertMsg) {
        log.debug("handleAlertMessage4AutoRepair received alertMsg: {}", alertMsg);
        OpsApSchemeItemDTO schemeItem = dao.querySchemeReferItemBySourceItemType(
                alertMsg.getSourceMark(), alertMsg.getApItemType(), alertMsg.getApItem());
        if (schemeItem == null
                || !judgeItemValueAutoRepairMatch(alertMsg, schemeItem)) {
            return;
        }

        AgentHostInfo agentHost = agentHostBiz.queryAgentDataByPoolAndAgentIP(alertMsg.getAssetPool(), alertMsg.getDeviceIp());
        if (agentHost == null) {
            log.error("There is no agent host with pool:{}, agentIp:{}, the AutoRepairAlertMessage process will be ignored.",
                    alertMsg.getAssetPool(), alertMsg.getDeviceIp());
            return;
        }
        OpsAutoRepairSchemeDTO matchScheme = dao.queryAutoRepairSchemeById(schemeItem.getSchemeId());
        List<OpsApSchemeItem> referItemList = dao.queryReferItemListBySchemeId(matchScheme.getSchemeId());

        // 自愈方案只关联了一个指标时, 直接触发自愈作业流程
        if (referItemList.size() == 1) {
            activeAutoRepairPipelines(agentHost, matchScheme, schemeItem, Collections.singletonList(alertMsg));
            return;
        }

        if (redissonClient == null) {
            String errorTipe = "Please init the redissonClient, now the process of AlertMessage4AutoRepair will be aborted.";
            log.error(errorTipe);
            throw new OpsServiceException(errorTipe);
        }
        // 自愈方案关联了多个自愈指标
        processMultiItemsApScheme(alertMsg, agentHost, matchScheme, schemeItem, referItemList);
    }

    /**
     * 功能描述: 处理自愈方案关联存在多个指标时的自愈匹配业务: 即在设定的时间区间内所有指标都匹配满足时,才触发自愈作业流程
     * <p>
     */
    private void processMultiItemsApScheme(AutoRepairAlertMessage alertMsg, AgentHostInfo agentHost,
                                           OpsAutoRepairSchemeDTO matchScheme, OpsApSchemeItemDTO currSchemeItem, List<OpsApSchemeItem> referItemList) {

        long nowTime = System.currentTimeMillis();
        alertMsg.setCacheTimeMark(nowTime);        // 设置缓存markTime

        RLock lock = null;
        try {
            String lockKey = format(AUTO_REPAIR_LOCK_PLACE, matchScheme.getSchemeId(), agentHost.getProxyIdentity(), agentHost.getAgentIp());
            lock = redissonClient.getLock(lockKey);
            lock.lock();

            String bucketName = format(AUTO_REPAIR_BUCKET_PLACE, matchScheme.getSchemeId(), agentHost.getProxyIdentity(), agentHost.getAgentIp());
            TypeReference<List<AutoRepairAlertMessage>> typeRef = new TypeReference<List<AutoRepairAlertMessage>>() {
            };
            RBucket<List<AutoRepairAlertMessage>> bucket = redissonClient.getBucket(bucketName, new TypedJsonJacksonCodec(typeRef));

            if (!bucket.isExists()) {
                bucket.set(Collections.singletonList(alertMsg));
                bucket.expire(matchScheme.getMultiItemsApplyTime(), TimeUnit.MINUTES);
                return;
            }

            List<AutoRepairAlertMessage> apAlertMsgList = bucket.get();
            apAlertMsgList.remove(alertMsg);
            apAlertMsgList.add(alertMsg);

            // 移除超过设定时间跨度的自愈指标
            for (int i = apAlertMsgList.size() - 1; i >= 0; i--) {
                if (nowTime - apAlertMsgList.get(i).getCacheTimeMark()
                        > TimeUnit.MINUTES.toMillis(matchScheme.getMultiItemsApplyTime())) {
                    apAlertMsgList.remove(i);
                }
            }

            if (referItemList.size() > apAlertMsgList.size()) {
                bucket.set(apAlertMsgList);
                bucket.expire(matchScheme.getMultiItemsApplyTime(), TimeUnit.MINUTES);
                return;
            }

            // 检测自愈方案关联的所有指标都匹配存在
            for (OpsApSchemeItem referItem : referItemList) {
                AutoRepairAlertMessage find = new AutoRepairAlertMessage();
                find.setAssetPool(agentHost.getPool());
                find.setDeviceIp(agentHost.getAgentIp());
                find.setSourceMark(referItem.getSourceMark());
                find.setApItemType(referItem.getApItemType());
                find.setApItem(referItem.getApItem());
                if (!apAlertMsgList.contains(find)) {
                    bucket.set(apAlertMsgList);
                    bucket.expire(matchScheme.getMultiItemsApplyTime(), TimeUnit.MINUTES);
                    return;
                }
            }

            // 在指定的时间跨度内, 所有自愈指标都匹配到, 则触发自愈流程
            bucket.delete();
            activeAutoRepairPipelines(agentHost, matchScheme, currSchemeItem, apAlertMsgList);
        } catch (Throwable e) {
            log.error("Error when run step timeout check job.", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 功能描述: 激活自愈作业流程
     * <p>
     * @param agentHost
     * @param matchScheme
     * @param schemeItem
     * @param alertMsgList
     */
    private void activeAutoRepairPipelines(AgentHostInfo agentHost, OpsAutoRepairSchemeDTO matchScheme,
                                           OpsApSchemeItemDTO schemeItem, List<AutoRepairAlertMessage> alertMsgList) {

        OpsAutoRepairExecuteLog apExecuteLog = new OpsAutoRepairExecuteLog();
        apExecuteLog.setSchemeId(matchScheme.getSchemeId());
        apExecuteLog.setSchemeName(matchScheme.getSchemeName());
        apExecuteLog.setSourceMark(schemeItem.getSourceMark());
        apExecuteLog.setApItemType(schemeItem.getApItemType());
        apExecuteLog.setApItemTypeName(schemeItem.getApItemTypeName());
        apExecuteLog.setApItem(schemeItem.getApItem());
        apExecuteLog.setApItemName(schemeItem.getApItemName());
        apExecuteLog.setAssetPool(agentHost.getPool());
        apExecuteLog.setAgentIp(agentHost.getAgentIp());
        apExecuteLog.setExtendDataMapJson(JsonUtil.toJacksonJson(alertMsgList));
        apExecuteLog.setTriggerTime(new Date());
        dao.insertOpsAutoRepairExecuteResult(apExecuteLog);

        // 激活第一个作业
        List<OpsApSchemePipelineDTO> referPipelineList = dao.queryReferPipelineListBySchemeId(matchScheme.getSchemeId());
        activeAutoRepairSchemePipeline(apExecuteLog, referPipelineList.get(0), agentHost);
    }

    /**
     * 功能描述: 执行指定自愈作业
     * <p>
     * @param apExecuteLog
     * @param schemePipe
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void activeAutoRepairSchemePipeline(
            OpsAutoRepairExecuteLog apExecuteLog, OpsApSchemePipelineDTO schemePipe, AgentHostInfo agentHost) {

        final List<String> targetHostList = new ArrayList<>();
        targetHostList.add(agentHost.getConcatHost());
        final Map<String, Object> replaceAttrs = new HashMap<>();
        replaceAttrs.put("target_hosts", targetHostList);

        OpsMessageExtendMeta extendMeta = new OpsMessageExtendMeta(BIZ_CLASSIFY_AUTO_REPAIR);
        GeneralResponse executeResp = opsActionBiz.executePipeline(
                schemePipe.getPipelineId(), OpsTriggerWayEnum.TRIGGER_BY_API, replaceAttrs, extendMeta);
        Long pipeInstanceId = Long.valueOf(String.valueOf(executeResp.getBizData()));
        OpsPipelineInstanceDTO pipeInstance = baseDataBiz.queryOpsPipelineInstanceById(pipeInstanceId);

        apExecuteLog.setCurrentPipelineOrder(schemePipe.getOrder());
        apExecuteLog.setCurrentPipelineInstanceId(pipeInstance.getPipelineInstanceId());
        apExecuteLog.setCurrentPipelineInstanceName(pipeInstance.getPipelineInstanceName());
        apExecuteLog.getExecutedPipeInstIdList().add(pipeInstance.getPipelineInstanceId());
        apExecuteLog.getExecutedPipeInstNameList().add(pipeInstance.getPipelineInstanceName());
        apExecuteLog.setStatus(pipeInstance.getStatus());
        dao.updateOpsAutoRepairExecuteResult(apExecuteLog);
    }

    /**
     * 功能描述: 判断自愈指标值是否匹配
     * <p>
     * @param alertMsg
     * @param schemeItem
     * @return
     */
    private boolean judgeItemValueAutoRepairMatch(AutoRepairAlertMessage alertMsg, OpsApSchemeItemDTO schemeItem) {
        String apItemValue = alertMsg.getAlertItemValue();
        if (apItemValue == null) {
            return false;
        }
        OpsApItem opsApItem = dao.queryOpsAutoRepairItemByUqKeys(
                schemeItem.getSourceMark(), schemeItem.getApItemType(), schemeItem.getApItem());

        // 从值中剔除单位
        String apItemValUnit = opsApItem.getApItemValueUnit();
        if (StringUtils.isNoneBlank(apItemValUnit)) {
            int unitIdx = apItemValue.toUpperCase().lastIndexOf(apItemValUnit.toUpperCase());
            if (unitIdx > 0) {
                apItemValue = apItemValue.substring(0, unitIdx).trim();
            }
        }

        // 判断包含
        String judgeSymbol = schemeItem.getJudgeSymbol();
        if (SYMBOL_SINGLE_EQUAL.equals(judgeSymbol)) {
            judgeSymbol = SYMBOL_EQUAL;
        }
        if (SYMBOL_CONTAIN.equals(judgeSymbol)) {
            if (apItemValue.contains(schemeItem.getJudgeValue())) {
                return true;
            }
            return false;
        }

        // 判断字符串类型的值相等
        if (VALUE_TYPE_STRING.equals(opsApItem.getApItemValueType())
                && SYMBOL_EQUAL.equals(judgeSymbol)) {
            return apItemValue.equals(schemeItem.getJudgeValue());
        }

        if (VALUE_TYPE_STRING.equals(opsApItem.getApItemValueType())
                && SYMBOL_NOT_EQUAL.equals(judgeSymbol)) {
            return !apItemValue.equals(schemeItem.getJudgeValue());
        }

        // 判断数值类型表达式
        apItemValue = apItemValue.trim();
        String key = "_key_apItemVal_";
        String expression = key + judgeSymbol + schemeItem.getJudgeValue();
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Map<String, Object> env = Collections.singletonMap(key, Float.valueOf(apItemValue));
        // 执行表达式
        Boolean result = (Boolean) compiledExp.execute(env);
        return result;
    }

    /**
     * 功能描述: 处理自愈方案中的 作业完成
     * <p>
     * @param currpipeInst
     * @param stepInstHostResult
     */
    public void handleApSchemePipelineFinish(OpsPipelineInstanceDTO currpipeInst, OpsAgentStepInstanceResult stepInstHostResult) {
        OpsAutoRepairExecuteLog apExecuteLog
                = dao.queryAutoRepairExecuteResultByPipelineInstanceId(currpipeInst.getPipelineInstanceId());
        if (apExecuteLog.isExecuteResultOver()) {
            log.warn("The auto repair log with id {}, schemeId {} is over, status is {}, the step result process will ignore.",
                    apExecuteLog.getId(), apExecuteLog.getSchemeId(), apExecuteLog.getStatus());
            return;
        }

        OpsApSchemePipelineDTO schemeCurrPipe = queryReferPipelineBySchemeIdAndOrder(
                apExecuteLog.getSchemeId(), apExecuteLog.getCurrentPipelineOrder());
        OpsApSchemePipelineDTO schemeNextPipe = queryReferPipelineBySchemeIdAndOrder(
                apExecuteLog.getSchemeId(), apExecuteLog.getCurrentPipelineOrder() + 1);

        // 不存在下一个作业, 则整个自愈作业流程已经完成
        if (schemeNextPipe == null) {
            handleAutoRepairProcessOver(stepInstHostResult, apExecuteLog, currpipeInst);
            return;
        }

        AgentHostInfo agentHost = agentHostBiz.queryAgentDataByPoolAndAgentIP(apExecuteLog.getAssetPool(), apExecuteLog.getAgentIp());
        boolean finishStatusMatch = judgeCurrPipeFinishStatusMatch(stepInstHostResult, apExecuteLog, currpipeInst, schemeCurrPipe);
        Integer finishAction = schemeCurrPipe.getFinishJudgeAction();

        if (finishStatusMatch && OpsApSchemePipelineDTO.FINISH_ACTION_OVER == finishAction) {
            handleAutoRepairProcessOver(stepInstHostResult, apExecuteLog, currpipeInst);
        } else if (finishStatusMatch && OpsApSchemePipelineDTO.FINISH_ACTION_CONTINUE == finishAction) {
            // 激活下一个作业
            activeAutoRepairSchemePipeline(apExecuteLog, schemeNextPipe, agentHost);
        } else if (finishStatusMatch && OpsApSchemePipelineDTO.FINISH_ACTION_MANUAL == finishAction) {
            // 设定当前自愈流程为 "人工干预", 后续由人工触发是否继续执行的动作
            apExecuteLog.setStatus(OpsAutoRepairStatusEnum.STATUS_6.getStatusCode());
            if (stepInstHostResult != null) {
                apExecuteLog.setResponse(stepInstHostResult.getOpsLog());
            }
            saveOpsAutoRepairExecuteResult(apExecuteLog);
        } else if (!finishStatusMatch && OpsApSchemePipelineDTO.FINISH_ACTION_OVER == finishAction) {
            // 已与需求确认：当动作设定为“自动终止”, 但结果不能匹配时, 继续执行下一个 作业
            activeAutoRepairSchemePipeline(apExecuteLog, schemeNextPipe, agentHost);
        } else {
            handleAutoRepairProcessOver(stepInstHostResult, apExecuteLog, currpipeInst);
        }
    }

    /**
     * 功能描述: 判断自愈流程当前作业完成状态是否和配置相匹配
     * <p>
     * @param stepInstHostResult
     * @param apExecuteLog
     * @param currpipeInst
     * @param schemeCurrPipe
     */
    private boolean judgeCurrPipeFinishStatusMatch(OpsAgentStepInstanceResult stepInstHostResult, OpsAutoRepairExecuteLog apExecuteLog,
                                                   OpsPipelineInstanceDTO currpipeInst, OpsApSchemePipelineDTO schemeCurrPipe) {

        String judgeType = schemeCurrPipe.getFinishJudgeType();
        String judgeValue = schemeCurrPipe.getFinishJudgeValue();

        if (OpsApSchemePipelineDTO.JUDGE_TYPE_EXEC_STATUS.equals(judgeType)) {
            // 失败或超时
            if (SchemePipeExecStatusEnum.STATUS_FAIL.getCode().equals(judgeValue)
                    && (OpsStatusEnum.STATUS_101.getStatusCode().equals(currpipeInst.getStatus())
                    || OpsStatusEnum.STATUS_102.getStatusCode().equals(currpipeInst.getStatus()))) {
                return true;
            }
            if (SchemePipeExecStatusEnum.STATUS_SUC.getCode().equals(judgeValue)
                    && OpsStatusEnum.STATUS_9.getStatusCode().equals(currpipeInst.getStatus())) {
                return true;
            }
        } else if (OpsApSchemePipelineDTO.JUDGE_TYPE_ASPNODE_RESULT.equals(judgeType) && stepInstHostResult != null) {
            if (String.valueOf(AspNodeResultFlagEnum.STATUS_SUC.getCode()).equals(judgeValue)
                    && AspNodeResultFlagEnum.STATUS_SUC.getCode().equals(stepInstHostResult.getAspNodeResult())) {
                return true;
            } else if (String.valueOf(AspNodeResultFlagEnum.STATUS_FAIL.getCode()).equals(judgeValue)
                    && AspNodeResultFlagEnum.STATUS_FAIL.getCode().equals(stepInstHostResult.getAspNodeResult())) {
                return true;
            }
        } else if (OpsApSchemePipelineDTO.JUDGE_TYPE_ASPNODE_MSG.equals(judgeType) && stepInstHostResult != null) {
            return judgeValue.equals(stepInstHostResult.getAspNodeMessage());
        }
        return false;
    }

    private void handleAutoRepairProcessOver(OpsAgentStepInstanceResult stepInstHostResult,
                                             OpsAutoRepairExecuteLog autoRepairResult, OpsPipelineInstanceDTO pipeInstance) {
        autoRepairResult.setEndTime(new Date());
        autoRepairResult.setStatus(pipeInstance.getStatus());
        if (stepInstHostResult != null) {
            autoRepairResult.setExitCode(stepInstHostResult.getExitCode());
            autoRepairResult.setResponse(stepInstHostResult.getOpsLog());
            autoRepairResult.setAspNodeResult(stepInstHostResult.getAspNodeResult());
            autoRepairResult.setAspNodeMessage(stepInstHostResult.getAspNodeMessage());
        }
        saveOpsAutoRepairExecuteResult(autoRepairResult);
    }

    /**
     * 功能描述: 监听作业超时事件
     * <p>
     * @param event
     */
    @Async
    @EventListener
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleAutoRepairExecuteTimeout(OpsPipelineExecuteTimeoutEvent event) {
        Long pipeInstId = event.getPipelineInstance().getPipelineInstanceId();
        OpsAutoRepairExecuteLog apExecuteLog = dao.queryAutoRepairExecuteResultByPipelineInstanceId(pipeInstId);
        if (apExecuteLog == null) {
            log.info("The pipeline instance of {} is not actived by the auto repair scheme.", pipeInstId);
            return;
        }

        if (apExecuteLog.isExecuteResultOver()) {
            log.warn("The auto repair log with id {}, schemeId {} is over, status is {}, the timeout process will ignore.",
                    apExecuteLog.getId(), apExecuteLog.getSchemeId(), apExecuteLog.getStatus());
            return;
        }

        OpsStepInstance stepInstance = OpsStepInstance.class.cast(event.getSource());
        AgentHostInfo agentHost = agentHostBiz.queryAgentDataByPoolAndAgentIP(apExecuteLog.getAssetPool(), apExecuteLog.getAgentIp());
        OpsAgentStepInstanceResult stepInstHostResult = baseDataBiz.queryAgentStepInstanceResultByKeys(
                stepInstance.getStepInstanceId(), agentHost.getConcatHost());

        handleApSchemePipelineFinish(event.getPipelineInstance(), stepInstHostResult);
    }

    /**
     * 功能描述: 人工执行自愈下一个作业
     * <p>
     * @param execLogId
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public GeneralResponse mannualActiveApSchemeNextPipe(Long execLogId) {
        OpsAutoRepairExecuteLog apExecuteLog = dao.queryAutoRepairExecuteResultById(execLogId);
        if (apExecuteLog == null) {
            return new GeneralResponse(false, "There is no ap scheme execution process with id:" + execLogId);
        }
        if (!OpsAutoRepairStatusEnum.STATUS_6.getStatusCode().equals(apExecuteLog.getStatus())) {
            return new GeneralResponse(false, "The auto repair execution process is not of the status of 'pause for manual continue'.");
        }
        OpsApSchemePipelineDTO schemeNextPipe = queryReferPipelineBySchemeIdAndOrder(
                apExecuteLog.getSchemeId(), apExecuteLog.getCurrentPipelineOrder() + 1);
        if (schemeNextPipe == null) {
            return new GeneralResponse(false, "There is no next pipeline to active for the scheme execution log with id:" + execLogId);
        }
        AgentHostInfo agentHost = agentHostBiz.queryAgentDataByPoolAndAgentIP(apExecuteLog.getAssetPool(), apExecuteLog.getAgentIp());
        activeAutoRepairSchemePipeline(apExecuteLog, schemeNextPipe, agentHost);
        return new GeneralResponse();
    }

    /**
     * 功能描述: 人工停止自愈流程
     * <p>
     * @param execLogId
     * @return
     */
    public GeneralResponse mannualTerminateApSchemeExecution(Long execLogId) {
        OpsAutoRepairExecuteLog apExecuteLog = dao.queryAutoRepairExecuteResultById(execLogId);
        if (apExecuteLog == null) {
            return new GeneralResponse(false, "There is no ap scheme execution process with id:" + execLogId);
        }
        if (!OpsAutoRepairStatusEnum.STATUS_6.getStatusCode().equals(apExecuteLog.getStatus())) {
            return new GeneralResponse(false, "The auto repair execution process is not of the status of 'pause for manual continue'.");
        }
        apExecuteLog.setEndTime(new Date());
        apExecuteLog.setStatus(OpsAutoRepairStatusEnum.STATUS_7.getStatusCode());  // 人工终止
        saveOpsAutoRepairExecuteResult(apExecuteLog);
        return new GeneralResponse();
    }
}
