package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.aspire.mirror.composite.service.cmdb.assessment.IDeviceProduceAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbDeviceProduceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    private CmdbDeviceProduceClient deviceProduceClient;
    /**
     * 查询所有厂家信息
     * @return 所有厂家信息
     */
    @Override
    public List<CmdbDeviceProduce> list() {
        return deviceProduceClient.list();
    }

    /**
     * 存储厂家信息
     */
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbDeviceProduce> deviceProduces) {
        return deviceProduceClient.save(deviceProduces);
    }
}
