package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类名称: MaintenanceStatistAnalysisMapper
 * 类描述: 维保统计分析DAO层
 * 创建人: luowenbo
 * 创建时间: 2019/11/15
 * 版本: v1.0
 */
@Mapper
public interface MaintenanceStatistAnalysisMapper {

    // 维保统计分析第一层接口
    List<Map<String,Object>> firstLayer(MaintenStatistAnalysisRequest request);
    // 查询距离当前最近的日期
    Map<String,Object> getCloserDate(Map<String,String> request);
    // 维保统计分析第二层接口
    List<Map<String,Object>> secondLayer(MaintenStatistAnalysisRequest request);
    // 维保统计分析第三层接口
    List<Map<String,Object>> thirdLayer(MaintenStatistAnalysisRequest request);
    // 维保统计分析第四层接口
    List<Map<String,Object>> fourthLayer(MaintenStatistAnalysisRequest request);
    // 维保周期统计分析
    List<Map<String,Object>> maintenancePeriodAnalysis(MaintenStatistAnalysisRequest request);
    // 查询属于同期的合同中，上一期的合同
    List<Map<String,Object>> getCloserMaintenPeriod(@Param("quarterFlag") String quarterFlag,@Param("serviceEndTime") String serviceEndTime);
    // 维保周期依据名称查询
    List<Map<String,Object>> getMaintenPeriodByProjectName(MaintenStatistAnalysisRequest request);
}
