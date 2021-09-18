package com.aspire.ums.cmdb.report.web;

import com.aspire.ums.cmdb.report.IProvinceReportAPI;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceInsertVO;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportReq;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.web
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:40
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class ProvinceReportController implements IProvinceReportAPI {

    @Autowired
    private ICmdb31ProvinceReportSettingService settingService;
    @Autowired
    private ICmdb31ProvinceReportService reportService;
    @Autowired
    private ICmdb31ProvinceReportTableService reportTableService;

    @Override
    public Map<String, Object> getSetting(@RequestBody Map<String, String> queryParams) {
        return reportTableService.getTitlesByParams(queryParams);
    }

    @Override
    public Cmdb31ProvinceTable getTableByName(@RequestParam("tableName")String tableName,
                                              @RequestParam(value = "showPage", required = false) String showPage) {
        return reportTableService.getByName(tableName, showPage);
    }


    //    @Override
//    public List<Cmdb31ProvinceReportSetting> getSettingByQuery(@RequestBody Map<String, String> queryParams) {
//        return settingService.getSettingByQuery(queryParams);
//    }
    @Override
    public Map<String, String> save(@RequestParam("userName") String userName,@RequestBody Map<String,Object> insertVOListMap) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            reportService.save(userName, insertVOListMap);
            returnMap.put("success", "true");
            returnMap.put("message", "保存成功");
        } catch (Exception e) {
            returnMap.put("success", "false");
            returnMap.put("message", "保存失败，error:" + e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Map<String, List<Map<String, Object>>> getSettingData(@RequestBody Cmdb31ProvinceReportReq req) {
        return reportService.getSettingData(req);
    }

    @Override
    public Map<String, String> submitCheck(@RequestBody Map<String, String> queryParams) {
        return reportService.submitCheck(queryParams);
    }

    @Override
    public Map<String, String> submitApprove(@RequestBody Map<String, String> queryParams) {
        return reportService.submitApprove(queryParams);
    }

    @Override
    public List<Map<String, String>> getProvinceStatus(@RequestBody Map<String, String> queryParam) {
        return reportService.getProvinceStatus(queryParam);
    }
}
