package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.screen;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;

/**
 * @projectName: ICmdbScreenProblemInfoClient
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-07-03 18:00
 **/
@FeignClient(value = "CMDB")
public interface ICmdbScreenProblemInfoClient {

    /*
     *  修改问题
     * */
    @PutMapping("/v3/cmdb/screen/problem/update")
    Map<String,Object> update(@RequestBody CmdbScreenProblemInfo req);

    /*
     *  添加问题
     * */
    @PostMapping("/v3/cmdb/screen/problem/save")
    Map<String,Object> save(@RequestBody CmdbScreenProblemInfo req);

    /*
     *  查询问题列表
     * */
    @PostMapping("/v3/cmdb/screen/problem/list")
    Result<CmdbScreenProblemInfo> list(@RequestBody CmdbScreenProblemInfoRequest req);

    /*
     *  查询具体问题详情，包括评论列表
     * */
    @GetMapping("/v3/cmdb/screen/problem/list/{id}")
    CmdbScreenProblemInfo listDetail(@PathVariable("id") String id);
}
