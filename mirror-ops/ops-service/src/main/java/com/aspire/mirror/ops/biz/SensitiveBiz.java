package com.aspire.mirror.ops.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.clientservice.RbacClient;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 敏感指令业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.biz
 * 类名称:    SensitiveConfigBiz.java
 * 类描述:    敏感指令业务层
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 14:12
 * 版本:      v1.0
 */
@Service
@Transactional
@Slf4j
public class SensitiveBiz {
    private static final String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
    @Autowired
    private SensitiveConfigDao sensitiveConfigDao;

    @Autowired
    private SensitiveReviewDao sensitiveReviewDao;

    @Autowired
    private SensitiveRuleDao sensitiveRuleDao;

    @Autowired
    OpsActionBiz opsActionBiz;


    @Autowired
    private RbacClient rbacClient;

    @Autowired
    private SensitiveRuleWhiteDao sensitiveRuleWhiteDao;

    @Autowired
    private SensitiveLevelDao sensitiveLevelDao;

    /**
     * 查询列表
     *
     * @param queryParam 查询参数
     * @return PageListQueryResult<SensitiveConfig> 返回列表对象
     */
    public PageListQueryResult<SensitiveConfig> querySensitiveConfigList(SensitiveConfigQueryModel queryParam) {
        if (StringUtils.isEmpty(queryParam.getOrderColumn())) {
            queryParam.setOrderColumn("update_time");
            queryParam.setOrderType("desc");
        }
        List<SensitiveConfig> sensitiveConfigList = sensitiveConfigDao.querySensitiveConfigList(queryParam);
        if (CollectionUtils.isEmpty(sensitiveConfigList)) {
            return new PageListQueryResult<SensitiveConfig>(0, sensitiveConfigList);
        }
        for (SensitiveConfig sensitiveConfig : sensitiveConfigList) {
            SensitiveReviewQueryModel reviewQueryModel = new SensitiveReviewQueryModel();
            reviewQueryModel.setSensitiveConfigId(sensitiveConfig.getSensitiveConfigId());
            reviewQueryModel.setReviewStatusString("1");
            RequestAuthContext.RequestHeadUser currUser = RequestAuthContext.getRequestHeadUser();
            // 获取当前用户对应角色
            if (currUser != null) {
                if (!currUser.isAdmin() && !currUser.isSuperUser()) {
                    List<RolesResponse> roleList = rbacClient.listRoles(null, null, currUser.getNamespace(),
                            null, currUser.getUsername());
                    List<String> roleIdList = roleList.stream().map(RolesResponse::getUuid).collect(Collectors.toList
                            ());
                    reviewQueryModel.setRoleIdList(roleIdList);
                }
            }
            Integer pendingReviewNum = sensitiveReviewDao.querySensitiveViewListTotalSize(reviewQueryModel);
            sensitiveConfig.setPendingReviewNum(pendingReviewNum);
        }
        Integer totalCount = sensitiveConfigDao.querySensitiveConfigListTotalSize(queryParam);
        return new PageListQueryResult<SensitiveConfig>(totalCount, sensitiveConfigList);
    }

    /**
     * 保存敏感指令
     *
     * @param sensitiveConfig 敏感指令对象
     * @return GeneralResponse 通用返回对象
     */
    public GeneralResponse saveSensitiveConfig(SensitiveConfig sensitiveConfig) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        if (sensitiveConfig.getSensitiveConfigId() == null) {
            sensitiveConfig.setCreater(currUser);
            sensitiveConfig.setCreateTime(new Date());
            sensitiveConfig.setUpdater(currUser);
            sensitiveConfig.setUpdateTime(new Date());
            sensitiveConfigDao.insertSensitiveConfig(sensitiveConfig);
        } else {
            sensitiveConfig.setUpdater(currUser);
            sensitiveConfig.setUpdateTime(new Date());
            sensitiveConfigDao.updateSensitiveConfig(sensitiveConfig);
        }
        // 敏感指令规则处理
//        sensitiveRuleDao.deleteSensitiveRuleByConfigId(sensitiveConfig.getSensitiveConfigId());

