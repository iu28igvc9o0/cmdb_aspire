package com.aspire.ums.cmdb.maintenance.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.IHardWareUseAPI;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;
import com.aspire.ums.cmdb.maintenance.service.IHardWareUseService;
import com.aspire.ums.cmdb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 类名称: HardWareUseController
 * 类描述: 硬件维保使用Controller
 * 创建人: PJX
 * 创建时间: 2019/8/4 13:16
 * 版本: v1.0
 */
@RestController
@Slf4j
public class HardWareUseController implements IHardWareUseAPI {

    @Autowired
    private IHardWareUseService hardWareUseService;

    @Override
    public Map<String, Object> addHardwareUse(@RequestBody HardwareUse hardwareUse) {
        return hardWareUseService.addHardwareUse(hardwareUse);
    }

    @Override
    public Map<String, Object> batchAddHardwareUse(@RequestBody List<HardwareUse> list) {
        return hardWareUseService.batchInsertHardWareUse(list);
    }

    @Override
    public Result<HardwareUse> selectHardwareUseByPage(@RequestBody HardwareUseRequest request) {
        return hardWareUseService.selectHardWareUseByPage(request);
    }

    @Override
    public Map<String, Object> updateHardwareUse(@RequestBody HardwareUse hardwareUse) {
        return hardWareUseService.update(hardwareUse);
    }

    @Override
    public Map<String, Object> batchUpdateHardwareUse(@RequestBody HardwareUse hardwareUseList) {
        return hardWareUseService.batchUpdate(hardwareUseList);
    }

    @Override
    public Map<String, Object> deleteHardwareUse(@RequestParam("id") String id) {
        return hardWareUseService.deleteHardwareUse(id);
    }

    @Override
    public List<Map<String, String>> getHardwareTableList() {
        List<Map<String, String>> rs = hardWareUseService.getHardwareTableList();
        return rs;
    }

    @Override
    public JSONObject export(@RequestBody Map<String, Object> sendRequest) {
        try {
            log.info("Request -> /cmdb/maintenHardWareUse/exprot data -> {}", JsonUtil.toJacksonJson(sendRequest));
            JSONObject returnJson = hardWareUseService.queryExportData(sendRequest);
            return returnJson;
        } catch (Exception e) {
            log.error("Query cmdb maintenHardWareUse exprot error. {}", e.getMessage(), e);
        }
        return null;
    }
}
