package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.report.mapper.ReportMapper;
import com.aspire.ums.cmdb.report.playload.Report;
import com.aspire.ums.cmdb.report.playload.ReportReq;
import com.aspire.ums.cmdb.report.service.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.service.impl
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:48
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class ReportServiceImpl implements IReportService {
    @Autowired
    private ReportMapper reportMapper;
    
    @Override
    public List<Report> listReportByBizSystem(String bizSystem, String idcType, String department1, String department2) {
        ReportReq request = new ReportReq();
        request.setBizSystem(bizSystem);
        request.setIdcType(idcType);
        request.setDepartment1(department1);
        request.setDepartment2(department2);
        return reportMapper.listReportByBizSystem(request);
    }
    
    @Override
    public List<Map<String, Object>> exportReportByBizSystem(String bizSystem, String idcType, String department1, String department2) {
        return reportMapper.exportReportByBizSystem(bizSystem,idcType,department1,department2);
    }

    @Override
    public List<Report> listReportByDepartment(String idcType, String department1, String department2) {
        ReportReq request = new ReportReq();
        request.setIdcType(idcType);
        request.setDepartment1(department1);
        request.setDepartment2(department2);
        return reportMapper.listReportByDepartment(request);
    }

    @Override
    public List<Map<String, Object>> exportReportByDepartment(String idcType, String department1, String department2) {
        return reportMapper.exportReportByDepartment(idcType, department1, department2);
    }

}