        if (!CollectionUtils.isEmpty(sensitiveConfig.getRuleList())) {
            // 修改规则
            List<SensitiveRule> updateRuleList = sensitiveConfig.getRuleList().stream().filter(rule -> rule
                    .getSensitiveRuleId() != null).collect(Collectors.toList());

            // 删除规则
            List<SensitiveRule> originalRule = sensitiveRuleDao.selectBySensitiveConfigId(sensitiveConfig
                    .getSensitiveConfigId());
            Set<Long> updateRuleIdSet = updateRuleList.stream().map(SensitiveRule::getSensitiveRuleId).collect
                    (Collectors.toSet());
            List<Long> deleteRuleIdList = originalRule.stream().filter(item -> !updateRuleIdSet.contains(item
                    .getSensitiveRuleId())).map(SensitiveRule::getSensitiveRuleId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deleteRuleIdList)) {
                sensitiveRuleDao.deleteSensitiveRuleByRuleIdArray(deleteRuleIdList);
                sensitiveRuleWhiteDao.deleteRuleWhiteByRuleIdArray(deleteRuleIdList);
            }
            for (SensitiveRule sensitiveRule : sensitiveConfig.getRuleList()) {
                if (sensitiveRule.getSensitiveRuleId() != null) {
                    sensitiveRuleDao.updateSensitiveRuleByRuleId(sensitiveRule);
                } else {
                    sensitiveRule.setStatus(1);
                    sensitiveRule.setSensitiveConfigId(sensitiveConfig.getSensitiveConfigId());
                    sensitiveRuleDao.insertSensitiveRule(sensitiveRule);
                }
                if (!CollectionUtils.isEmpty(sensitiveRule.getWhiteList())) {
                    for (SensitiveRuleWhite sensitiveRuleWhite : sensitiveRule.getWhiteList()) {
                        if (sensitiveRuleWhite.getSensitiveRuleWhiteId() == null) {
                            sensitiveRuleWhite.setSensitiveRuleId(sensitiveRule.getSensitiveRuleId());
                            SensitiveRuleWhite white = sensitiveRuleWhiteDao.queryByRuleIdAndObjectId
                                    (sensitiveRuleWhite);
                            if (white == null) {
                                sensitiveRuleWhiteDao.insertSensitiveRuleWhite(sensitiveRuleWhite);
                            } else {
                                log.warn("add sensitive rule white exist ");
                            }
                        } else {
                            sensitiveRuleWhiteDao.updateSensitiveRuleWhite(sensitiveRuleWhite);
                        }
                    }
                }
            }
//            if (!CollectionUtils.isEmpty(updateRuleList)) {
//                for (SensitiveRule sensitiveRule : updateRuleList) {
//                    sensitiveRuleDao.updateSensitiveRuleByRuleId(sensitiveRule);
//                }
//            }
            // 创建规则
//            List<SensitiveRule> createRuleList = sensitiveConfig.getRuleList().stream().filter(rule -> rule
//                    .getSensitiveRuleId() == null).collect(Collectors.toList());
//            createRuleList.stream().forEach(rule -> {
//                rule.setSensitiveConfigId(sensitiveConfig
//                        .getSensitiveConfigId());
//                // 默认启动状态
//                if (rule.getStatus() == null) {
//                    rule.setStatus(1);
//                }
//            });
//            if (!CollectionUtils.isEmpty(createRuleList)) {
//                sensitiveRuleDao.insertBatchSensitiveRule(createRuleList);
//            }
        }
        return new GeneralResponse(true, null, sensitiveConfig);
    }

    public SensitiveConfig querySensitiveConfigById(Long sensitiveConfigId) {
        SensitiveConfig sensitiveConfig = sensitiveConfigDao.querySensitiveConfigById(sensitiveConfigId);
        //敏感指令规则查询
        if (sensitiveConfig != null) {
            List<SensitiveRule> ruleList = sensitiveRuleDao.selectBySensitiveConfigId(sensitiveConfigId);
//            for (SensitiveRule sensitiveRule : ruleList) {
//                List<SensitiveRuleWhite> whiteList = sensitiveRuleWhiteDao.queryRuleWhiteByRuleId(sensitiveRule
// .getSensitiveRuleId());
//
//                sensitiveRule.setWhiteList(whiteList);
//            }
            sensitiveConfig.setRuleList(ruleList);
        }
        return sensitiveConfig;
    }

    public GeneralResponse removeSensitiveConfig(Long sensitiveConfigId) {
        List<SensitiveRule> listRule = sensitiveRuleDao.selectBySensitiveConfigId(sensitiveConfigId);
        List<Long> listRuleId = listRule.stream().map(SensitiveRule::getSensitiveRuleId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listRuleId)) {
            sensitiveRuleWhiteDao.deleteRuleWhiteByRuleIdArray(listRuleId);
        }
        sensitiveRuleDao.deleteSensitiveRuleByConfigId(sensitiveConfigId);
        sensitiveConfigDao.removeSensitiveConfig(sensitiveConfigId);
        return new GeneralResponse();
    }

    public GeneralResponse saveSensitiveReview(SensitiveReview sensitiveReview) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        sensitiveReview.setApplicant(currUser);
        sensitiveReview.setApplyTime(new Date());
        sensitiveReview.setReviewStatus(SensitiveReview.REVIEW_STATUS_PENDING);
        sensitiveReviewDao.saveSensitiveReview(sensitiveReview);
        return new GeneralResponse(true, null, sensitiveReview);
    }

    public SensitiveReview querySensitiveReviewById(Long reviewId) {
        return sensitiveReviewDao.querySensitiveReviewById(reviewId);
    }

    public List<SensitiveReview> querySensitiveReviewByPipelineInstanceId(Long pipelineInstanceId) {
        return sensitiveReviewDao.querySensitiveReviewByPipelineInstanceId(pipelineInstanceId);
    }

    public GeneralResponse reviewInstance(String reviewIds, Integer reviewStatus) {
        SensitiveReview sensitiveReview = new SensitiveReview();
        sensitiveReview.setReviewStatus(reviewStatus);
        String currUser = RequestAuthContext.getRequestHeadUserName();
        sensitiveReview.setReviewer(currUser);
        sensitiveReview.setReviewTime(new Date());
//
        sensitiveReview.setReviewIdsString(reviewIds);
        sensitiveReviewDao.modifySensitiveReviewStatus(sensitiveReview);

        //处理通过审批脚本，作业数据
        if (reviewStatus.equals(ReviewStatusEnum.STATUS_2.getStatusCode())) {
            String[] reviewIdArray = reviewIds.split(",");
            List<Long> reviewIdList = Arrays.asList(reviewIdArray).stream().map(item -> Long.parseLong(item)).collect
                    (Collectors.toList());
            List<Long> instanceIdList = sensitiveReviewDao.queryCompleteAllReviewedInstance(reviewIdList);
//            opsActionBiz.executeReviewedInstance(instanceIdList);
            if (!CollectionUtils.isEmpty(instanceIdList)) {
                for (Long instanceId : instanceIdList) {
                    opsActionBiz.updatePipelineInstance(instanceId, OpsStatusEnum.STATUS_11.getStatusCode
                            (), null, null);
                }
            }
        } else if (reviewStatus.equals(ReviewStatusEnum.STATUS_3.getStatusCode())) {
            List<SensitiveReview> reviewList = sensitiveReviewDao.querySensitiveReviewListByIds(reviewIds);
            for (SensitiveReview sr : reviewList) {
                opsActionBiz.updatePipelineInstance(sr.getPipelineInstanceId(), OpsStatusEnum.STATUS_10.getStatusCode
                        (), new Date(), null);
            }
        }

        return new GeneralResponse(true, null, sensitiveReview);
    }

    public GeneralResponse removeSensitiveReview(Long reviewId) {
        sensitiveReviewDao.removeSensitiveReview(reviewId);
        return new GeneralResponse();
    }

    public GeneralResponse removeSensitiveReviewByInstanceId(Long pipelineInstanceId) {
        sensitiveReviewDao.removeSensitiveReviewByInstanceId(pipelineInstanceId);
        return new GeneralResponse();
    }

    public GeneralResponse validScriptContentSenstive(final List<OpsScript> scriptList) {
        GeneralResponse response = new GeneralResponse(true, "");
        List<String> errorTipList = Lists.newArrayList();
//        Map<String, Long> commandKeys = getSensitiveMap();
        List<SensitiveConfig> sensitiveConfigList = sensitiveConfigDao.querySensitiveConfigList(new
                SensitiveConfigQueryModel());
        for (OpsScript script : scriptList) {
            Set<String> sensitiveCommandSet = Sets.newHashSet();
            // 脚本内容解密
            script.decodeScriptContent();    // base64解码
            String content = script.getScriptContent();
            if (!StringUtils.isEmpty(content)) {
                // 遍历敏感指令集合，包含敏感指令即返回
                for (SensitiveConfig sensitiveConfig : sensitiveConfigList) {
                    boolean isSensitive = false;
//                    String params = StringUtils.isEmpty(sensitiveConfig.getParams()) ? "" : sensitiveConfig.getParams();
//                    if (sensitiveConfig.getMatchType() == 1) {
//                        String command = sensitiveConfig.getCommand() + " " + params;
//                        if (content.indexOf(command) != -1) {
//                            isSensitive = true;
//                        }
//                    } else if (sensitiveConfig.getMatchType() == 2) {
//                        String command = sensitiveConfig.getCommand() + "($|[\\s]+)" + params;
//                        Pattern p = Pattern.compile(command);
//                        Matcher m = p.matcher(content);
//                        while (m.find()) {
//                            isSensitive = true;
//                            break;
//                        }
//                    }
                    String params = "";
                    String command = sensitiveConfig.getCommand();
                    if (sensitiveConfig.getMatchType() == 1 && org.apache.commons.lang.StringUtils.isNotEmpty(sensitiveConfig.getParams())) {
                        params = sensitiveConfig.getParams();
                        for (String key : fbsArr) {
                            if (params.contains(key)) {
                                params = params.replace(key, "\\" + key);
                            }
                        }
                    }
                    command = command + "($|[\\s]+)" + params;
                    Pattern p = Pattern.compile(command);
                    Matcher m = p.matcher(content);
                    while (m.find()) {
                        isSensitive = true;
                        break;
                    }
                    if (isSensitive) {
                        sensitiveCommandSet.add(sensitiveConfig.getCommand());
                    }
                }
                if (!CollectionUtils.isEmpty(sensitiveCommandSet)) {
                    errorTipList.add(MessageFormat.format("名称为：{0}的脚本，包含{1}敏感指令", script.getScriptName(),
                            Joiner.on(",").join(sensitiveCommandSet)));
                }
            }
        }
        if (!CollectionUtils.isEmpty(errorTipList)) {
            response.setFlag(false);
            response.setErrorTip(Joiner.on(";").join(errorTipList));
        }
        return response;
    }

