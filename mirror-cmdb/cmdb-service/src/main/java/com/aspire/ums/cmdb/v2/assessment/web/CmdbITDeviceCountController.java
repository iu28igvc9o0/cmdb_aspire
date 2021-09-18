package com.aspire.ums.cmdb.v2.assessment.web;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.IITDeviceCountAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbItDeviceCountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbITDeviceCountController
 * Author:   hangfang
 * Date:     2019/6/25 9:49
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbITDeviceCountController implements IITDeviceCountAPI {

    @Autowired
    ICmdbItDeviceCountService deviceCountService;


    /**
     * 查询所有设备量信息
     * @return 设备量
     */
    @Override
    public List<CmdbItDeviceCount> list(@RequestBody CmdbItDeviceCount deviceCount) {
        List<CmdbItDeviceCount> result = new ArrayList<>();
        try {
            log.info("Request CmdbITDeviceCountController.list");
            result = deviceCountService.listByEntity(deviceCount);
        } catch (Exception e) {
            log.info("list CmdbITDeviceCount error. Cause: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 存储设备量信息
     * @return 设备量
     */
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbItDeviceCount> deviceCounts) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbITDeviceCountController.save");
            deviceCountService.insert(deviceCounts);
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.info("save CmdbITDeviceCount error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "新增失败，原因:" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> delete(@RequestBody List<String> produces) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbITDeviceCountController.delete");
            if (produces.size() == 0) {
                throw new RuntimeException("删除厂家不能为空");
            }
            CmdbItDeviceCount deviceCount = new CmdbItDeviceCount();
            for (String produce : produces) {
                deviceCount.setProduce(produce);
                deviceCountService.delete(deviceCount);
            }
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            log.info("save CmdbITDeviceCount error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "删除失败，原因:" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getProvinceStatus(@RequestParam("quarter") String quarter) {
        return deviceCountService.getProvinceStatus(quarter);
    }

    @Override
    public List<Map<String, Object>> getProduceAndDeviceList(@RequestBody Map<String, String> requestMp) {
        return deviceCountService.getProduceAndDeviceList(requestMp);
    }
}
