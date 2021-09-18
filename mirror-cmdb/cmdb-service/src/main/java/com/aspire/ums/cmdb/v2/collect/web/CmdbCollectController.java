package com.aspire.ums.cmdb.v2.collect.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.client.CmdbApprovalESClient;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.ICollectApprovalAPI;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectController
 * Author:   HANGFANG
 * Date:     2019/6/24 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbCollectController implements ICollectApprovalAPI {

    @Autowired
    CmdbCollectApprovalService approvalService;

    @Autowired
    CmdbInstanceMapper instanceMapper;
    @Autowired
    private CmdbApprovalESClient approvalESClient;

    @Override
    public List<Map<String, String>> getFiledNameList() {
        return approvalService.getFiledNameList();
    }


    @Override
    public List<Map<String, String>> getOperatorTypeList() {
        return approvalService.getOperatorTypeList();
    }

    @Override
    public List<CmdbSimpleCode> getApprovalHeaderCode(String moduleId) {
        return approvalService.getApprovalHeaderCode(moduleId);
    }

    @Override
    public Result<Map<String,Object>> list(@RequestBody CmdbCollectApprovalQuery approvalQuery) {
        Result<Map<String,Object>> result = new Result<>();
        try {
            log.info("Request CmdbCollectController.list  data -> {}", approvalQuery);
            if ("detail".equals(approvalQuery.getType())) {
                Map<String, Object> resultMap =  approvalESClient.query((Map<String, Object>)approvalQuery);
                result.setCount(Integer.parseInt(resultMap.get("count").toString()));
                result.setData(new ObjectMapper().convertValue(resultMap.get("data"), new TypeReference<List<Map<String, Object>>>(){}));
            } else {
                List<Map<String,Object>> collectApprovals =  approvalService.list(approvalQuery);
                result.setCount(approvalService.listCount(approvalQuery));
                result.setData(collectApprovals);
            }
        } catch (Exception e) {
            log.error("Query cmdb collect control error. Cause: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }


    @Override
    public Map<String, Object> insert(List<CmdbCollectApproval> approvals) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            approvalService.insertByBatch(approvals);
            resultMap.put("success", true);
            resultMap.put("message", "新增配置审核成功！");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "新增配置审核失败，error：" + e.getMessage());
        }
        return resultMap;
    }

}
