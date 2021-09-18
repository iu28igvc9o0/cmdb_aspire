package com.aspire.mirror.theme.server.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.theme.api.dto.CmdbInstance;

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
@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbServiceClient {

    @PostMapping(value = "/v1/cmdb/instance/queryDeviceByIdcTypeAndIP")
    CmdbInstance queryDeviceByRoomIdAndIP(@RequestBody Map<String, Object> params);

    /**
     * 根据模型ID, 查询模型数据
     * @param queryParams 模型入参配置
     * @param moduleId 模型标识
     * @param moduleType 模型类型
     */
    @RequestMapping(value = "/module/data", method = RequestMethod.POST)
    List<Map<String, Object>> getModuleData(@RequestBody Map<String, Object> queryParams,
                                            @RequestParam(value = "moduleId", required = false) String moduleId,
                                            @RequestParam(value = "moduleType", required = false) String moduleType);
    //    /**
    //     *
    //     */
    //    @GetMapping(value = "/cmdb/circle/getModuleTree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //    List<Map> getModuleTree();
    //
    //    /**
    //     *
    //     */
    //    @PostMapping(value = "/cmdb/form/getForms")
    //    Map<String, Object> getForms(@RequestBody Map request);
    //
    //    @GetMapping(value = "/cmdb/repertryInstance/getInstanceByIds/{instanceIds}")
    //    List<Map> getInstanceByIds(@PathVariable("instanceIds") String instanceIds);
}
