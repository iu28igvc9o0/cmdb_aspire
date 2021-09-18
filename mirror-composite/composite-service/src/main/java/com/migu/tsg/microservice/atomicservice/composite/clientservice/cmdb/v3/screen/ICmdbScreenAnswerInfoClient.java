package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.screen;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;

/**
 * @projectName: CmdbScreenAnswerInfoClient
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-07-03 17:59
 **/
@FeignClient(value = "CMDB")
public interface ICmdbScreenAnswerInfoClient {
    /*
     *  添加回答
     * */
    @PostMapping("/v3/cmdb/screen/answer/save")
    Map<String,Object> save(@RequestBody CmdbScreenAnswerInfo req,
                            @RequestParam("isAdmin") Boolean isAdmin);
}