//    private Map<Integer, List<SensitiveConfig>> getSensitiveMap() {
//        // 敏感指令集合
//        Map<String, Long> commandKeys = Maps.newHashMap();
//        List<SensitiveConfig> sensitiveConfigList = sensitiveConfigDao.querySensitiveConfigList(new
//                SensitiveConfigQueryModel());
//        for (SensitiveConfig sensitiveConfig : sensitiveConfigList) {
//            sensitiveConfig.getMatchType()
//            // 过滤出所有关键字
////            if (sensitiveConfig.getMatchType() == 1) {
//                //完全匹配
//                commandKeys.put(sensitiveConfig.getCommand(), sensitiveConfig.getSensitiveConfigId());
////            } else {
////                // 正则匹配
////                if (sensitiveConfig.getCommand().indexOf("(") != -1 && sensitiveConfig.getCommand()
////                        .indexOf(")") != -1) {
////                    String itemString = sensitiveConfig.getCommand().substring(sensitiveConfig.getCommand()
////                                    .indexOf("(") + 1,
////                            sensitiveConfig.getCommand().indexOf(")")).trim();
////                    String[] items = itemString.split("\\|");
////                    for (String item : items) {
////                        commandKeys.put(sensitiveConfig.getCommand().substring(0, sensitiveConfig.getCommand()
////                                .indexOf("(")) + item, sensitiveConfig.getSensitiveConfigId());
////                    }
////                } else {
////                    commandKeys.put(sensitiveConfig.getCommand(), sensitiveConfig.getSensitiveConfigId());
////                }
////
////                Pattern p = Pattern.compile(sensitiveConfig.getCommand());
////                Matcher m=p.matcher("echo 'test' \n yum  install aaaa");
////            }
//        }
//        return commandKeys;
//    }

    public PageListQueryResult<SensitiveReviewPageResponse> querySensitiveReviewList(SensitiveReviewQueryModel
                                                                                             queryParam) {
        RequestAuthContext.RequestHeadUser currUser = RequestAuthContext.getRequestHeadUser();
        // 获取当前用户对应角色
        if (currUser != null) {
//            Boolean isAdmin = false;
//            for (RolesResponse rolesResponse : roleList) {
//                if (rolesResponse.getAdminRole()) {
//                    isAdmin = true;
//                    break;
//                }
//            }
            if (!currUser.isAdmin() && !currUser.isSuperUser()) {
                List<RolesResponse> roleList = rbacClient.listRoles(null, null, currUser.getNamespace(),
                        0, currUser.getUsername());
                List<String> roleIdList = roleList.stream().map(RolesResponse::getUuid).collect(Collectors.toList());
                queryParam.setRoleIdList(roleIdList);
            }
        }
        List<SensitiveReviewPageResponse> sensitiveReviewList = sensitiveReviewDao.querySensitiveReviewList(queryParam);
        if (CollectionUtils.isEmpty(sensitiveReviewList)) {
            return new PageListQueryResult<>(0, sensitiveReviewList);
        }
        Integer totalCount = sensitiveReviewDao.querySensitiveViewListTotalSize(queryParam);
        return new PageListQueryResult<>(totalCount, sensitiveReviewList);
    }

    public GeneralResponse updateStatusByRuleId(Integer status, Long sensitiveRuleId) {
        SensitiveRule sensitiveRule = new SensitiveRule();
        sensitiveRule.setStatus(status);
        sensitiveRule.setSensitiveRuleId(sensitiveRuleId);
        sensitiveRuleDao.updateStatusByRuleId(sensitiveRule);
        return new GeneralResponse(true, null, sensitiveRule);
    }

    public GeneralResponse updateObjectStatusByWhiteId(Integer status, Long sensitiveRuleWhiteId) {
        SensitiveRuleWhite sensitiveRule = new SensitiveRuleWhite();
        sensitiveRule.setObjectStatus(status);
        sensitiveRule.setSensitiveRuleWhiteId(sensitiveRuleWhiteId);
        sensitiveRuleWhiteDao.updateStatusByWhiteId(sensitiveRule);
        return new GeneralResponse(true, null, sensitiveRule);
    }

    public PageListQueryResult<SensitiveRuleWhite> querySensitiveRuleWhiteList(SensitiveRuleWhiteQueryModel
                                                                                       queryParam) {
        Integer totalSize = sensitiveRuleWhiteDao.querySensitiveRuleWhiteListTotalSize(queryParam);

        List<SensitiveRuleWhite> listWhite = null;
        if (totalSize > 0) {
            listWhite = sensitiveRuleWhiteDao.querySensitiveRuleWhiteList(queryParam);
        } else {
            listWhite = Lists.newArrayList();
        }
        return new PageListQueryResult<>(totalSize, listWhite);
    }

    public PageListQueryResult<SensitiveLevel> querySensitiveLevelList(SensitiveLevelQueryModel queryParam) {
        Integer totalSize = sensitiveLevelDao.querySensitiveLevelListTotalSize(queryParam);
        List<SensitiveLevel> listLevel = null;
        if (totalSize > 0) {
            listLevel =  sensitiveLevelDao.querySensitiveLevelList(queryParam);

            for (SensitiveLevel sensitiveLevel : listLevel) {
                List<String> auditList = Lists.newArrayList();
                if (!CollectionUtils.isEmpty(sensitiveLevel.getAuditRoleList())) {
                    List<RolesResponse> auditRoleList = rbacClient.listRoles(sensitiveLevel.getAuditRoleList(), null, null, null, null);
                    auditRoleList.stream().forEach(item -> auditList.add(JSON.toJSONString(item)));
                    sensitiveLevel.setAuditRoleList(auditList);
                }

                if (!CollectionUtils.isEmpty(sensitiveLevel.getExecRoleList())) {
                    List<String> execList = Lists.newArrayList();
                    List<RolesResponse> execRoleList = rbacClient.listRoles(sensitiveLevel.getExecRoleList(), null, null, null, null);
                    execRoleList.stream().forEach(item -> execList.add(JSON.toJSONString(item)));
                    sensitiveLevel.setExecRoleList(execList);
                }
            }
        }else {
            listLevel = Lists.newArrayList();
        }
        return new PageListQueryResult<>(totalSize, listLevel);
    }

    public GeneralResponse updateSensitiveLevelById(SensitiveLevel sensitiveLevel) {
        if (sensitiveLevel == null || sensitiveLevel.getId() == null) {
            return new GeneralResponse(false, "修改敏感指令级别对象或级别id为空");
        }
        String currUser = RequestAuthContext.getRequestHeadUserName();
        sensitiveLevel.setUpdater(currUser);
        sensitiveLevel.setUpdateTime(new Date());
        sensitiveLevelDao.updateSensitiveLevelById(sensitiveLevel);
        return new GeneralResponse(true, null, sensitiveLevel);
    }

    public PageListQueryResult<SensitiveRule> querySensitiveRuleList(SensitiveRuleQueryModel sensitiveRuleQueryModel) {
       Integer count = sensitiveRuleDao.querySensitiveRuleListTotalSize(sensitiveRuleQueryModel);
        List<SensitiveRule> sensitiveRuleList = null;
       if (count > 0) {
           sensitiveRuleList = sensitiveRuleDao.querySensitiveRuleList(sensitiveRuleQueryModel);
       } else {
           sensitiveRuleList = Lists.newArrayList();
       }
        return new PageListQueryResult<>(count, sensitiveRuleList);
    }

    public GeneralResponse batchCreateSensitiveRuleWhite(SensitiveRuleWhiteBatchCreateReq sensitiveRuleWhiteBatchCreateReq) {
        if (!CollectionUtils.isEmpty(sensitiveRuleWhiteBatchCreateReq.getSensitiveRuleWhiteList())) {
            for (SensitiveRuleWhite sensitiveRuleWhite : sensitiveRuleWhiteBatchCreateReq.getSensitiveRuleWhiteList()) {
                if (sensitiveRuleWhite.getSensitiveRuleWhiteId() == null) {
                    sensitiveRuleWhite.setSensitiveRuleId(sensitiveRuleWhite.getSensitiveRuleId());
                    SensitiveRuleWhite white = sensitiveRuleWhiteDao.queryByRuleIdAndObjectId
                            (sensitiveRuleWhite);
                    if (white == null) {
                        sensitiveRuleWhiteDao.insertSensitiveRuleWhite(sensitiveRuleWhite);
                    } else {
                        log.warn("add sensitive rule white exist ");
                    }
                } else {
                    sensitiveRuleWhiteDao.updateSensitiveRuleWhite(sensitiveRuleWhite);
                }
            }
        }
        return new GeneralResponse();
    }
}
