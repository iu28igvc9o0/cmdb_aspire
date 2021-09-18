package com.aspire.ums.cmdb.v2.assessment.web;

import com.aspire.ums.cmdb.assessment.IDeviceProduceAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceProduceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceProduceController
 * Author:   hangfang
 * Date:     2019/6/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbDeviceProduceController implements IDeviceProduceAPI {

    @Autowired
    private ICmdbDeviceProduceService deviceProduceService;

    /**
     * 获取所有厂家
     * @return 返回所有厂家
     */
    @Override
    public List<CmdbDeviceProduce> list() {
        return deviceProduceService.list();
    }

    /**
     * 新增厂家
     * @return 返回
     */
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbDeviceProduce> deviceProduces) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request CmdbDeviceProduceController.save Data: {}", deviceProduces);
            deviceProduceService.insertByBatch(deviceProduces);
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.info("save CmdbDeviceProduceController error. Cause: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "新增失败");
        }
        return result;
    }
}
