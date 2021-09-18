package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;

import java.util.List;
import java.util.Map;

public interface MaintenanceStatistAnalysisService {
    // 维保统计分析第一层接口
    List<Map<String,Object>> firstLayer(MaintenStatistAnalysisRequest request);
    // 维保统计分析第二层接口
    List<Map<String,Object>> secondLayer(MaintenStatistAnalysisRequest request);
    // 维保统计分析第三层接口
    List<Map<String,Object>> thirdLayer(MaintenStatistAnalysisRequest request);
    // 维保统计分析第四层接口
    List<Map<String,Object>> fourthLayer(MaintenStatistAnalysisRequest request);
    // 维保周期统计分析
    List<Map<String,Object>> maintenancePeriodAnalysis(MaintenStatistAnalysisRequest request);
}
