package com.aspire.ums.cmdb.v2.assessment.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.IDeviceRepairEventAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceRepairEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceRepairEventController
 * Author:   hangfang
 * Date:     2019/6/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbDeviceRepairEventController implements IDeviceRepairEventAPI {

    @Autowired
    private ICmdbDeviceRepairEventService deviceRepairEventService;

    /**
     * 查询所有设备维修事件
     * @return 设备量
     */
    @Override
    public Result<CmdbDeviceRepairEvent> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              @RequestBody CmdbDeviceRepairEvent deviceRepairEvent) {
        Result<CmdbDeviceRepairEvent> result = new Result<>();
        try {
            log.info("Request CmdbDeviceRepairEventController.list");
            Integer start = null;
            if (null != pageNum) {
                start = (pageNum - 1) * pageSize;
            }
            result.setData(deviceRepairEventService.listByEntity(start, pageSize, deviceRepairEvent));
            result.setTotalSize(deviceRepairEventService.listCount(deviceRepairEvent));
        } catch (Exception e) {
            log.info("list CmdbDeviceRepairEventController error. Cause: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 存储设备维修事件
     */
    @Override
    public Map<String, Object> save(@RequestBody JSONObject data) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbDeviceRepairEventController.list");
            deviceRepairEventService.insert(data);
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.info("save CmdbDeviceRepairEventController error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "新增失败");
        }
        return result;
    }

    /**
     * 删除设备维修事件
     */
    @Override
    public Map<String, Object> delete(@PathVariable("id") String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbDeviceRepairEventController.delete");
            CmdbDeviceRepairEvent deviceRepairEvent = new CmdbDeviceRepairEvent();
            deviceRepairEvent.setId(id);
            deviceRepairEventService.delete(deviceRepairEvent);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            log.info("save CmdbDeviceRepairEventController error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
}
