package com.aspire.ums.cmdb.v2.assessment.web;

import com.aspire.ums.cmdb.assessment.IProduceAssessmentAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.aspire.ums.cmdb.v2.assessment.DeviceConst;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbProduceAssessmentService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbServiceAssessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessmentController
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@RefreshScope
@RestController
@Slf4j
public class CmdbProduceAssessmentController implements IProduceAssessmentAPI {

    @Autowired
    private ICmdbProduceAssessmentService produceAssessmentService;
    @Autowired
    private ICmdbServiceAssessService serviceAssessService;

    /**
     * 新增厂家设备评分
     */
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbProduceAssessment> produceAssessments) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbProduceAssessmentController.save Data: {}", produceAssessments);
            produceAssessmentService.save(produceAssessments);
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.info("save CmdbProduceAssessmentController error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "新增失败，原因:" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<CmdbProduceAssessment> listByCountIds(@RequestBody List<String> countIds) {
        return produceAssessmentService.listByCountIds(countIds);
    }

    @Override
    public Map<String, Object> approval(@RequestParam("status") Integer status,
                                        @RequestParam("province") String province,
                                        @RequestParam("quarter") String quarter) {
        Map<String, Object> result = new HashMap<>();
        if (!DeviceConst.APPROVESTATUS.contains(status)) {
            throw new RuntimeException(status + " 为非法状态! 状态（1审批通过，2保存中，0审批驳回，-1录入待审批）");
        }
        try {
            log.info("Request CmdbProduceAssessmentController.approval status: {} province: {}", status, province);
            produceAssessmentService.approval(status, province, quarter);
            result.put("success", true);
            if (status == -1) {
                result.put("message", "提交成功");
            } else {
                result.put("message", "审批成功");
            }
        } catch (Exception e) {
            log.info("save CmdbProduceAssessmentController error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            if (status == -1) {
                result.put("message", "提交失败，原因:" + e.getMessage());
            } else {
                result.put("message", "审批失败，原因:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 生成评分信息
     */
    public Map<String, Object> makeEvaluate(@RequestParam("deviceType") String deviceType,
                                     @RequestParam("quarter") String quarter) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbProduceAssessmentController.makeEvaluate deviceType: {} quarter: {}", deviceType, quarter);
            serviceAssessService.delete(deviceType, quarter);
            serviceAssessService.calcTotalDeviceCount(deviceType, quarter);
            serviceAssessService.calcRepairEvent(deviceType, quarter);
            serviceAssessService.calcProblemEvent(deviceType, quarter);
            serviceAssessService.calcEquipmentProblem(deviceType, quarter);
            serviceAssessService.calcHandlerAndRepairLong(deviceType, quarter);
            serviceAssessService.calcEvaluateScore(deviceType, quarter);
            result.put("flag", "success");
            log.info("生成评分信息结束.");
        } catch (Exception e) {
            log.info("Save evaluate error. Cause: {}", e.getMessage(), e);
            result.put("flag", "error");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> exportEvaluate(@RequestParam("deviceType") String deviceType,
                                              @RequestParam("quarter") String quarter) {
        return serviceAssessService.exportEvaluate(deviceType, quarter);
    }
}
