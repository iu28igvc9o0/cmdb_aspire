/**
 * 项目名： ops-service
 * <p/>
 * <p>
 * 文件名:  OpsManageBiz.java
 * <p/>
 * <p>
 * 功能描述: ops管理业务实现
 * <p/>
 *
 * @author pengguihua
 * @date 2019年10月22日
 * @version V1.0 <p/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 */
package com.aspire.mirror.ops.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.aspire.mirror.ops.api.domain.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.common.util.ZipUtil;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel;
import com.aspire.mirror.ops.api.domain.OpsLabel.OpsLabelQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO.OpsPipelineQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob.OpsPipelineRunJobQueryModel;
import com.aspire.mirror.ops.biz.model.OpsGroupObjectHandler;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.AgentDataDao;
import com.aspire.mirror.ops.dao.OpsDataDao;
import com.aspire.mirror.ops.dao.SensitiveRuleWhiteDao;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.OpsPipeline;
import com.aspire.mirror.ops.domain.OpsPipelineAuditInfo;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;
import com.aspire.mirror.ops.domain.OpsScriptAuditInfo;
import com.aspire.mirror.ops.domain.OpsStep;
import com.aspire.mirror.ops.domain.OpsStepInstance;
import com.aspire.mirror.ops.util.JsonUtil;
import com.aspire.mirror.ops.util.SshUtil;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.support.CronExpressionUtils;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目名称: ops-service
 * <p/>
 * <p>
 * 类名: OpsBaseDataBiz
 * <p/>
 * <p>
 * 类功能描述: ops基础数据管理
 * <p/>
 *
 * @author pengguihua
 * @version V1.0
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有
 * @date 2019年10月22日
 */
@Service
@Transactional
@Slf4j
@SuppressWarnings("rawtypes")
public class OpsBaseDataBiz {
    private static final String OPS_PIPELINE_JOB_IDETIFY = "ops_pipeline_cron_job";
    private static final String OPS_TRACKER_NODE_GROUP = "mirror_taskTracker";
    @Autowired
    private OpsDataDao opsBaseDataDao;
    @Autowired
    private AgentDataDao agentDataDao;

    @Autowired
    private JobClient jobClient;

    @Autowired
    private SensitiveRuleWhiteDao sensitiveRuleWhiteDao;

    @Autowired
    private OpsGroupObjectHandler opsGroupObjectHandler;

    @Value("${file_regenerate_temp:D://temp/}")
    private String tempPath;

    private static final String INSTANCE_LOG_DIR = "/ops_instance_log/";

    @Autowired
    private CommonSftpServerConfig sftpConfig;

    @Autowired
    private OpsFileBiz opsFileBiz;


    public GeneralResponse saveOpsAccount(OpsAccount account) {
        OpsAccount existAccount = opsBaseDataDao.queryAccountByName(account.getAccountName());
        if (existAccount != null) {
            return new GeneralResponse(false, "Already exist the account with name: " + account.getAccountName());
        }
        account.setCreater(RequestAuthContext.getRequestHeadUserName());
        account.setCreateTime(new Date());
        opsBaseDataDao.insertOpsAccount(account);
        return new GeneralResponse(true, null, account);
    }

    public GeneralResponse removeOpsAccount(String accountName) {
        opsBaseDataDao.removeOpsAccount(accountName);
        return new GeneralResponse();
    }

    public List<OpsAccount> queryOpsAccountList(OpsAccountQueryModel queryParam) {
        return opsBaseDataDao.queryOpsAccountList(queryParam);
    }

    @Transactional
    public GeneralResponse saveOpsLabel(OpsLabel label) {
        OpsLabel existLabel = opsBaseDataDao.queryOpsLabelByCode(label.getCode());
        if (existLabel != null) {
            opsBaseDataDao.updateOpsLabel(label);
            return new GeneralResponse(true, null, label);
        }
        opsBaseDataDao.insertOpsLabel(label);
        return new GeneralResponse(true, null, label);
    }

    @Transactional
    public GeneralResponse updateOpsLabel(OpsLabel label) {
        opsBaseDataDao.updateOpsLabel(label);
        return new GeneralResponse(true, null, label);
    }

    @Transactional
    public GeneralResponse removeOpsLabel(String labelCode) {
        Integer referCount = opsBaseDataDao.queryOpsLabelReferCount(labelCode);
        if (referCount > 0) {
            return new GeneralResponse(false, "Can't remove the label with labelCode: " + labelCode
                    + " as it was refrenced by others.");
        }
        opsBaseDataDao.removeOpsLabel(labelCode);
        return new GeneralResponse();
    }

    @Transactional(readOnly = true)
    public List<OpsLabel> queryOpsLabelList(OpsLabelQueryModel queryParam) {
        return opsBaseDataDao.queryOpsLabelList(queryParam);
    }

    @Transactional
    public GeneralResponse saveOpsScript(final OpsScript script) {
        script.decodeScriptContent();    // base64解码
        if (StringUtils.isEmpty(script.getCurrentVersion())) {
            script.setCurrentVersion(script.newVersion());
        }
        String currUser = RequestAuthContext.getRequestHeadUserName();
        if (script.getScriptId() == null) {
            Integer count = opsBaseDataDao.queryOpsScriptCountByName(script.getScriptName());
            if (count > 0) {
                return new GeneralResponse(false, "The script name is already exists.");
            }
            script.setCreater(currUser);
            script.setCreateTime(new Date());
            script.setUpdater(currUser);
            script.setUpdateTime(new Date());
            opsBaseDataDao.insertOpsScript(script);
            if (!CollectionUtils.isEmpty(script.getOpsParamReferenceList())) {
                for (OpsParamReference opsParamReference : script.getOpsParamReferenceList()) {
                    opsParamReference.setEntityId(script.getScriptId());
                    opsBaseDataDao.insertEntityParamRefer(opsParamReference);
                }
            }
        } else {
            script.setUpdater(currUser);
            script.setUpdateTime(new Date());
            opsBaseDataDao.updateOpsScript(script);
            opsBaseDataDao.cleanEntityParamRefers(script.getScriptId());
            if (!CollectionUtils.isEmpty(script.getOpsParamReferenceList())) {
                for (OpsParamReference opsParamReference : script.getOpsParamReferenceList()) {
                    opsParamReference.setEntityId(script.getScriptId());
                    opsBaseDataDao.insertEntityParamRefer(opsParamReference);
                }
            }
        }

        script.encodeScriptContent();
        opsGroupObjectHandler.saveOpsGroup(script, script.getScriptId(), OpsGroupObjectTypeEnum.SCRIPT.getStatusCode());

        if (script.getNeedAddHis()) {
            OpsScript opsScript = opsBaseDataDao.queryOpsScriptById(script.getScriptId());
            OpsScriptHis opsScriptHis = new OpsScriptHis();
            BeanUtils.copyProperties(opsScript, opsScriptHis);
            opsBaseDataDao.insertOpsScriptHis(opsScriptHis);
        }
        return new GeneralResponse(true, null, script);
    }

