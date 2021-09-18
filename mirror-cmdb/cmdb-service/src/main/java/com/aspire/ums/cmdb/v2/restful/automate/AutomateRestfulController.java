package com.aspire.ums.cmdb.v2.restful.automate;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.restful.automate.IAutomateRestfulAPI;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutomateRestfulController
 * Author:   zhu.juwang
 * Date:     2019/9/11 11:00
 * Description: 该接口类用来专门提供接口给自动化运维工具使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class AutomateRestfulController implements IAutomateRestfulAPI {
    @Autowired
    private ICmdbInstanceService instanceService;
    @Override
    public List<CmdbInstance> getInstanceList(@RequestBody Map<String, Object> params) {
        log.info("Request AutomateRestfulController.getInstanceList params -> {}", JSONObject.toJSON(params).toString());
        if (!params.containsKey("device_type")) {
            throw new RuntimeException("缺少设备类型[device_type]参数");
        }
        if (!params.containsKey("idcType")) {
            throw new RuntimeException("缺少资源池[idcType]参数");
        }
        CmdbInstance queryInstance = new CmdbInstance();
        queryInstance.setDeviceType(params.get("device_type").toString());
        queryInstance.setIdcType(params.get("idcType").toString());
        return instanceService.listByEntity(queryInstance);
    }
}
