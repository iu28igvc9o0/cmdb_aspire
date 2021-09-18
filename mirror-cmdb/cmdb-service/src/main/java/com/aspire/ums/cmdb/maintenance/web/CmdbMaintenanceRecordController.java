package com.aspire.ums.cmdb.maintenance.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.IMaintenSoftwareRecordAPI;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceSoftwareRecordService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/4
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbMaintenanceRecordController implements IMaintenSoftwareRecordAPI {

    @Autowired
    private ICmdbMaintenanceSoftwareRecordService softwareRecordService;

    @Override
    public PageBean<CmdbMaintenanceSoftwareRecord> list(@RequestBody CmdbMaintenanceSoftwareRecordQuery query) {
        PageBean<CmdbMaintenanceSoftwareRecord> result = new PageBean<>();
        List<CmdbMaintenanceSoftwareRecord> records = softwareRecordService.listByEntity(query);
        int total = softwareRecordService.listCount(query);
        result.setCount(total);
        result.setResult(records);
        return result;
    }

    @Override
    public Map<String, Object> saveAndUpdate(@RequestBody List<CmdbMaintenanceSoftwareRecord> records) {
        log.info("Request into CmdbMaintenanceRecordController.saveAndUpdate()  params -> {}", records);
        Map<String, Object> result = new HashMap<>();
        boolean flag = false;
        if (records.size() == 0) {
            return result;
        }
        try {
            if (StringUtils.isNotEmpty(records.get(0).getId())) {
                flag = true;
            }
            softwareRecordService.insert(records);
            if (flag) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", true);
                result.put("message", "新增成功");
            }
        } catch (Exception e) {
            if (flag) {
                result.put("success", false);
                result.put("message", "更新失败");
            } else {
                result.put("success", false);
                result.put("message", "新增失败");
            }
            log.error("saveAndUpdate software error.", e);
        }
        return result;
    }

    @Override
    public Map<String, Object> delete(@RequestBody JSONObject data) {
        List<String> deleteIds = JSON.parseArray(data.getJSONArray("deleteIds").toJSONString(), String.class);
        log.info("Request into CmdbMaintenanceRecordController.delete()  params -> {}", deleteIds);
        Map<String, Object> result = new HashMap<>();
        try {
            softwareRecordService.delete(deleteIds);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
}
