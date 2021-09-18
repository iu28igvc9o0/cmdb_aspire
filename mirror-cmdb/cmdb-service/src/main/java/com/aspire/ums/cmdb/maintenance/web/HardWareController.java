package com.aspire.ums.cmdb.maintenance.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.IHardWareAPI;
import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;
import com.aspire.ums.cmdb.maintenance.service.IHardWareService;
import com.aspire.ums.cmdb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称: HardWareController
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/30 15:51
 * 版本: v1.0
 */
@RestController
@Slf4j
public class HardWareController implements IHardWareAPI {

    @Autowired
    private IHardWareService hardWareService;

    @Override
    public Map<String, Object> updateHardware(@RequestBody Hardware hardware) {
        log.info("HardWare updateHardware() param is {} ",hardware);
        return hardWareService.update(hardware);
    }

    @Override
    public Map<String, Object> batchUpdateHardware(@RequestBody Hardware hardwareList) {
        log.info("HardWare batchUpdateHardware() param is {} ",hardwareList);
        return hardWareService.batchUpdate(hardwareList);
    }

    @Override
    public Result<Hardware> selectHardwareByPage(@RequestBody HardwareRequest request) {
        log.info("HardWare selectHardwareByPage() param is {} ",request);
        Result<Hardware> rs = hardWareService.selectHardWareByPage(request);
        return rs;
    }

    @Override
    public List<Hardware> getHardwareList(@RequestBody HardwareRequest request) {
        log.info("HardWare getHardwareList() param is {} ",request);
        return null;
    }

    @Override
    public List<Map<String, Object>> export(@RequestBody HardwareRequest sendRequest) {
        try {
            log.info("Request -> /cmdb/maintenHardWare/exprot data -> {}", JsonUtil.toJacksonJson(sendRequest));
            return hardWareService.queryExportData(sendRequest);
        } catch (Exception e) {
            log.error("Query cmdb maintenHardWare exprot error. {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, Object> queryIsExist(@RequestParam("deviceSerialNumber") String deviceSerialNumber, @RequestParam("warrantyDate") String warrantyDate) {
        log.info("HardWare getHardwareList() param is {} ",deviceSerialNumber,warrantyDate);
        return hardWareService.queryIsExist(deviceSerialNumber,warrantyDate);
    }

    @Override
    public Map<String, Object> queryInfoByNameAndDeviceSn(@RequestParam("projectName") String projectName,@RequestParam("deviceSn") String deviceSn) {
        log.info("HardWare queryInfoByNameAndDeviceSn() param is {} ",projectName,deviceSn);
        return hardWareService.queryInfoByNameAndDeviceSn(projectName,deviceSn);
    }
}
