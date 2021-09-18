package com.aspire.ums.cmdb.v2.assessment.service;

import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.service
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 15:28
 * 版本: v1.0
 */
public interface ICmdbServiceAssessService {
    
    void delete (String deviceType, String quarter);

    void calcTotalDeviceCount(String deviceType, String quarter);

    void calcRepairEvent(String deviceType, String quarter);

    void calcProblemEvent(String deviceType, String quarter);

    void calcEquipmentProblem(String deviceType, String quarter);

    void calcHandlerAndRepairLong(String deviceType, String quarter);

    void calcEvaluateScore(String deviceType, String quarter);

    List<CmdbServiceAssess> queryScoreList(String deviceType, String quarter);
    
    Map<String,Object> saveScore (List<CmdbServiceAssess> list);

    List<Map<String, Object>> getScoreDeviceTypeStatus(String quarter);

    /**
     * 导出评分信息
     * @param deviceType
     * @param quarter
     * @return
     */
    Map<String, Object> exportEvaluate(String deviceType, String quarter);
}
