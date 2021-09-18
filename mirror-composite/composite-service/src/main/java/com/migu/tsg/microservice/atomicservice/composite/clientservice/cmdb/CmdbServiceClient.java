package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;

/**
 * cmdb客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:    CmdbServiceClient.java
 * 类描述:    cmdb客户端
 * 创建人:    JinSu
 * 创建时间:  2018/9/4 10:28
 * 版本:      v1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbServiceClient {
    /**
     * . <br/>
     *
     * @param devicePageRequest
     * @return
     */
//    @GetMapping(value = "/cmdb/repertryInstance/getInstanceBaseInfoList", produces = MediaType
//            .APPLICATION_JSON_UTF8_VALUE)
//    CompDeviceListResponse listDeviceDetails(@RequestBody CompDevicePageRequest devicePageRequest);

    @RequestMapping(value = "/cmdb/instance/list", method = RequestMethod.POST)
    Result<Map<String, Object>> listDeviceDetails(@RequestBody CmdbQueryInstance queryInstance);

    /**
     *
     */
    @GetMapping(value = "/cmdb/circle/getModuleTree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<Map> getModuleTree();

    /**
     *
     */
    @PostMapping(value = "/cmdb/form/getForms")
    Map<String, Object> getForms(@RequestBody Map request);

//    @GetMapping(value = "/cmdb/repertryInstance/getInstanceByIds/{instanceIds}")
//    List<Map> getInstanceByIds(@PathVariable("instanceIds") String instanceIds);

    @RequestMapping(value = "/cmdb/instance/listInstanceBaseInfo/{device_ids}", method = RequestMethod.GET)
    List<CmdbInstance> getInstanceByIds(@PathVariable("device_ids") String deviceIds);

    @PostMapping(value = "/cmdb/repertryInstance/getAuthDeviceData")
    Map<String, Object> getAuthDeviceData(@RequestBody Map request);
}
