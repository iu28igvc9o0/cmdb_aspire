package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.report.mapper.BizResourceMapper;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.aspire.ums.cmdb.report.playload.BizResReportReq;
import com.aspire.ums.cmdb.report.service.IBizResReportService;
import com.aspire.ums.cmdb.util.StringUtils;
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
public class BizResReportServiceImpl implements IBizResReportService {
    @Autowired
    private BizResourceMapper bizResourceMapper;
    
    @Override
    public Result<BizResReport> getAllReportBizResData(int pageNum,
                                                       int pageSize,
                                                       String bizSystem,
                                                       String idcType,
                                                       String department1,
                                                       String department2,
                                                       String deviceType,
                                                       String createTime1,
                                                       String createTime2) {
        BizResReportReq request = new BizResReportReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setStartPageNum();
        request.setBizSystem(bizSystem);
        request.setIdcType(idcType);
        request.setDepartment1(department1);
        request.setDepartment2(department2);
        request.setDeviceType(deviceType);
        request.setCreateTime1(createTime1);
        request.setCreateTime2(createTime2);
        
        Result<BizResReport> response = new Result<BizResReport>();
        if (StringUtils.isEmpty(deviceType)) {
            int dataCount = bizResourceMapper.getAllReportCount_total(request);
            response.setCount(dataCount);
            if (dataCount > 0) {
                List<BizResReport> dataList = bizResourceMapper.getAllReportData_total(request);
                response.setData(dataList);
            }
        } else {
            int dataCount = bizResourceMapper.getAllReportCount(request);
            response.setCount(dataCount);
            if (dataCount > 0) {
                List<BizResReport> dataList = bizResourceMapper.getAllReportData(request);
                response.setData(dataList);
            }
        }
        return response;
    }
    
    @Override
    public List<Map<String, Object>> getBizResExportData(String bizSystem,
                                                         String idcType,
                                                         String department1,
                                                         String department2,
                                                         String deviceType,
                                                         String createTime1,
                                                         String createTime2) {
        if (StringUtils.isEmpty(deviceType)) {
            List<Map<String, Object>> rs = bizResourceMapper.getExportData_total(bizSystem, idcType, department1, department2, deviceType, createTime1, createTime2);
            return rs;
        } else {
            List<Map<String, Object>> rs = bizResourceMapper.getExportData(bizSystem, idcType, department1, department2, deviceType, createTime1, createTime2);
            return rs;
        }
    }
    
    @Override
    public void insert(BizResReport bizResReport) {
        bizResourceMapper.insert(bizResReport);
    }
    
}
