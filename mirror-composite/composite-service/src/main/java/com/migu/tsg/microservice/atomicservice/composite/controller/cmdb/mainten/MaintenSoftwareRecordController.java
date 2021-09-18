package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenSoftwareRecordAPI;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenSoftwareRecordClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process.conf.ImportTemplate;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/6
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@RestController
public class MaintenSoftwareRecordController implements IMaintenSoftwareRecordAPI {

    @Autowired
    private MaintenSoftwareRecordClient softwareRecordClient;
    @Autowired
    private ImportTemplate importTemplate;
    @Override
    public PageBean<CmdbMaintenanceSoftwareRecord> list(@RequestBody CmdbMaintenanceSoftwareRecordQuery query) {
        return softwareRecordClient.list(query);
    }

    @Override
    public Map<String, Object> saveAndUpdate(@RequestBody List<CmdbMaintenanceSoftwareRecord> records) {
        return softwareRecordClient.saveAndUpdate(records);
    }

    @Override
    public Map<String, Object> delete(@RequestBody JSONObject data) {
        return softwareRecordClient.delete(data);
    }

    @Override
    public Map<String, Object> downloadTemplate(HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String templateHeader = importTemplate.getMaintenance().getMaintensoftwarerecord();
        String[] headers = templateHeader.split(",");
        String fileName = "软件维保使用信息.xlsx";
        OutputStream os = null;// 取得输出流
        try {
            os = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, fileName, headers, new ArrayList<>(), new String[]{});
            book.write(os);
            os.flush();
            os.close();
            result.put("success", true);
            result.put("message", "导出模板成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", true);
            result.put("message", "导出模板失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> exportData(@RequestBody CmdbMaintenanceSoftwareRecordQuery recordQuery, HttpServletResponse response) {
        log.info("CmdbMaintenanceSoftwareRecordQuery is {} ",recordQuery);

        PageBean<CmdbMaintenanceSoftwareRecord> softwareRecords = softwareRecordClient.list(recordQuery);
        String[] headerList =  {"项目","分类","软件名称","服务厂家","服务人员","服务级别",
                "开始时间", "结束时间","处理时长","实际人天","移动审批人","运维审批人","创建人"};
        String[] keyList = {"project","classify","softwareName","company","serverPerson","serverLevel",
                "serverStartTime","serverEndTime",
                "handleLong","realDays","yidongApprover","devopsApprover","createUser" };
        String title = "软件维保使用信息";
        String fileName = title+".xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CmdbMaintenanceSoftwareRecord record : softwareRecords.getResult()) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(record);

                if(record.getServerStartTime()!=null){
                    map.put("serverStartTime",  sdf.format(record.getServerStartTime()));
                }
                if(record.getServerEndTime()!=null){
                    map.put("serverEndTime",  sdf.format(record.getServerEndTime()));
                }
                dataLists.add(map);
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        }
        return null;
    }

}
