package com.aspire.mirror.ops.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.biz.OpsActionBiz;
import com.aspire.mirror.ops.clientservice.RbacClient;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.*;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;
import com.aspire.mirror.ops.domain.OpsStepInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.aspire.mirror.ops.api.domain.OpsStepDTO.OPS_TYPE_SCRIPT;

/**
 * 敏感指令处理帮助类
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    SensitiveHelper
 * 类描述:    敏感指令处理帮助类
 * 创建人:    JinSu
 * 创建时间:  2021/3/11 10:54
 * 版本:      v1.0
 */
@Component
@Slf4j
public class SensitiveHelper {
    private static final String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|", "!"};
    @Autowired
    private SensitiveConfigDao sensitiveConfigDao;

    @Autowired
    private SensitiveRuleDao sensitiveRuleDao;

    @Autowired
    private SensitiveReviewDao sensitiveReviewDao;

    @Autowired
    private SensitiveRuleWhiteDao sensitiveRuleWhiteDao;

    @Autowired
    private SensitiveLevelDao sensitiveLevelDao;

    @Autowired
    private RbacClient rbacClient;
    // 执行作业或者脚本匹配敏感指令处理 生成审核数据
    public Integer stepInstanceSenstiveProcess(List<OpsStepInstance> opsStepInstancesList, OpsPipelineInstance
            pipelineInstance, OpsTriggerWayEnum opsTriggerWayEnum) {
        Integer resultStatus = OpsStatusEnum.STATUS_5.getStatusCode();
        RequestAuthContext.RequestHeadUser currUser = RequestAuthContext.getRequestHeadUser();
        Map<String, String> roleMap = null;
        // 获取当前用户对应角色
        if (currUser != null) {
            List<RolesResponse> roleList = rbacClient.listRoles(null, null, currUser.getNamespace(),
                    null, currUser.getUsername());
            roleMap = roleList.stream().collect(Collectors.toMap(RolesResponse::getUuid,
                    RolesResponse::getName));
        }
        //        Map<Long, List<String>> commandKeys = getSensitiveMap();
        resultStatus = this.sensitiveMatch(opsStepInstancesList, pipelineInstance, opsTriggerWayEnum, resultStatus, roleMap);
        return resultStatus;
    }

