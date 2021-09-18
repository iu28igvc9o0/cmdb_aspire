package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import com.aspire.mirror.composite.service.opsmanage.ICompSensitiveService;
import com.aspire.mirror.ops.api.domain.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.SensitiveClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 高危指令操作控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage
 * 类名称:    CompSensitiveController.java
 * 类描述:    高危指令操作控制层
 * 创建人:    JinSu
 * 创建时间:  2020/2/11 13:54
 * 版本:      v1.0
 */
@RestController
public class CompSensitiveController implements ICompSensitiveService {

    @Autowired
    private SensitiveClient sensitiveClient;
    @Autowired
    protected ResourceAuthHelper resAuthHelper;


    @Override
    @ResAction(action = "view", resType = "sensitive")
    public PageListQueryResult<SensitiveConfig> querySensitiveConfigList(@RequestBody SensitiveConfigQueryModel queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.querySensitiveConfigList(queryParam);
    }

    @Override
    @ResAction(action = "create", resType = "sensitive")
    public GeneralResponse saveSensitiveConfig(@RequestBody SensitiveConfig sensitiveConfig) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.saveSensitiveConfig(sensitiveConfig);
    }

    @Override
    @ResAction(action = "view", resType = "sensitive")
    public SensitiveConfig querySensitiveConfigById(@RequestParam("sensitiveConfigId") Long sensitiveConfigId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.querySensitiveConfigById(sensitiveConfigId);
    }

    @Override
    @ResAction(action = "delete", resType = "sensitive")
    public GeneralResponse removeSensitiveConfig(@RequestParam("sensitiveConfigId") Long sensitiveConfigId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.removeSensitiveConfig(sensitiveConfigId);
    }

    @Override
    @ResAction(action = "view", resType = "sensitive")
    public GeneralResponse checkScriptSensitivity(@RequestBody CheckScriptRequst checkScriptRequst) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.checkScriptSensitivity(checkScriptRequst);
    }

    @Override
    @ResAction(action = "view", resType = "sensitiveReview")
    public PageListQueryResult<SensitiveReviewPageResponse> querySensitiveReviewList(@RequestBody SensitiveReviewQueryModel
                                                                                                 queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.querySensitiveReviewList(queryParam);
    }

    @Override
    @ResAction(action = "review", resType = "sensitiveReview")
    public GeneralResponse reviewInstance(@RequestParam("review_ids") String reviewIds, @RequestParam("review_status") Integer reviewStatus) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.reviewInstance(reviewIds, reviewStatus);
    }

    @Override
    @ResAction(action = "create", resType = "sensitive")
    public GeneralResponse updateStatusByRuleId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleId") Long sensitiveRuleId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.updateStatusByRuleId(status, sensitiveRuleId);
    }

    @Override
    @ResAction(action = "review", resType = "sensitiveReview")
    public List<SensitiveReview> querySensitiveReviewByPipelineInstanceId(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.querySensitiveReviewByPipelineInstanceId(pipelineInstanceId);
    }

    @Override
    @ResAction(action = "create", resType = "sensitive")
    public GeneralResponse updateObjectStatusByWhiteId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleWhiteId") Long sensitiveRuleWhiteId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return sensitiveClient.updateObjectStatusByWhiteId(status, sensitiveRuleWhiteId);
    }

    @Override
    @ResAction(action = "view", resType = "sensitive")
    public PageListQueryResult<SensitiveRuleWhite> querySensitiveRuleWhiteList(@RequestBody SensitiveRuleWhiteQueryModel
                                                                                           queryParam) {
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        
        return sensitiveClient.querySensitiveRuleWhiteList(queryParam);
    }

    @Override
    public PageListQueryResult<SensitiveLevel> querySensitiveLevelList(@RequestBody SensitiveLevelQueryModel queryParam) {
        return sensitiveClient.querySensitiveLevelList(queryParam);
    }

    @Override
    public GeneralResponse updateSensitiveLevelById(@RequestBody SensitiveLevel sensitiveLevel) {
        return sensitiveClient.updateSensitiveLevelById(sensitiveLevel);
    }

    @Override
    public List<SensitiveRule> getSensitiveRuleListByPipelineId(@RequestParam("pipelineId") String pipelineId) {
        return null;
    }

    @Override
    public PageListQueryResult<SensitiveRule> querySensitiveRuleList(@RequestBody SensitiveRuleQueryModel sensiTiveRuleQueryModel) {
        return sensitiveClient.querySensitiveRuleList(sensiTiveRuleQueryModel);
    }

    @Override
    @ResAction(action = "create", resType = "sensitive")
    public GeneralResponse batchCreateSensitiveRuleWhite(@RequestBody SensitiveRuleWhiteBatchCreateReq sensitiveRuleWhiteBatchCreateReq) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return sensitiveClient.batchCreateSensitiveRuleWhite(sensitiveRuleWhiteBatchCreateReq);
    }


}
