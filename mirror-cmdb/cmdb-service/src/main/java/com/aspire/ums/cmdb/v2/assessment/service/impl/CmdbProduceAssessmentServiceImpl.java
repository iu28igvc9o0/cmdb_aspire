package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;
import com.aspire.ums.cmdb.v2.assessment.mapper.*;
import com.aspire.ums.cmdb.v2.assessment.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessmentServiceImpl
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Service
public class CmdbProduceAssessmentServiceImpl implements ICmdbProduceAssessmentService {

    @Autowired
    CmdbProduceAssessmentMapper produceAssessmentMapper;

    @Autowired
    ICmdbItDeviceCountService deviceCountService;

    @Autowired
    ICmdbProblemEventService problemEventService;

    @Autowired
    ICmdbDeviceRepairEventService deviceRepairEventService;

    @Autowired
    IEquipmentProblemService problemService;

    @Autowired
    private ICmdbServiceAssessService serviceAssessService;

    @Override
    @Transactional
    public void save(List<CmdbProduceAssessment> produceAssessments) {
        for (CmdbProduceAssessment assessment : produceAssessments) {
            if (StringUtils.isEmpty(assessment.getDeviceCountId())) {
                throw new RuntimeException("设备量id不能为空");
            }
            if (StringUtils.isEmpty(assessment.getMetricName())) {
                throw new RuntimeException("评估指标不能为空");
            }
            if (null == assessment.getScore()) {
                throw new RuntimeException("评分不能为空");
            }
            if (StringUtils.isEmpty(assessment.getScoreDescription())) {
                throw new RuntimeException("评分说明不能为空");
            }
            if (StringUtils.isEmpty(assessment.getId())) {
                assessment.setId(UUIDUtil.getUUID());
            }
        }
        if (produceAssessments.size() > 0) {
            produceAssessmentMapper.insertByBatch(produceAssessments);
        }
    }

    @Override
    public List<CmdbProduceAssessment> listByCountIds(List<String> countIds) {
       return produceAssessmentMapper.listByCountIds(countIds);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void approval(Integer status, String province, String quarter) {
        CmdbItDeviceCount deviceCount = new CmdbItDeviceCount();
        CmdbDeviceRepairEvent repairEvent = new CmdbDeviceRepairEvent();
        EquipmentProblem equipmentProblem = new EquipmentProblem();
        // 审批it数量
        deviceCount.setProvince(province);
        deviceCount.setStatus(status);
        deviceCount.setQuarter(quarter);
        deviceCountService.update(deviceCount);
        // 审批设备维修事件
        repairEvent.setStatus(status);
        repairEvent.setProvince(province);
        repairEvent.setQuarter(quarter);
        deviceRepairEventService.update(repairEvent);
        // 审批故障问题
        equipmentProblem.setProvince(province);
        equipmentProblem.setAssessStatus(status.toString());
        equipmentProblem.setQuarter(quarter);
        problemService.approveEquipmentProblem(equipmentProblem);
        // 审批问题事件
        problemEventService.updateStatus(province, status, quarter);
    }

    @Override
    public List<CmdbProduceAssessment> listByEntity(CmdbProduceAssessment entity) {
        return produceAssessmentMapper.listByEntity(entity);
    }
}
