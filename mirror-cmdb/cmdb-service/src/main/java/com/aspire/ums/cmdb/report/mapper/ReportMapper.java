package com.aspire.ums.cmdb.report.mapper;


import com.aspire.ums.cmdb.report.playload.Report;
import com.aspire.ums.cmdb.report.playload.ReportReq;
import com.aspire.ums.cmdb.systemManager.payload.Concat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:49
 * 版本: v1.0
 */
@Mapper
public interface ReportMapper {
   
   List<Report> listReportByBizSystem(ReportReq request);
    
   List<Map<String,Object>> exportReportByBizSystem(@Param("bizSystem") String bizSystem,
                                              @Param("idcType") String idcType,
                                              @Param("department1") String department1,
                                              @Param("department2") String department2);

    List<Report> listReportByDepartment(ReportReq request);

    List<Map<String,Object>> exportReportByDepartment(@Param("idcType") String idcType,
                                                     @Param("department1") String department1,
                                                     @Param("department2") String department2);
}
