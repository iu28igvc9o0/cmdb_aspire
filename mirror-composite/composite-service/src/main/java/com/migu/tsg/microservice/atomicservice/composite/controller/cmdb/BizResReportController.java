package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.IBizResReportService;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.BizResReportClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:48
 * 版本: v1.0
 */
@RestController
@Slf4j
public class BizResReportController implements IBizResReportService {
    
    @Autowired
    private BizResReportClient reportClient;
    
    @Override
    public Result<BizResReport> getAllBizResReportData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                       @RequestParam(value = "pageSize",required = false) int pageSize,
                                                       @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                       @RequestParam(value = "idcType",required = false) String idcType,
                                                       @RequestParam(value = "department1",required = false) String department1,
                                                       @RequestParam(value = "department2",required = false) String department2,
                                                       @RequestParam(value = "deviceType", required = false) String deviceType,
                                                       @RequestParam(value = "createTime1", required = false) String createTime1,
                                                       @RequestParam(value = "createTime2", required = false) String createTime2) {
        Result<BizResReport> rs = reportClient.getAllBizResReportData(pageNum,pageSize,bizSystem,idcType,department1,department2,deviceType,createTime1,createTime2);
        return rs;
    }
    
    @Override
    public void exportBizRes(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                             @RequestParam(value = "idcType",required = false) String idcType,
                             @RequestParam(value = "department1",required = false) String department1,
                             @RequestParam(value = "department2",required = false) String department2,
                             @RequestParam(value = "deviceType", required = false) String deviceType,
                             @RequestParam(value = "createTime1", required = false) String createTime1,
                             @RequestParam(value = "createTime2", required = false) String createTime2) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
            List<Map<String, Object>> reportData = reportClient.exportBizRes(bizSystem,idcType,department1,department2,deviceType,createTime1,createTime2);
            log.info("[cdmb Report Data] >>> {}", reportData );
            String[] headerList = {"资源池名称","业务系统名称","归属部门（一级）","归属部门（二级）","POD名称","已分配物理计算资源(台)","已分配虚拟计算资源（台）","交付周期（天）","交付比例（%）","日期"};
            String[] keyList = {"idcType","bizSystem","department1","department2","pod_name","allocatedPhysical","allocatedVM","delivery_cycle","delivery_ratio","createTime"};
            String title = "业务资源分配";
            if (StringUtils.isEmpty(deviceType)) {
                title = "业务资源分配";
            } else if ("物理机".equals(deviceType)) {
                title = "业务物理资源分配";
                headerList = new String[]{"业务系统名称","归属部门（一级）","归属部门（二级）","所属资源池","POD名称","计划申请总量(台)","已分配设备总量(台)","交付周期（天）","交付比例（%）","日期"};
                keyList = new String[]{"bizSystem","department1","department2","idcType","pod_name","total_planned_application","total_allocated_equipment","delivery_cycle","delivery_ratio","createTime"};
            } else if ("虚拟机".equals(deviceType)) {
                title = "业务虚拟资源分配";
                headerList = new String[]{"业务系统名称","归属部门（一级）","归属部门（二级）","所属资源池","POD名称","计划申请总量(台)","已分配设备总量(台)","虚拟核数vCPU（个）","总内存（G）","交付周期（天）","交付比例（%）","日期"};
                keyList = new String[]{"bizSystem","department1","department2","idcType","pod_name","total_planned_application","total_allocated_equipment","vcpu","total_memory","delivery_cycle","delivery_ratio","createTime"};
            } else {
                title = "业务资源模板";
                headerList = new String[]{"业务系统名称","归属部门（一级）","归属部门（二级）","设备分类","设备类型","所属资源池","POD名称","计划申请总量(台)","已分配设备总量(台)","虚拟核数vCPU（个）","总内存（G）","交付周期（天）","交付比例（%）","日期"};
                keyList = new String[]{"bizSystem","department1","department2","device_class","device_type","idcType","pod_name","total_planned_application","total_allocated_equipment","vcpu","total_memory","delivery_cycle","delivery_ratio","createTime"};
            }
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, reportData, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("[export reportData is error] >>> " + e);
        }
    }
    
}
