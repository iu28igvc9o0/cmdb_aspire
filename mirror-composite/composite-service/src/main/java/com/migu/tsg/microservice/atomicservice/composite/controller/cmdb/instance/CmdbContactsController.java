package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.composite.service.cmdb.instance.ICmdbContactsAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.CmdbContactsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 18:18
 */
@Slf4j
@RestController
public class CmdbContactsController implements ICmdbContactsAPI {
    @Autowired
    private CmdbContactsClient cmdbContactsClient;

    @Override
    public ResultVo<CmdbContactsResponse> findContactsInfo(@RequestParam(value = "instanceId") String instanceId,
                                                           @RequestParam(value = "moduleId") String moduleId) {
        log.info("CmdbContactsController.findContactsInfo instanceId={}，moduleId={}", instanceId, moduleId);
        return cmdbContactsClient.findContactsInfo(new HashMap<String, Object>() {{
            put("instanceId", instanceId);
            put("moduleId", moduleId);
        }});
    }

    @Override
    public ResultVo<CmdbContactsResponse> allocation(@RequestParam(value = "instanceId") String instanceId,
                                                     @RequestParam(value = "assignStatus") String assignStatus,
                                                     @RequestParam(value = "namespace") String namespace,
                                                     @RequestParam(value = "moduleId") String moduleId) {
        log.info("CmdbContactsController.allocation instanceId={}，assignStatus={}，assignStatus={}，moduleId={}", instanceId, assignStatus, namespace, moduleId);
        String format = DateUtil.format(new Date(), DateUtil.DATE_TIME_CH_FORMAT);
        HashMap<String, Object> hashMap = new HashMap<String, Object>() {{
            put("instance_id", instanceId);
            put("module_id", moduleId);
            put("assign_status", assignStatus);
            put("operator", namespace);
        }};
        if ("已分配".equals(assignStatus)) {
            hashMap.put("assign_user",namespace);
            hashMap.put("assign_time",format);
        } else {
            hashMap.put("assign_user","");
            hashMap.put("assign_time","");
        }
        return cmdbContactsClient.allocation(hashMap);
    }
}
