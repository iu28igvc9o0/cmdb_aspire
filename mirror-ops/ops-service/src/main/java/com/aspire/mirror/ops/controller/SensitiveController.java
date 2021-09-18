package com.aspire.mirror.ops.controller;

import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.api.service.ISensitiveService;
import com.aspire.mirror.ops.biz.SensitiveBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 敏感指令控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.controller
 * 类名称:    SensitiveController.java
 * 类描述:    敏感指令控制层
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 14:00
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class SensitiveController implements ISensitiveService {
    @Autowired
    private SensitiveBiz sensitiveBiz;
    @Override
    @ResponseStatus(HttpStatus.OK)
    public PageListQueryResult<SensitiveConfig> querySensitiveConfigList(@RequestBody SensitiveConfigQueryModel queryParam) {
        return sensitiveBiz.querySensitiveConfigList(queryParam);
    }

    @Override
    public GeneralResponse saveSensitiveConfig(@RequestBody SensitiveConfig sensitiveConfig) {
        return sensitiveBiz.saveSensitiveConfig(sensitiveConfig);
    }

    @Override
    public SensitiveConfig querySensitiveConfigById(@RequestParam("sensitiveConfigId") Long sensitiveConfigId) {
        return sensitiveBiz.querySensitiveConfigById(sensitiveConfigId);
    }

    @Override
    public GeneralResponse removeSensitiveConfig(@RequestParam("sensitiveConfigId") Long sensitiveConfigId) {
        return sensitiveBiz.removeSensitiveConfig(sensitiveConfigId);
    }

    @Override
    public GeneralResponse saveSensitiveReview(@RequestBody SensitiveReview sensitiveReview) {
        return sensitiveBiz.saveSensitiveReview(sensitiveReview);
    }

    @Override
    public SensitiveReview querySensitiveReviewById(@RequestParam("review_id") Long reviewId) {
        return sensitiveBiz.querySensitiveReviewById(reviewId);
    }

    @Override
    public List<SensitiveReview> querySensitiveReviewByPipelineInstanceId(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        return sensitiveBiz.querySensitiveReviewByPipelineInstanceId(pipelineInstanceId);
    }

    @Override
    public GeneralResponse reviewInstance(@RequestParam("review_ids") String reviewIds, @RequestParam("review_status") Integer reviewStatus) {
        if (StringUtils.isEmpty(reviewIds) || reviewStatus == null) {
            log.error("SensitiveController[reviewInstance] param is invalid");
            return new GeneralResponse(false);
        }
        return sensitiveBiz.reviewInstance(reviewIds, reviewStatus);
    }

    @Override
    public GeneralResponse removeSensitiveReview(@PathVariable("review_id") Long reviewId) {
        return sensitiveBiz.removeSensitiveReview(reviewId);
    }

    @Override
    public GeneralResponse removeSensitiveReviewByInstanceId(@PathVariable("pipeline_instance_id") Long pipelineInstanceId) {
        return sensitiveBiz.removeSensitiveReviewByInstanceId(pipelineInstanceId);
    }

    @Override
    public GeneralResponse checkScriptSensitivity(@RequestBody CheckScriptRequst checkScriptRequst) {
        if (checkScriptRequst == null || CollectionUtils.isEmpty(checkScriptRequst.getScriptList())){
            return null;
        } else {
            return sensitiveBiz.validScriptContentSenstive(checkScriptRequst.getScriptList());
        }
    }

    @Override
    public PageListQueryResult<SensitiveReviewPageResponse> querySensitiveReviewList(@RequestBody SensitiveReviewQueryModel
                                                                                                 queryParam) {
        return sensitiveBiz.querySensitiveReviewList(queryParam);
    }

    @Override
    public GeneralResponse updateStatusByRuleId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleId") Long sensitiveRuleId) {
        return sensitiveBiz.updateStatusByRuleId(status, sensitiveRuleId);
    }

    @Override
    public GeneralResponse updateObjectStatusByWhiteId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleWhiteId") Long sensitiveRuleWhiteId) {
        return sensitiveBiz.updateObjectStatusByWhiteId(status, sensitiveRuleWhiteId);
    }

    @Override
    public PageListQueryResult<SensitiveRuleWhite> querySensitiveRuleWhiteList(@RequestBody SensitiveRuleWhiteQueryModel queryParam) {
        return sensitiveBiz.querySensitiveRuleWhiteList(queryParam);
    }

    @Override
    public PageListQueryResult<SensitiveLevel> querySensitiveLevelList(@RequestBody SensitiveLevelQueryModel queryParam) {
        return sensitiveBiz.querySensitiveLevelList(queryParam);
    }

    @Override
    public GeneralResponse updateSensitiveLevelById(@RequestBody SensitiveLevel sensitiveLevel) {
        return sensitiveBiz.updateSensitiveLevelById(sensitiveLevel);
    }

    @Override
    public GeneralResponse batchCreateSensitiveRuleWhite(@RequestBody SensitiveRuleWhiteBatchCreateReq sensitiveRuleWhiteBatchCreateReq) {
        return sensitiveBiz.batchCreateSensitiveRuleWhite(sensitiveRuleWhiteBatchCreateReq);
    }

    @Override
    public PageListQueryResult<SensitiveRule> querySensitiveRuleList(@RequestBody SensitiveRuleQueryModel sensitiveRuleQueryModel) {
        return sensitiveBiz.querySensitiveRuleList(sensitiveRuleQueryModel);
    }
}