    @Transactional
    public GeneralResponse removeOpsScript(Long scriptId) {
        OpsScript script = opsBaseDataDao.queryOpsScriptById(scriptId);
        if (script != null && script.getStepId() != null) {
            return new GeneralResponse(false, "Can't delete the script as it is referenced by the pipeline.");
        }
        // 删除分组
        opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(scriptId, OpsGroupObjectTypeEnum.SCRIPT
                .getStatusCode());
        opsBaseDataDao.removeOpsScriptById(scriptId);
        return new GeneralResponse();
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsScript> queryOpsScriptList(OpsScriptQueryModel queryParam) {
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        List<OpsScript> opsScriptList = opsBaseDataDao.queryOpsScriptList(queryParam, resFilterConfig);
        if (CollectionUtils.isEmpty(opsScriptList)) {
            return new PageListQueryResult<OpsScript>(0, opsScriptList);
        }
        Integer totalCount = opsBaseDataDao.queryOpsScriptListTotalSize(queryParam, resFilterConfig);
        opsScriptList.stream().forEach(script -> {
            script.encodeScriptContent();
        });
        return new PageListQueryResult<OpsScript>(totalCount, opsScriptList);
    }

    @Transactional(readOnly = true)
    public OpsScript queryOpsScriptById(Long scriptId) {
        OpsScript result = opsBaseDataDao.queryOpsScriptById(scriptId);
        if (result != null) {
            result.encodeScriptContent();
        }
        return result;
    }

    @Transactional
    public GeneralResponse saveOpsPipeline(OpsPipelineDTO dto) {
        GeneralResponse checkResult = check4OpsPipelineSave(dto);
        if (!checkResult.isFlag()) {
            return checkResult;
        }

        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date nowTime = new Date();
        OpsPipeline pipeline = new OpsPipeline();
        BeanUtils.copyProperties(dto, pipeline);
        pipeline.setCurrentVersion(pipeline.newVersion());
        if (dto.getPipelineId() == null) {
            pipeline.setCreater(currUser);
            pipeline.setCreateTime(nowTime);
            pipeline.setUpdater(currUser);
            pipeline.setUpdateTime(nowTime);
            opsBaseDataDao.insertOpsPipeline(pipeline);
        } else {
            pipeline.setUpdater(currUser);
            pipeline.setUpdateTime(nowTime);
            opsBaseDataDao.updateOpsPipeline(pipeline);
        }

        if (CollectionUtils.isNotEmpty(dto.getRemoveOpsStepIdList())) {
            dto.getRemoveOpsStepIdList().stream().forEach(stepId -> {
                opsBaseDataDao.removeOpsScriptByStepId(stepId);
                opsBaseDataDao.removeOpsStepById(stepId);
            });
        }
        // 判断作业的脚本  执行用户、执行机器、脚本内容是否有过变更
        boolean updateFlag = false;
        for (OpsStepDTO stepDTO : dto.getOpsStepList()) {
            if (stepDTO.getOpsType() == OpsStep.OPS_TYPE_SCRIPT) {
                //新增了脚本步骤时
                if (stepDTO.getStepId() == null) {
                    updateFlag = true;
                    break;
                } else {
                    OpsStep step = opsBaseDataDao.queryOpsStepById(stepDTO.getStepId());
                    if (!step.getTargetHosts().containsAll(stepDTO.getTargetHosts())) {
                        updateFlag = true;
                        break;
                    }
                    if (!step.getTargetOpsUser().equals(stepDTO.getTargetOpsUser())) {
                        updateFlag = true;
                        break;
                    }
                    OpsScript opsScript = stepDTO.getEmbedScript();
                    OpsScript script = opsBaseDataDao.queryOpsScriptById(stepDTO.getScriptId());
                    script.encodeScriptContent();
                    if (!script.getScriptContent().equals(opsScript.getScriptContent())) {
                        updateFlag = true;
                        break;
                    }
                }
            }
        }
        if (updateFlag) {
            SensitiveRuleWhite sensitiveRuleWhite = new SensitiveRuleWhite();
            sensitiveRuleWhite.setObjectId(dto.getPipelineId());
            // 解绑
            sensitiveRuleWhite.setObjectStatus(2);
            sensitiveRuleWhite.setObjectType("pipeline");
            // 自动
            sensitiveRuleWhite.setUntieSource(1);
            sensitiveRuleWhiteDao.updateStatusByObjectIdAndType(sensitiveRuleWhite);
        }

        // 添加到历史数据
        OpsPipeline opsPipeline = opsBaseDataDao.queryPipelineById(pipeline.getPipelineId());
        OpsPipelineHis opsPipelineHis = new OpsPipelineHis();
        BeanUtils.copyProperties(opsPipeline, opsPipelineHis);
        opsPipelineHis.setCreateTime(nowTime);
        opsPipelineHis.setCreater(currUser);
        opsBaseDataDao.insertOpsPipelineHis(opsPipelineHis);

        for (OpsStepDTO stepDto : dto.getOpsStepList()) {
            OpsStep step = new OpsStep();
            BeanUtils.copyProperties(stepDto, step);
            step.setPipelineId(pipeline.getPipelineId());

            if (step.getOpsType() == OpsStep.OPS_TYPE_SCRIPT) {
                OpsScript embedScript = step.getEmbedScript();
                embedScript.decodeScriptContent();
                // 作业新增不需要新增历史数据，作业这边处理
                embedScript.setNeedAddHis(false);
                embedScript.setCurrentVersion(pipeline.getCurrentVersion());
                this.saveOpsScript(embedScript);
                step.setScriptId(embedScript.getScriptId());
            }

            if (step.getStepId() == null) {
                step.setCreater(currUser);
                step.setCreateTime(nowTime);
                opsBaseDataDao.insertOpsStep(step);
            } else {
                step.setUpdater(currUser);
                step.setUpdateTime(nowTime);
                opsBaseDataDao.updateOpsStep(step);
            }
            // 反向填充step_id到script的step_id字段
            if (step.getOpsType() == OpsStep.OPS_TYPE_SCRIPT
                    && step.getEmbedScript().getStepId() == null) {
                OpsScript embedScript = step.getEmbedScript();
                if (CollectionUtils.isEmpty(embedScript.getGroupIdList())) {
                    embedScript.setGroupIdList(pipeline.getGroupIdList());
                }
                embedScript.setStepId(step.getStepId());
                this.saveOpsScript(embedScript);
            }


            OpsScriptHis opsScriptHis = new OpsScriptHis();
            if (step.getOpsType() == OpsStep.OPS_TYPE_SCRIPT) {
                OpsScript opsScript = opsBaseDataDao.queryOpsScriptById(step.getEmbedScript().getScriptId());
                BeanUtils.copyProperties(opsScript, opsScriptHis);
                opsBaseDataDao.insertOpsScriptHis(opsScriptHis);
            }
            OpsStepHis opsStepHis = new OpsStepHis();
            BeanUtils.copyProperties(step, opsStepHis);
            opsStepHis.setPipelineHisId(opsPipelineHis.getId());
            opsStepHis.setScriptHisId(opsScriptHis.getId());
            opsBaseDataDao.insertOpsStepHis(opsStepHis);

            //回填stepid 到script
            if (step.getOpsType() == OpsStep.OPS_TYPE_SCRIPT && opsScriptHis.getId() != null) {
                opsScriptHis.setStepHisId(opsStepHis.getId());
                opsBaseDataDao.updateOpsScriptHis(opsScriptHis);
            }
        }
        // 添加分组列表
        opsGroupObjectHandler.saveOpsGroup(pipeline, pipeline.getPipelineId(), OpsGroupObjectTypeEnum.PILELINE
                .getStatusCode());
        return new GeneralResponse(true, null, pipeline.getPipelineId());
    }

    /**
     * 功能描述: 验证数据
     * <p>
     *
     * @param dto
     * @return
     */
    public GeneralResponse check4OpsPipelineSave(OpsPipelineDTO dto) {
        if (dto.getPipelineId() == null) {
            OpsPipelineQueryModel queryParam = new OpsPipelineQueryModel();
            queryParam.setPipelineName(dto.getPipelineName().trim());
            Integer count = opsBaseDataDao.queryOpsPipelineTotalCount(queryParam, null);
            if (count > 0) {
                return new GeneralResponse(false, "已存在该作业名: " + dto.getPipelineName());
            }
        }

        List<String> allStepNameList = new ArrayList<>();
        List<String> allScriptNameList = new ArrayList<>();
        for (OpsStepDTO stepDto : dto.getOpsStepList()) {
            String stepName = stepDto.getStepName().trim();
            if (allStepNameList.contains(stepName)) {
                return new GeneralResponse(false, "已存在该步骤名: " + stepDto.getStepName());
            }
            allStepNameList.add(stepName);

            if (stepDto.getStepId() == null) {
                Integer stepCount = opsBaseDataDao.queryOpsStepCountByName(stepName);
                if (stepCount > 0) {
                    return new GeneralResponse(false, "已存在该步骤名: " + stepDto.getStepName());
                }
            }

            OpsScript embedScript = stepDto.getEmbedScript();
            if (embedScript != null) {
                String scriptName = embedScript.getScriptName() == null ? "" : embedScript.getScriptName().trim();
                if (allScriptNameList.contains(scriptName)) {
                    return new GeneralResponse(false, "已存在该脚本名: " + embedScript.getScriptName());
                }

                if (embedScript.getScriptId() == null) {
                    allScriptNameList.add(scriptName);
                    Integer scriptCount = opsBaseDataDao.queryOpsScriptCountByName(scriptName);
                    if (scriptCount > 0) {
                        return new GeneralResponse(false, "已存在该脚本名: " + embedScript.getScriptName());
                    }
                }
            }
        }
        return new GeneralResponse();
    }

    @Transactional
    public GeneralResponse removeOpsPipeline(Long pipelineId) {
        //关联场景的作业不允许删除
        List<OpsPipelineScenes> scenesList = opsBaseDataDao.queryPipelineScenesByPipelineId(pipelineId);
        if (CollectionUtils.isNotEmpty(scenesList)) {
            return new GeneralResponse(false, "关联场景的作业不允许删除");
        }
        sensitiveRuleWhiteDao.deleteRuleWhiteByObjectIdAndType(pipelineId, OpsGroupObjectTypeEnum.PILELINE
                .getStatusCode());
        //删除脚本分组信息
        List<OpsScript> scriptList = opsBaseDataDao.queryOpsScriptListByPipelineId(pipelineId);
        if (CollectionUtils.isNotEmpty(scriptList)) {
            for (OpsScript opsScript : scriptList) {
                opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(opsScript.getScriptId(),
                        OpsGroupObjectTypeEnum
                                .SCRIPT.getStatusCode());
            }
        }
        opsBaseDataDao.removeOpsScriptByPipelineId(pipelineId);
        opsBaseDataDao.removeAllOpsStepsByPipelineId(pipelineId);
        //删除分组信息
        opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(pipelineId, OpsGroupObjectTypeEnum.PILELINE
                .getStatusCode());
        opsBaseDataDao.removePipelineById(pipelineId);
        return new GeneralResponse();
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsPipelineDTO> queryOpsPipelineList(OpsPipelineQueryModel queryParam) {
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        List<OpsPipeline> pipelineList = opsBaseDataDao.queryOpsPipelineList(queryParam, resFilterConfig);
        if (CollectionUtils.isEmpty(pipelineList)) {
            return new PageListQueryResult<OpsPipelineDTO>(0, null);
        }
        Integer totalCount = opsBaseDataDao.queryOpsPipelineTotalCount(queryParam, resFilterConfig);
        List<OpsPipelineDTO> dataList = pipelineList.stream().map(pipeline -> {
            return OpsPipelineDTO.class.cast(pipeline);
        }).collect(Collectors.toList());
        return new PageListQueryResult<OpsPipelineDTO>(totalCount, dataList);
    }

    @Transactional(readOnly = true)
    public OpsPipeline queryOpsPipelineById(Long pipelineId) {
        OpsPipelineQueryModel queryParam = new OpsPipelineQueryModel();
        queryParam.setPipelineId(pipelineId);
        List<OpsPipeline> pipelineList = opsBaseDataDao.queryOpsPipelineList(queryParam, null);
        return CollectionUtils.isEmpty(pipelineList) ? null : pipelineList.get(0);
    }

    @Transactional(readOnly = true)
    public List<OpsStepDTO> queryOpsStepListByPipelineId(Long pipelineId) {
        List<OpsStep> stepList = opsBaseDataDao.queryOpsStepListByPipelineId(pipelineId);
        return stepList.stream().map(step -> {
            OpsStepDTO opsStepDTO = OpsStepDTO.class.cast(step);
            List<SimpleAgentHostInfo> simpleAgentHostInfoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(opsStepDTO.getTargetHosts())) {
                List<AgentHostInfo> agentHostInfos = agentDataDao.queryAgentDataListByProxyIdConcatIP(opsStepDTO.getTargetHosts());
                simpleAgentHostInfoList = agentHostInfos.stream().map(item -> {
                    SimpleAgentHostInfo simpleAgentHostInfo = JsonUtil.jacksonConvert(item, SimpleAgentHostInfo.class);
                    simpleAgentHostInfo.setProxyId(item.getProxyId());
                    simpleAgentHostInfo.setAgentIp(item.getAgentIp());
                    return simpleAgentHostInfo;
                }).collect(Collectors.toList());
            }
            opsStepDTO.setTargetHostList(simpleAgentHostInfoList);
            return opsStepDTO;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsPipelineInstanceDTO> queryOpsPipelineInstanceList(OpsPipelineInstanceQueryParam
                                                                                            queryParam) {
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        List<OpsPipelineInstance> list = opsBaseDataDao.queryOpsPipelineInstanceList(queryParam, resFilterConfig);
        if (CollectionUtils.isEmpty(list)) {
            return new PageListQueryResult<OpsPipelineInstanceDTO>(0, null);
        }
        Integer totalCount = opsBaseDataDao.queryOpsPipelineInstanceTotalSize(queryParam, resFilterConfig);
        List<OpsPipelineInstanceDTO> dataList = list.stream().map(instance -> {
            return OpsPipelineInstanceDTO.class.cast(instance);
        }).collect(Collectors.toList());
        return new PageListQueryResult<OpsPipelineInstanceDTO>(totalCount, dataList);
    }

    @Transactional(readOnly = true)
    public OpsPipelineInstanceDTO queryOpsPipelineInstanceById(Long pipelineInstanceId) {
        return opsBaseDataDao.queryPipelineInstanceById(pipelineInstanceId);
    }

    @Transactional(readOnly = true)
    public List<OpsStepInstanceDTO> queryStepInstListByPipelineInstId(Long pipelineInstanceId) {
        List<OpsStepInstance> list = opsBaseDataDao.queryStepInstListByPipelineInstId(pipelineInstanceId);
        return list.stream().map(instance -> {
            OpsStepInstanceDTO dto = OpsStepInstanceDTO.class.cast(instance);
            dto.encodeScriptContent();
            List<SimpleAgentHostInfo> simpleAgentHostInfoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(dto.getTargetHosts())) {
                List<AgentHostInfo> agentHostInfos = agentDataDao.queryAgentDataListByProxyIdConcatIP(dto.getTargetHosts());
                simpleAgentHostInfoList = agentHostInfos.stream().map(item -> {
                    SimpleAgentHostInfo simpleAgentHostInfo = JsonUtil.jacksonConvert(item, SimpleAgentHostInfo.class);
                    simpleAgentHostInfo.setAgentIp(item.getAgentIp());
                    simpleAgentHostInfo.setProxyId(item.getProxyId());
                    return simpleAgentHostInfo;
                }).collect(Collectors.toList());
            }
            dto.setTargetHostList(simpleAgentHostInfoList);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OpsStepInstanceDTO queryStepInstanceById(Long stepInstId) {
        OpsStepInstanceDTO result = opsBaseDataDao.queryStepInstanceById(stepInstId);
        result.encodeScriptContent();
        return result;
    }

    @Transactional(readOnly = true)
    public PageListQueryResult<OpsAgentStepInstanceResult> queryOpsStepAgentRunResultList(
            OpsAgentStepInstanceResultQueryModel param) {

        List<OpsAgentStepInstanceResult> resultList = opsBaseDataDao.queryOpsStepAgentRunResultList(param);
        if (CollectionUtils.isEmpty(resultList)) {
            return new PageListQueryResult<OpsAgentStepInstanceResult>(0, resultList);
        }
        Integer totalCount = opsBaseDataDao.queryOpsStepAgentRunResultTotalSize(param);
        return new PageListQueryResult<OpsAgentStepInstanceResult>(totalCount, resultList);
    }

    /**
     * 功能描述: 根据stepInstId 和 proxyid:AgentIp 查询执行日志详情
     * <p>
     *
     * @param stepInstId
     * @param targetHost
     * @return
     */
    @Transactional(readOnly = true)
    public OpsAgentStepInstanceResult queryAgentStepInstanceResultByKeys(Long stepInstId, String targetHost) {
        return opsBaseDataDao.queryAgentStepInstanceResultByKeys(stepInstId, targetHost);
    }

    @Transactional
    public GeneralResponse saveOpsPipelineRunJob(OpsPipelineRunJob runJob) {
        if (!CronExpressionUtils.isValidExpression(runJob.getCronExpression())) {
            return new GeneralResponse(false, "The cron expression is invalid.");
        }
        if (runJob.getJobId() == null) {
            OpsPipelineRunJobQueryModel queryParam = new OpsPipelineRunJobQueryModel();
            queryParam.setName(runJob.getName());
            Integer existCount = opsBaseDataDao.queryOpsPipelineRunJobTotalSize(queryParam);
            if (existCount.intValue() > 0) {
                return new GeneralResponse(false, "已存在该任务名: " + runJob.getName());
            }
            runJob.setCreater(RequestAuthContext.getRequestHeadUserName());
            runJob.setCreateTime(new Date());
            opsBaseDataDao.insertOpsPipelineRunJob(runJob);
        } else {

            runJob.setUpdater(RequestAuthContext.getRequestHeadUserName());
            runJob.setUpdateTime(new Date());
            opsBaseDataDao.updateOpsPipelineRunJob(runJob);
        }
        return new GeneralResponse(true, null, runJob);
    }

    @Transactional
    public GeneralResponse removeOpsPipelineRunJob(Long jobId) {
        OpsPipelineRunJob runJob = opsBaseDataDao.queryOpsPipelineRunJobById(jobId);
        if (runJob == null) {
            return new GeneralResponse();
        }
        if (OpsStatusEnum.STATUS_100.getStatusCode().equals(runJob.getStatus())) {
            unSchedulePipelineCronJob(jobId);
        }
        opsBaseDataDao.removeOpsPipelineRunJob(jobId);
        return new GeneralResponse();
    }

    @Transactional
    public PageListQueryResult<OpsPipelineRunJob> queryOpsPipelineRunJobList(OpsPipelineRunJobQueryModel queryParam) {
        List<OpsPipelineRunJob> resultList = opsBaseDataDao.queryOpsPipelineRunJobList(queryParam);
        if (CollectionUtils.isEmpty(resultList)) {
            return new PageListQueryResult<OpsPipelineRunJob>(0, resultList);
        }
        Integer totalCount = opsBaseDataDao.queryOpsPipelineRunJobTotalSize(queryParam);
        return new PageListQueryResult<OpsPipelineRunJob>(totalCount, resultList);
    }

    @Transactional
    public GeneralResponse schedulePipelineCronJob(Long jobId) {
        OpsPipelineRunJob pipelineJob = opsBaseDataDao.queryOpsPipelineRunJobById(jobId);
        if (pipelineJob == null) {
            String tip = "There is no pipeline job with id " + jobId;
            return new GeneralResponse(false, tip);
        }
        if (OpsStatusEnum.STATUS_100.getStatusCode().equals(pipelineJob.getStatus())) {
            String tip = "The pipeline job is already running.";
            return new GeneralResponse(false, tip);
        }

        Job job = new Job();
        job.setTaskId(OPS_PIPELINE_JOB_IDETIFY + "_" + jobId);
        job.setParam("task_identity", OPS_PIPELINE_JOB_IDETIFY);
        job.setParam("jobId", String.valueOf(jobId));
        job.setTaskTrackerNodeGroup(OPS_TRACKER_NODE_GROUP);
        job.setCronExpression(pipelineJob.getCronExpression());

        job.setRelyOnPrevCycle(false);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);
        Response response = jobClient.submitJob(job);

        if (response.isSuccess()) {
            OpsPipelineRunJob udpateData = new OpsPipelineRunJob();
            udpateData.setJobId(jobId);
            udpateData.setStatus(OpsStatusEnum.STATUS_100.getStatusCode());
            opsBaseDataDao.updateOpsPipelineRunJob(udpateData);
        }
        return new GeneralResponse(response.isSuccess());
    }

    @Transactional
    public GeneralResponse unSchedulePipelineCronJob(Long jobId) {
        Response response = jobClient.cancelJob(OPS_PIPELINE_JOB_IDETIFY + "_" + jobId, OPS_TRACKER_NODE_GROUP);
        if (response.isSuccess()) {
            OpsPipelineRunJob udpateData = new OpsPipelineRunJob();
            udpateData.setJobId(jobId);
            udpateData.setStatus(OpsStatusEnum.STATUS_6.getStatusCode());
            opsBaseDataDao.updateOpsPipelineRunJob(udpateData);
        }
        return new GeneralResponse(response.isSuccess());
    }

    public GeneralResponse savePipelineScences(OpsPipelineScenes scenes) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        if (scenes.getPipelineScenesId() == null) {
            Integer count = opsBaseDataDao.queryPipelineScenesCountByName(scenes.getScenesName());
            if (count > 0) {
                return new GeneralResponse(false, "The scenes name is already exists.");
            }
            scenes.setCreater(currUser);
            scenes.setCreateTime(new Date());
            opsBaseDataDao.insertPipelineScences(scenes);
        } else {
            scenes.setUpdater(currUser);
            scenes.setUpdateTime(new Date());
            opsBaseDataDao.updatePipelineScences(scenes);
        }

        // 添加分组列表
        opsGroupObjectHandler.saveOpsGroup(scenes, scenes.getPipelineScenesId(), OpsGroupObjectTypeEnum.SCENES
                .getStatusCode());
        return new GeneralResponse(true, null, scenes);
    }

    public List<GroupScenes> pipelineScenesAllList() {
        List<GroupScenes> groupScenesList = Lists.newArrayList();
        Map<Long, List<GroupRelationDetail>> groupMap = opsGroupObjectHandler.querGroupRelationMapByObjectType
                (OpsGroupObjectTypeEnum.SCENES.getStatusCode());

        for (Long gorupId : groupMap.keySet()) {
            GroupScenes groupScenes = new GroupScenes();
            groupScenes.setGroupId(gorupId);
            groupScenes.setGroupName(groupMap.get(gorupId).get(0).getGroupName());
            List<Long> objectIdList = groupMap.get(gorupId).stream().map(GroupRelationDetail::getObjectId).collect
                    (Collectors.toList());
            List<OpsPipelineScenes> scenesList = opsBaseDataDao.queryPipelineScenesByIds(objectIdList);
            groupScenes.setScenesList(scenesList);
            groupScenesList.add(groupScenes);
        }
        return groupScenesList;
    }

    public GeneralResponse deletePipelineScenes(String scenesIds) {
        for (String scenesId : scenesIds.split(",")) {
            opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(Long.valueOf(scenesId), OpsGroupObjectTypeEnum
                    .SCENES.getStatusCode());
        }
        opsBaseDataDao.deletePipelineScenesByIds(scenesIds);
        return new GeneralResponse(true);
    }

    public OpsPipelineScenes pipelineScenesById(Long pipelineScenesId) {
        return opsBaseDataDao.pipelineScenesById(pipelineScenesId);
    }

    public GeneralResponse logPackageApply(Long pipelineInstanceId) {
        OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(pipelineInstanceId);
        if (opsPipelineInstance != null) {
            List<OpsStepInstance> stepInstanceList = opsBaseDataDao.queryStepInstListByPipelineInstId
                    (pipelineInstanceId);
            if (!stepInstanceList.isEmpty()) {
                OpsPipelineInstanceLog opsPipelineInstanceLog = new OpsPipelineInstanceLog();
                opsPipelineInstanceLog.setPipelineInstanceId(pipelineInstanceId);
                opsPipelineInstanceLog.setStatus(0);
                opsBaseDataDao.insertPipelineInstanceLog(opsPipelineInstanceLog);
                //生成zip日志包并上传ftp
                generateLogFile(opsPipelineInstance, stepInstanceList);
            }
        }
        return new GeneralResponse();
    }

    public void generateLogFile(OpsPipelineInstance opsPipelineInstance, List<OpsStepInstance> stepInstanceList) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletableFuture.runAsync(() -> {
            FileWriter fw = null;
            try {

                String rootPath = tempPath + opsPipelineInstance.getPipelineInstanceName();
                File rootFile = new File(rootPath);
                if (!rootFile.exists()) {
                    rootFile.mkdirs();
                }
                for (OpsStepInstance opsStepInstance : stepInstanceList) {
                    String stepPath = rootPath + File.separator + opsStepInstance.getStepInstanceName();
                    File stepFile = new File(stepPath);
                    if (!stepFile.exists()) {
                        stepFile.mkdir();
                    }
                    OpsAgentStepInstanceResultQueryModel opsAgentStepInstanceResult = new OpsAgentStepInstanceResultQueryModel();
                    opsAgentStepInstanceResult.setStepInstanceId(opsStepInstance.getStepInstanceId());
                    List<OpsAgentStepInstanceResult> instanceResultList = opsBaseDataDao.queryOpsStepAgentRunResultList
                            (opsAgentStepInstanceResult);
                    for (OpsAgentStepInstanceResult stepInstanceResult : instanceResultList) {
                        String instancePath = stepPath + File.separator + stepInstanceResult.getAgentIp
                                () + ".txt";
                        File instanceResultFile = new File(instancePath);
                        if (!instanceResultFile.exists()) {
                            instanceResultFile.createNewFile();
                        }
                        fw = new FileWriter(instancePath);
                        fw.write(stepInstanceResult.getOpsLog() == null ? "" : stepInstanceResult.getOpsLog());
                        fw.flush();
                    }
                }
                //生成压缩文件
                String generateFileName = rootFile.getParent() + File.separator + "日志汇总_" + opsPipelineInstance.getPipelineInstanceName() + ".zip";
                ZipUtil.toZip(rootPath, generateFileName, true);
//                FileOutputStream outputStream = new FileOutputStream(generateFileName);
//                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
//                generateFile(zipOutputStream, rootFile, "");
                // 关闭 输出流
//                zipOutputStream.close();

                // TODO 上传sftp

//            Path tempDir = Files.createTempDirectory(REPORT_LOCAL_DIR_PREFIX);
//            Path tempFile = tempDir.resolve(fileName);
//            file.transferTo(tempFile.toFile());

                Pair<SshUtil.SshResultWrap, String> uploadResult = uploadReportFile(new File(generateFileName).toPath(), opsPipelineInstance.getPipelineInstanceId());
                if (uploadResult.getLeft().isSuccess()) {
                    // 修改实例日志状态
                    OpsPipelineInstanceLog opsPipelineInstanceLog = new OpsPipelineInstanceLog();
                    opsPipelineInstanceLog.setPipelineInstanceId(opsPipelineInstance.getPipelineInstanceId());
                    opsPipelineInstanceLog.setStatus(1);
                    opsPipelineInstanceLog.setLogPath(uploadResult.getRight());
                    opsBaseDataDao.updatePipelineInstanceLog(opsPipelineInstanceLog);

                    // 写入文件管理
                    OpsFile opsFile = new OpsFile();
                    opsFile.setFileType(OpsFile.FILE_TYPE_1);
                    opsFile.setFileGenerateType(OpsFile.GENERATE_TYPE_2);
                    opsFile.setFileClass(OpsFileClassEnum.FILE_CLASS_5.getStatusCode().toString());
                    opsFile.setFileVersion(OpsFile.AUTO_LOG_VERSION + opsPipelineInstance.getPipelineInstanceId());
                    opsFile.setFileName(opsPipelineInstance.getPipelineInstanceName() + ".zip");
                    opsFile.setFilePath(uploadResult.getRight());
                    opsFile.setRelationId(opsPipelineInstance.getPipelineInstanceId().toString());
                    opsFile.setRelationName(opsPipelineInstance.getPipelineInstanceName());
                    opsFileBiz.saveFile(opsFile);
                } else {
                    log.error("upload pipeline instance log file to SFTP is failed!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fw != null) {
                        fw.flush();
                        fw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, executor);
//        new Thread(() -> {
//        });
    }

    private Pair<SshUtil.SshResultWrap, String> uploadReportFile(Path localFile, Long pipelineInstanceId) {
        SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
        sftpServer.setIpAddress(sftpConfig.getIpAddress());
        sftpServer.setPort(sftpConfig.getPort());
        sftpServer.setLoginUser(sftpConfig.getLoginUser());
        sftpServer.setLoginPass(sftpConfig.getLoginPass());

        String subDir = INSTANCE_LOG_DIR + pipelineInstanceId;
        String remotePath = sftpConfig.getRootDirectory() + subDir;
        SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);

        Pair<SshUtil.SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);
        String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
        return Pair.of(uploadResult.getLeft(), relativePath);
    }

//    private void generateFile(ZipOutputStream out, File file, String dir) throws IOException {
//        // 当前的是文件夹，则进行一步处理
//        if (file.isDirectory()) {
//            //得到文件列表信息
//            File[] files = file.listFiles();
//
//            //将文件夹添加到下一级打包目录
//            out.putNextEntry(new ZipEntry(dir + "/"));
//
//            dir = dir.length() == 0 ? "" : dir + "/";
//            //循环将文件夹中的文件打包
//            for (int i = 0; i < files.length; i++) {
//                generateFile(out, files[i], dir + files[i].getName());
//            }
//        } else { // 当前是文件
//            // 输入流
//            FileInputStream inputStream = new FileInputStream(file);
//            // 标记要打包的条目
//            out.putNextEntry(new ZipEntry(dir));
//            // 进行写操作
//            int len = 0;
//            byte[] bytes = new byte[1024];
//            while ((len = inputStream.read(bytes)) > 0) {
//                out.write(bytes, 0, len);
//            }
//            // 关闭输入流
//            inputStream.close();
//        }
//    }

    public OpsPipelineInstanceLog getPipelineInstanceLog(Long pipelineInstanceId) {
        return opsBaseDataDao.getPipelineInstanceLog(pipelineInstanceId);
    }

    public GeneralResponse outputPackage(Long pipelineInstanceId) {
        OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(pipelineInstanceId);
        String rootPath = tempPath + "output" + File.separator + "产出汇总_" + opsPipelineInstance.getPipelineInstanceName() + "_" + pipelineInstanceId;
        File rootFile = new File(rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        List<OpsStepInstance> stepInstList = opsBaseDataDao.queryStepInstListByPipelineInstId(pipelineInstanceId);
        Map<String, Set<String>> parsePathMap = Maps.newHashMap();
        for (OpsStepInstance stepInstance : stepInstList) {
            //文件存储
            OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel param = new OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel();
            if (stepInstance.getOpsType() == 2) {
                param.setStepInstanceId(stepInstance.getStepInstanceId());
                List<OpsAgentStepInstanceResult> instanceResultList = opsBaseDataDao.queryOpsStepAgentRunResultList(param);
                for (OpsAgentStepInstanceResult stepInstanceResult : instanceResultList) {
                    if (StringUtils.isNotEmpty(stepInstanceResult.getAspNodeMessage())) {
                        String[] pathList = stepInstanceResult.getAspNodeMessage().split(",");
                        for (String path : pathList) {
                            String fileName = path.substring(path.lastIndexOf("/") + 1);
                            if (parsePathMap.get(fileName) == null) {
                                Set<String> pathSet = Sets.newHashSet();
                                pathSet.add(path);
                                parsePathMap.put(fileName, pathSet);
                            } else {
                                parsePathMap.get(fileName).add(path);
                            }
                        }
                    }
                }
            }
        }
        try {
            if (!parsePathMap.isEmpty()) {
                for (String fileName : parsePathMap.keySet()) {
                    File instanceResultFile = new File(rootPath + File.separator + fileName);
                    if (!instanceResultFile.exists()) {
                        instanceResultFile.createNewFile();
                    }
                    String tableHeader = null;
                    for (String filePath : parsePathMap.get(fileName)) {
//                    String fullDownFilePath = filePath;
                        SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
                        sftpServer.setIpAddress(sftpConfig.getIpAddress());
                        sftpServer.setPort(sftpConfig.getPort());
                        sftpServer.setLoginUser(sftpConfig.getLoginUser());
                        sftpServer.setLoginPass(sftpConfig.getLoginPass());

                        Path localDownDir = Files.createTempDirectory("log_file_down");
                        Pair<SshUtil.SshResultWrap, Path> downResult
                                = SshUtil.sftpDownload(sftpServer, filePath, localDownDir.toFile().getCanonicalPath());
                        SshUtil.SshResultWrap wrap = downResult.getLeft();
                        if (!wrap.isSuccess()) {
                            throw new RuntimeException(wrap.getBizLog());
                        }
                        if (fileName.endsWith(".csv")) {
                            Writer writer = new OutputStreamWriter(new FileOutputStream(instanceResultFile, true), "GB2312");
//                        OutputStream out = new FileOutputStream(instanceResultFile,true);
                            List<String> lineAll;
                            try {
                                lineAll = Files.readAllLines(downResult.getRight());
                            } catch (MalformedInputException malformedInputException) {
                                lineAll = Files.readAllLines(downResult.getRight(), Charset.forName("GB2312"));
                            }
                            if (tableHeader == null) {
                                tableHeader = lineAll.get(0);
                                writer.write(lineAll.get(0));
                                writer.write(System.getProperty("line.separator"));
                            } else {
                                // 如果已经添加表头
                                if (!tableHeader.equals(lineAll.get(0))) {
                                    writer.write(lineAll.get(0));
                                    writer.write(System.getProperty("line.separator"));
                                }
                            }
                            for (int i = 1; i < lineAll.size(); i++) {
                                writer.write(lineAll.get(i));
                                writer.write(System.getProperty("line.separator"));
                            }
                            writer.flush();
                            IOUtils.closeQuietly(writer);
                        } else {
                            OutputStream out = new FileOutputStream(instanceResultFile, true);
                            out.write(Files.readAllBytes(downResult.getRight()));
                            IOUtils.closeQuietly(out);
                        }

//                        out.write(Files.readAllBytes(downResult.getRight()));
//                        IOUtils.closeQuietly(out);
                    }
                }
                //生成压缩文件
                String generateFileName = rootFile.getParent() + File.separator + rootFile.getName() + ".zip";
                ZipUtil.toZip(rootPath, generateFileName, true);
//                FileOutputStream outputStream = new FileOutputStream(generateFileName);
//                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
//                generateFile(zipOutputStream, rootFile, "");

                // 关闭 输出流
//                zipOutputStream.close();
                Pair<SshUtil.SshResultWrap, String> uploadResult = uploadReportFile(new File(generateFileName).toPath(), pipelineInstanceId);
                if (uploadResult.getLeft().isSuccess()) {
                    // 修改实例日志状态
                    OpsPipelineInstance pipelineInstance = new OpsPipelineInstance();
                    pipelineInstance.setPipelineInstanceId(pipelineInstanceId);
                    pipelineInstance.setOutputPath(uploadResult.getRight());
                    opsBaseDataDao.updatePipelineInstance(pipelineInstance);
                    // 写入文件管理
                    OpsFile opsFile = new OpsFile();
                    opsFile.setFileType(OpsFile.FILE_TYPE_1);
                    opsFile.setFileGenerateType(OpsFile.GENERATE_TYPE_2);
                    opsFile.setFileClass(OpsFileClassEnum.FILE_CLASS_4.getStatusCode().toString());
                    opsFile.setFileVersion(OpsFile.AUTO_OUPUT_VERSION + pipelineInstanceId);
                    opsFile.setFileName(rootFile.getName() + ".zip");
                    opsFile.setFilePath(uploadResult.getRight());
                    opsFile.setRelationId(pipelineInstanceId.toString());
                    opsFile.setRelationName(pipelineInstance.getPipelineInstanceName());
                    opsFileBiz.saveFile(opsFile);
                    return new GeneralResponse(true, null, uploadResult.getRight());
                } else {
                    log.error("upload pipeline instance log file to SFTP is failed!");
                }
            }
        } catch (Exception e) {
            log.error("parse output is error!", e);
        }

        return new GeneralResponse();
    }

    public PageListQueryResult<OpsParam> getParamAllList(OpsParamQueryModel paramModel) {

        paramModel = paramModel == null ? new OpsParamQueryModel() : paramModel;
        Integer count = opsBaseDataDao.getParamListTotalSize(paramModel);
        List<OpsParam> list;
        if (count > 0) {
            list = opsBaseDataDao.getParamList(paramModel);
        } else {
            list = Lists.newArrayList();
        }
        return new PageListQueryResult<OpsParam>(count, list);
    }

    @Transactional(readOnly = true)
    public List<OpsParamType> loadAllParamTypeList() {
        return opsBaseDataDao.loadAllParamTypeList();
    }

    public GeneralResponse saveOpsParam(OpsParam saveModel) {
        Date now = new Date();
        if (saveModel.getParamId() == null) {
            OpsParam existModel = opsBaseDataDao.queryOpsParamByCode(saveModel.getParamCode());
            if (existModel != null) {
                return new GeneralResponse(false, "There is already exists opsParam with paramcode: " + saveModel.getParamCode());
            }
            saveModel.setCreateTime(now);
            saveModel.setLastUpdateTime(now);
            opsBaseDataDao.insertOpsParam(saveModel);
        } else {
            saveModel.setLastUpdateTime(now);
            opsBaseDataDao.updateOpsParam(saveModel);
        }
        opsGroupObjectHandler.saveOpsGroup(saveModel, saveModel.getParamId(), OpsGroupObjectTypeEnum.PARAM.getStatusCode());
        GeneralResponse resp = new GeneralResponse();
        resp.setBizData(saveModel.getParamId());
        return resp;
    }

    public GeneralResponse deleteOpsParamById(Long paramId) {
        Integer referCount = opsBaseDataDao.queryOpsParamReferCount(paramId);
        if (referCount > 0) {
            return new GeneralResponse(false, "Can't delete the opsParam with paramId: " + paramId
                    + " as it is still refered by other entities.");
        }
        //删除分组信息
        opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(paramId, OpsGroupObjectTypeEnum.PARAM
                .getStatusCode());
        opsBaseDataDao.deleteOpsParamById(paramId);
        return new GeneralResponse();
    }

    /**
     * 功能描述: 查询实体引用的参数列表
     * <p>
     *
     * @param entityId
     * @return
     */
    @Transactional(readOnly = true)
    public List<OpsParamReference> queryReferParamListByEntityId(Long entityId) {
        return opsBaseDataDao.queryReferParamListByEntityId(entityId);
    }

    /**
     * 功能描述: 保存实体引用 参数关系
     * <p>
     *
     * @param entityId
     * @param paramReferList
     * @return
     */
    public GeneralResponse saveEntityReferParamList(Long entityId, List<OpsParamReference> paramReferList) {
        opsBaseDataDao.cleanEntityParamRefers(entityId);
        for (OpsParamReference refer : paramReferList) {
            opsBaseDataDao.insertEntityParamRefer(refer);
        }
        return new GeneralResponse();
    }

    public GeneralResponse auditPipeline(Long pipelineId, String auditStatus, String auditDesc) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        OpsPipelineAuditInfo opsPipelineAuditInfo = new OpsPipelineAuditInfo();
        opsPipelineAuditInfo.setPipelineId(pipelineId);
        opsPipelineAuditInfo.setAuditStatus(auditStatus);
        opsPipelineAuditInfo.setAuditDesc(auditDesc);
        opsPipelineAuditInfo.setReviewer(currUser);
        opsPipelineAuditInfo.setReviewTime(new Date());
        opsBaseDataDao.auditPipeline(opsPipelineAuditInfo);
//        opsBaseDataDao.auditPipeline(pipelineId, auditStatus, auditDesc, currUser, new Date());
        return new GeneralResponse();
    }

    public GeneralResponse auditScript(Long scriptId, String auditStatus, String auditDesc) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        OpsScriptAuditInfo opsScriptAuditInfo = new OpsScriptAuditInfo();
        opsScriptAuditInfo.setScriptId(scriptId);
        opsScriptAuditInfo.setAuditStatus(auditStatus);
        opsScriptAuditInfo.setAuditDesc(auditDesc);
        opsScriptAuditInfo.setReviewer(currUser);
        opsScriptAuditInfo.setReviewTime(new Date());
//        opsBaseDataDao.auditPipeline(opsPipelineAuditInfo);
        opsBaseDataDao.auditScript(opsScriptAuditInfo);
        return new GeneralResponse();
    }

    public List<OpsPipelineHis> getPipelineHisListByPipelineId(Long pipelineId) {
        List<OpsPipelineHis> pipelineList = opsBaseDataDao.queryPipelineHisListByPipelineId(pipelineId);
        return pipelineList;
    }

    public List<OpsScriptHis> getScriptHisListByScriptId(Long scriptId) {
        List<OpsScriptHis> opsScriptHisList = opsBaseDataDao.queryScriptHisListByScriptId(scriptId);
        opsScriptHisList.stream().forEach(script -> {
            script.encodeScriptContent();
        });
        return opsScriptHisList;
    }

    public List<OpsStepHis> queryOpsStepHisListByPipelineHisId(Long pipelineHisId) {
        List<OpsStepHis> stepList = opsBaseDataDao.queryOpsStepHisListByPipelineHisId(pipelineHisId);
        return stepList.stream().map(step -> {
            List<SimpleAgentHostInfo> simpleAgentHostInfoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(step.getTargetHosts())) {
                List<AgentHostInfo> agentHostInfos = agentDataDao.queryAgentDataListByProxyIdConcatIP(step.getTargetHosts());
                simpleAgentHostInfoList = agentHostInfos.stream().map(item -> {
                    SimpleAgentHostInfo simpleAgentHostInfo = JsonUtil.jacksonConvert(item, SimpleAgentHostInfo.class);
                    simpleAgentHostInfo.setProxyId(item.getProxyId());
                    simpleAgentHostInfo.setAgentIp(item.getAgentIp());
                    return simpleAgentHostInfo;
                }).collect(Collectors.toList());
            }
            step.setTargetHostList(simpleAgentHostInfoList);
            return step;
        }).collect(Collectors.toList());
    }

    public OpsPipelineHis queryOpsPipelineHisById(Long pipelineHisId) {
        OpsPipelineHis opsPipelineHis = opsBaseDataDao.queryOpsPipelineHisById(pipelineHisId);
        return opsPipelineHis;
    }

    public OpsScriptHis queryOpsScriptHisById(Long scriptHisId) {
        OpsScriptHis opsScriptHis = opsBaseDataDao.queryOpsScriptHisById(scriptHisId);
        if (opsScriptHis != null) {
            opsScriptHis.encodeScriptContent();
        }
        return opsScriptHis;
    }

    public GeneralResponse updatePipelineVersion(Long pipelineHisId) {
        if (pipelineHisId == null) {
            return new GeneralResponse(false, "更新作业版本参数为空");
        }
        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date nowTime = new Date();
        try {
            OpsPipelineHis opsPipelineHis = opsBaseDataDao.queryOpsPipelineHisById(pipelineHisId);
            if (opsPipelineHis == null) {
                return new GeneralResponse(false, "更新作业版本该作业历史版本不存在");
            }
            List<OpsStepHis> stepHisList = opsBaseDataDao.queryOpsStepHisListByPipelineHisId(pipelineHisId);
            if (CollectionUtils.isEmpty(stepHisList)) {
                return new GeneralResponse(false, "更新作业版本步骤不存在");
            }
            Set<Long> scriptHisIdSet = stepHisList.stream().filter(item -> item.getScriptHisId() != null).map(OpsStepHis::getScriptHisId).collect(Collectors.toSet());
            List<OpsScriptHis> scriptHisList = Lists.newArrayList();
            for (Long scriptHisId : scriptHisIdSet) {
                OpsScriptHis opsScriptHis = opsBaseDataDao.queryOpsScriptHisById(scriptHisId);
                if (opsScriptHis != null) {
                    scriptHisList.add(opsScriptHis);
                }
            }
            OpsPipeline opsPipeline = new OpsPipeline();
            BeanUtils.copyProperties(opsPipelineHis, opsPipeline);
            opsPipeline.setUpdater(currUser);
            opsPipeline.setUpdateTime(nowTime);
            opsBaseDataDao.updateOpsPipeline(opsPipeline);
            for (OpsStepHis opsStepHis : stepHisList) {
                OpsStep opsStep = new OpsStep();
                BeanUtils.copyProperties(opsStepHis, opsStep);
                opsBaseDataDao.updateOpsStep(opsStep);
            }
            for (OpsScriptHis opsScriptHis : scriptHisList) {
                OpsScript opsScript = new OpsScript();
                opsScript.setUpdater(currUser);
                opsScript.setUpdateTime(nowTime);
                BeanUtils.copyProperties(opsScriptHis, opsScript);
                opsBaseDataDao.updateOpsScript(opsScript);
            }
        } catch (Exception e) {
            log.error("OpsBaseDataBiz[updatePipelineVersion] is error.", e);
            return new GeneralResponse(false, "修改作业版本操作失败！");
        }
        return new GeneralResponse();
    }

    public GeneralResponse updateScriptVersion(Long scriptHisId) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date nowTime = new Date();
        if (scriptHisId == null) {
            return new GeneralResponse(false, "更新脚本版本参数为空");
        }
        OpsScriptHis opsScriptHis = opsBaseDataDao.queryOpsScriptHisById(scriptHisId);
        if (opsScriptHis == null) {
            return new GeneralResponse(false, "更新脚本版本脚本历史数据不存在");
        }
        OpsScript opsScript = new OpsScript();
        opsScript.setUpdater(currUser);
        opsScript.setUpdateTime(nowTime);
        BeanUtils.copyProperties(opsScriptHis, opsScript);
        opsBaseDataDao.updateOpsScript(opsScript);
        return new GeneralResponse();
    }

    public List<String> getUsernameListByAgentIp(String agentIp) {
        return opsBaseDataDao.queryUsernameListByAgentIp(agentIp);
    }

    public List<OpsParamValueDetail> queryParamValueList(OpsParamValue paramValue) {
        return opsBaseDataDao.queryParamValueList(paramValue);
    }

    public List<NormalAgentHostInfo> queryNormalAgentHostByStepId(Long stepId) {
        OpsStep opsStep = opsBaseDataDao.queryOpsStepById(stepId);
        List<NormalAgentHostInfo> hostList = null;
        if (opsStep != null) {
            hostList = agentDataDao.queryyNormalAgentHostByProxyIdConcatIPList(opsStep.getTargetHosts());
        }
        if (hostList == null) {
            hostList = Lists.newArrayList();
        }
        return hostList;
    }
}