    private Integer sensitiveMatch(List<OpsStepInstance> opsStepInstancesList, OpsPipelineInstance pipelineInstance, OpsTriggerWayEnum opsTriggerWayEnum, Integer resultStatus, Map<String, String> roleMap) {
        List<SensitiveConfig> sensitiveConfigList = this.querySensitiveConfigList(new
                SensitiveConfigQueryModel());
        // 匹配到的敏感指令规则
        List<Long> matchRuleIdList = Lists.newArrayList();
//        Set<Long> sudoConfigIdSet = Sets.newHashSet();
        for (OpsStepInstance opsStepInstance : opsStepInstancesList) {
            if (StringUtils.isNotEmpty(opsStepInstance.getScriptContent()) && opsStepInstance.getOpsType() == 0) {
                // 遍历敏感指令集合
                for (SensitiveConfig sensitiveConfig : sensitiveConfigList) {
                    // 判断脚本内容是否包含敏感指令
                    Boolean includeSenstiveFlag = getSensitiveFlag(sensitiveConfig, opsStepInstance);
                    // 判断是否需要加sudo权限
//                    if (includeSenstiveFlag.getRight()) {
//                        sudoConfigIdSet.add(sensitiveConfig.getSensitiveConfigId());
//                    }
                    if (includeSenstiveFlag) {
                        List<SensitiveRule> ruleList = sensitiveRuleDao.selectPassedBySensitiveConfigId
                                (sensitiveConfig.getSensitiveConfigId());
                        if (!CollectionUtils.isEmpty(ruleList)) {
                            for (SensitiveRule rule : ruleList) {
                                // 针对已经匹配了的规则，多脚本作业无需多次处理;同时处于白名单的无需处理
                                if (!matchRuleIdList.contains(rule.getSensitiveRuleId())) {
                                    // 验证规则管理设备IP和执行用户
                                    RuleValid ruleValid = new RuleValid(opsStepInstance, rule, roleMap, sensitiveConfig.getExecRoleList()).invoke();
                                    Boolean hostFlag = ruleValid.getHostFlag();
                                    Boolean userFlag = ruleValid.getUserFlag();
                                    Boolean execRoleFlag = ruleValid.getExecRoleFlag();
                                    Map<String, Object> contentMap = ruleValid.getContentMap();
                                    //只有IP范围满足同时可执行范围满足时，才能认定此脚本通过敏感指令规则，需要审核或阻断
                                    if (hostFlag || userFlag || execRoleFlag) {
                                        matchRuleIdList.add(rule.getSensitiveRuleId());
                                        Integer reviewStatus = null;

                                        String handUser = "";
                                        if (opsTriggerWayEnum.getStatusCode().equals(opsTriggerWayEnum.TRIGGER_BY_API.getStatusCode()) && NoAuthLabelEnum.getUserNameByLabelId(pipelineInstance
                                                .getLabelId()) != null) {
                                            reviewStatus = ReviewStatusEnum.STATUS_2.getStatusCode();
                                            handUser = NoAuthLabelEnum.getUserNameByLabelId(pipelineInstance
                                                    .getLabelId());
                                        } else {
                                            SensitiveRuleWhite sensitiveRuleWhite = new SensitiveRuleWhite();
                                            sensitiveRuleWhite.setSensitiveRuleId(rule.getSensitiveRuleId());
                                            sensitiveRuleWhite.setObjectId(pipelineInstance.getPipelineId());
                                            SensitiveRuleWhite resultWhite = sensitiveRuleWhiteDao
                                                    .queryValidWhiteByRuleIdAndObjectId(sensitiveRuleWhite);
                                            if (resultWhite != null) {
                                                reviewStatus = ReviewStatusEnum.STATUS_2.getStatusCode();
                                                handUser = SensitiveReview.WHITE_LIST_USER_NAME;
                                            } else {
                                                // 阻断、审核判断
                                                if (sensitiveConfig.getDealType() == 2) {
                                                    //阻断
                                                    resultStatus = OpsStatusEnum.STATUS_7.getStatusCode();
                                                    reviewStatus = ReviewStatusEnum.STATUS_9.getStatusCode();
                                                } else if (sensitiveConfig.getDealType() == 1) {
                                                    //审核
                                                    resultStatus = OpsStatusEnum.STATUS_8.getStatusCode();
                                                    reviewStatus = ReviewStatusEnum.STATUS_0.getStatusCode();
                                                }
                                            }
                                        }
                                        // 拼装审核数据
                                        buildSensitiveReviewData(opsStepInstance, rule, contentMap,
                                                sensitiveConfig, reviewStatus, handUser);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultStatus;
    }

    // 激活步骤之前解析sudo赋权参数
    public void generateSudoAuthParam(OpsStepInstance stepInstance) {
        //拼装权限列表
        List<SensitiveReview> reviewList = sensitiveReviewDao.querySensitiveReviewByPipelineInstanceId(stepInstance.getPipelineInstanceId());
        if (CollectionUtils.isNotEmpty(reviewList)) {
            Set<Long> configIdSet = reviewList.stream().map(SensitiveReview::getSensitiveConfigId).collect(Collectors.toSet());
            List<String> authList = Lists.newArrayList();
            for (Long configId : configIdSet) {
                SensitiveConfig sensitiveConfig = sensitiveConfigDao.querySensitiveConfigById(configId);
                // 生成权限指令列表
                if (sensitiveConfig != null) {
                    // 判断是否需要加sudo权限
                    if (stepInstance.getOpsType().equals(OPS_TYPE_SCRIPT)) {
                        Boolean isSudo = getIsSudo(stepInstance, sensitiveConfig);
                        if (isSudo) {
                            String[] pathArray = sensitiveConfig.getPath().split(",");
                            for (String path : pathArray) {
                                Integer firstSpaceIndex = sensitiveConfig.getCommand().indexOf(" ");
                                if (firstSpaceIndex == -1) {
                                    authList.add(path + "/" + sensitiveConfig.getCommand());
                                } else {
                                    authList.add(path + "/" + sensitiveConfig.getCommand().substring(0, firstSpaceIndex));
                                }
                            }
                        }
                    }
                }
            }
            stepInstance.setAuthList(authList);
        }
    }

    private Boolean getIsSudo(OpsStepInstance stepInstance, SensitiveConfig sensitiveConfig) {
        Boolean isSudo = false;
        String params = "";
        String command = sensitiveConfig.getCommand();
        if (sensitiveConfig.getMatchType() == 1 && StringUtils.isNotEmpty(sensitiveConfig.getParams())) {
            params = sensitiveConfig.getParams();
            for (String key : fbsArr) {
                if (params.contains(key)) {
                    params = params.replace(key, "\\" + key);
                }
            }
        }
        command = command + "($|[\\s]+)" + params;
        command = "sudo[\\s]+" + command;
        Pattern p2 = Pattern.compile(command);
        Matcher m2 = p2.matcher(stepInstance.getScriptContent());
        while (m2.find()) {
            isSudo = true;
            break;
        }
        return isSudo;
    }

//
//    private Map<Long, List<String>> getSensitiveMap() {
//        // 敏感指令集合
//        Map<Long, List<String>> commandKeys = Maps.newHashMap();
//        List<SensitiveConfig> sensitiveConfigList = sensitiveConfigDao.querySensitiveConfigList(new
//                SensitiveConfigQueryModel());
//        for (SensitiveConfig sensitiveConfig : sensitiveConfigList) {
//            // 过滤出所有关键字
//            List<String> commandList = Lists.newArrayList();
//            if (sensitiveConfig.getMatchType() == 1) {
//                // 完全匹配
//                commandList.add(sensitiveConfig.getCommand());
//            } else {
//                // 正则匹配
//                if (sensitiveConfig.getCommand().indexOf("(") != -1 && sensitiveConfig.getCommand().indexOf(")") !=
//                        -1) {
//                    String itemString = sensitiveConfig.getCommand().substring(sensitiveConfig.getCommand()
//                                    .indexOf("(") + 1,
//                            sensitiveConfig.getCommand().indexOf(")")).trim();
//                    String[] items = itemString.split("\\|");
//                    for (String item : items) {
//                        commandList.add(sensitiveConfig.getCommand().substring(0, sensitiveConfig.getCommand()
//                                .indexOf("(")) + item);
//
//                    }
//                } else {
//                    commandList.add(sensitiveConfig.getCommand());
//                }
//            }
//            commandKeys.put(sensitiveConfig.getSensitiveConfigId(), commandList);
//        }
//        return commandKeys;
//    }

    private Boolean getSensitiveFlag(SensitiveConfig sensitiveConfig, OpsStepInstance opsStepInstance) {
        boolean isSensitive = false;
//        boolean isSudo = false;
        String params = "";
        String command = sensitiveConfig.getCommand();
        if (sensitiveConfig.getMatchType() == 1 && StringUtils.isNotEmpty(sensitiveConfig.getParams())) {
            params = sensitiveConfig.getParams();
            for (String key : fbsArr) {
                if (params.contains(key)) {
                    params = params.replace(key, "\\" + key);
                }
            }
        }
        //        if (isSensitive) {
//            command = "sudo[\\s]+" + command;
//            Pattern p2 = Pattern.compile(command);
//            Matcher m2 = p2.matcher(opsStepInstance.getScriptContent());
//            while (m2.find()) {
//                isSudo = true;
//                break;
//            }
//        }
        // $从字符串末尾进行匹配 或者空格不少于一个
        command = command + "($|[\\s]+)" + params;
        Pattern p = Pattern.compile(command);
        Matcher m = p.matcher(opsStepInstance.getScriptContent());
        while (m.find()) {
            isSensitive = true;
            break;
        }

//        if (sensitiveConfig.getMatchType() == 1) {
//            if (opsStepInstance.getScriptContent().indexOf(sensitiveConfig.getCommand()) != -1) {
//                isSensitive = true;
//            }
//        } else if (sensitiveConfig.getMatchType() == 2) {
//            Pattern p = Pattern.compile(sensitiveConfig.getCommand());
//            Matcher m = p.matcher(opsStepInstance.getScriptContent());
//            while (m.find()) {
//                isSensitive = true;
//                break;
//            }
//        }
        return isSensitive;
    }

    private void buildSensitiveReviewData(OpsStepInstance opsStepInstance,
                                          SensitiveRule rule, Map<String, Object> contentMap, SensitiveConfig
                                                  sensitiveConfig, Integer reviewStatus, String handUser) {
        SensitiveReview sensitiveReview = new SensitiveReview();
        Date now = new Date();
        String currUser = RequestAuthContext.getRequestHeadUserName();
        sensitiveReview.setApplicant(currUser);
        sensitiveReview.setApplyTime(now);
        if (reviewStatus.equals(ReviewStatusEnum.STATUS_2.getStatusCode())) {
            sensitiveReview.setReviewer(handUser);
            sensitiveReview.setReviewTime(now);
        }
        sensitiveReview.setReviewStatus(reviewStatus);
        sensitiveReview.setCommand(sensitiveConfig.getCommand());
        sensitiveReview.setRuleName(rule.getRuleName());
        sensitiveReview.setPipelineInstanceId(opsStepInstance.getPipelineInstanceId());
        sensitiveReview.setSensitiveConfigId(sensitiveConfig.getSensitiveConfigId());
        sensitiveReview.setSensitiveRuleId(rule.getSensitiveRuleId());
        StringBuffer reviewContent = new StringBuffer();
        for (String key : contentMap.keySet()) {
            reviewContent.append(key).append(":").append(contentMap.get(key)).append(",");
        }
        reviewContent.append("不允许执行敏感指令").append(sensitiveConfig.getCommand());
        sensitiveReview.setReviewContent(reviewContent.toString());

        sensitiveReviewDao.saveSensitiveReview(sensitiveReview);
        if (sensitiveReview.getReviewId() != null) {
//            JSONObject jsonObject = JSONObject.parseObject(rule.getRuleRange());
//            JSONArray reviewRoleArray = jsonObject.getJSONArray("review_role_ids");
//            if (reviewRoleArray != null && !reviewRoleArray.isEmpty()) {
            if (StringUtils.isNotEmpty(sensitiveConfig.getAuditRoleList())) {
                List<String> reviewRoleArray = Arrays.asList(sensitiveConfig.getAuditRoleList().split(","));
                List<SensitiveReviewRole> reviewRoleList = reviewRoleArray.stream().map(obj -> {
                    SensitiveReviewRole sensitiveReviewRole = new SensitiveReviewRole();
//                    JSONObject map = (JSONObject) obj;
                    sensitiveReviewRole.setReviewId(sensitiveReview.getReviewId());
                    sensitiveReviewRole.setRoleId(obj);

                    List<String> roleUUids = Lists.newArrayList();
                    roleUUids.add(obj);
                    List<RolesResponse> roleDetailList = rbacClient.listRoles(roleUUids, null, null, null, null);
                    sensitiveReviewRole.setRoleName(CollectionUtils.isEmpty(roleDetailList) ? null : roleDetailList.get(0).getName());
                    return sensitiveReviewRole;
                }).collect(Collectors.toList());
                sensitiveReviewDao.saveBatchSensitiveReviewRole(reviewRoleList);
            }
        }
//        reviewList.add(sensitiveReview);
    }

    public List<SensitiveConfig> querySensitiveConfigList(SensitiveConfigQueryModel sensitiveConfigQueryModel) {
        List<SensitiveConfig> sensitiveConfigList = sensitiveConfigDao.querySensitiveConfigList(new
                SensitiveConfigQueryModel());
        return sensitiveConfigList;
    }

    public void reviewSensitiveApply(Long pipelineInstanceId) {
        sensitiveReviewDao.modifyBySensitiveReviewApply(pipelineInstanceId);
    }

    private class RuleValid {
        private OpsStepInstance opsStepInstance;
        private SensitiveRule rule;
        private Map<String, String> roleMap;
        private Boolean hostFlag;
        private Boolean userFlag;
        private Boolean execRoleFlag;
        private Map<String, Object> contentMap;
        private String execRoleListString;

        public RuleValid(OpsStepInstance opsStepInstance, SensitiveRule rule, Map<String, String> roleMap, String execRoleListString) {
            this.opsStepInstance = opsStepInstance;
            this.rule = rule;
            this.roleMap = roleMap;
            this.execRoleListString = execRoleListString;
        }

        public Boolean getHostFlag() {
            return hostFlag;
        }

        public Boolean getUserFlag() {
            return userFlag;
        }

        public Boolean getExecRoleFlag() {
            return execRoleFlag;
        }

        public Map<String, Object> getContentMap() {
            return contentMap;
        }

        public RuleValid invoke() {
            hostFlag = true;
            userFlag = true;
            execRoleFlag = true;
            contentMap = Maps.newHashMap();
            if (!StringUtils.isEmpty(rule.getRuleRange())) {
                JSONObject jsonObject = JSONObject.parseObject(rule.getRuleRange());
                JSONArray hostArray = jsonObject.getJSONArray("target_host");
                if (hostArray != null && !hostArray.isEmpty()) {
                    List<String> hosts = hostArray.stream().map(obj -> {
                        JSONObject map = (JSONObject) obj;
                        return map.getString("proxy_id") + ":" + map.getString("agent_ip");
                    }).collect(Collectors.toList());
                    // 两集合取交集，结果存hosts
                    hosts.retainAll(opsStepInstance.getTargetHosts());
                    if (hosts.isEmpty()) {
                        hostFlag = false;
                    } else {
                        String target = hosts.size() > 3 ? Joiner.on(",").join(hosts.subList(0, 3)) + "等" : Joiner.on
                                (",").join(hosts);
                        contentMap.put("设备", target);
                    }
                }
                JSONArray userArray = jsonObject.getJSONArray("target_ops_user");
                if (userArray != null && !userArray.isEmpty()) {
                    List<String> userList = userArray.stream().map(obj -> {
                        JSONObject map = (JSONObject) obj;
                        return map.getString("accountName");
                    }).collect(Collectors.toList());

                    if (userList.contains(opsStepInstance.getTargetOpsUser())) {
                        userFlag = false;
                    } else {
                        contentMap.put("执行账号", opsStepInstance.getTargetOpsUser());
                    }
                }
//                JSONArray execRoleArray = jsonObject.getJSONArray("exec_role_ids");
//                if (execRoleArray != null && !execRoleArray.isEmpty()) {
                if (StringUtils.isNotEmpty(execRoleListString)) {
                    List<String> execRoleList = Lists.newArrayList(execRoleListString.split(","));
//                    List<String> execRoleList = execRoleArray.stream().map(obj -> {
//                        JSONObject map = (JSONObject) obj;
//                        return map.getString("uuid");
//                    }).collect(Collectors.toList());
//                    List<String> execRoleList = new ArrayList<>(Arrays.asList(jsonObject.getString("exec_role_ids")
//                            .split(",")));
                    if (roleMap != null) {
                        // 两集合取交集，结果存execRoleList
                        execRoleList.retainAll(roleMap.keySet());
                        // 两集合存在交集，则该用户存在可执行角色，无需审核
                        if (!execRoleList.isEmpty()) {
                            execRoleFlag = false;
                        } else {
                            contentMap.put("系统用户关联角色", Joiner.on(",").join(roleMap.values()));
                        }
                    } else {
                        execRoleFlag = false;
                    }

                }
            }
            return this;
        }
    }
}
