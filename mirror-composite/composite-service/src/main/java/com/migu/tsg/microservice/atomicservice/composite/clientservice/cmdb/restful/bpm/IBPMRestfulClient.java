package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.bpm;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "CMDB")
public interface IBPMRestfulClient {
    /**
     * 对接BPM资源申请流程
     * @param resourceInfo 申请资源信息
     *  {
     *    "bizSystem": "业务系统",
     *    "idcType": "资源池名称",
     *    "data": [{
     *          "type": "资源类型",
     *          "num": "申请数量",
     *          "cpu": "云主机CPU数量",
     *          "memory": "云主机内存数量"
     *       }]
     *  }
     */
    @RequestMapping(value = "/cmdb/restful/bpm/resource/request", method = RequestMethod.POST)
    Map<String, Object> resourceRequestProcess(@RequestBody Map<String, Object> resourceInfo);

    @RequestMapping(value = "/cmdb/restful/bpm/resource/syncOrgSystem", method = RequestMethod.POST)
    Map<String, Object> syncOrgSystem(@RequestBody Map<String, Object> orgManagerData);
}
