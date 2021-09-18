package com.aspire.mirror.alert.server.dao.faultReport;

import com.aspire.mirror.alert.server.dao.faultReport.po.FaultReportManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FaultReportManageMapper {
    void insert(FaultReportManage manage);
    void update(FaultReportManage manage);
    FaultReportManage selectById(@Param("id") Integer id);
    List<FaultReportManage> selectByIds(@Param("ids")List<Integer> ids);
    List<FaultReportManage> pageList(Map<String, Object> map);
    Integer pageCount(Map<String, Object> map);
}
