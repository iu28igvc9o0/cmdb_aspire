package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.assessment.IDeviceRepairEventAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.common.Result;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbDeviceRepairEventClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    private CmdbDeviceRepairEventClient deviceRepairEventClient;


    /**
     * 查询所有设备维修事件
     * @return 设备量
     */
    @Override
    public Result<CmdbDeviceRepairEvent> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              @RequestParam(value = "page", required = false) String page,
                                              @RequestBody CmdbDeviceRepairEvent deviceRepairEvent) {
        Result<CmdbDeviceRepairEvent> result = deviceRepairEventClient.list(pageNum, pageSize, deviceRepairEvent);
        if (result.getTotalSize() > 0 && StringUtils.isNotEmpty(page) && "approve".equals(page)){
            Integer status = result.getData().get(0).getStatus();
            if (status == -1 || status == 1) {
                return result;
            } else {
                return new Result<>();
            }
        }
        return result;
    }

    /**
     * 存储设备维修事件
     */
    @Override
    public Map<String, Object> save(@RequestBody JSONObject data) {
        if (data.size() == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "没有要保存的数据");
            return result;
        }
        return deviceRepairEventClient.save(data);
    }

    @Override
    public Map<String, Object> delete(@PathVariable("id") String id) {
        return deviceRepairEventClient.delete(id);
    }
}
