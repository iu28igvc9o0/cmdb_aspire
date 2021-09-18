package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.config;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbConfigAPI
 * Author:   zhu.juwang
 * Date:     2020/5/6 11:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbConfigClient {

    /**
     * 获取根模型分组列表
     * @param
     * @return
     */
    @GetMapping("/v3/cmdb/module/config/getConfigByCode")
    @ApiOperation(value = "根据配置编码获取配置明细", notes = "根据配置编码获取配置明细", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbConfig getConfigByCode(@RequestParam("configCode") String configCode);

    /*
     *  新增CMDB配置编码
     * */
    @PostMapping("/v3/cmdb/module/config/save")
    Map<String, Object> save(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  编辑CMDB配置编码
     * */
    @PutMapping("/v3/cmdb/module/config/update")
    Map<String, Object> update(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  删除CMDB配置编码
     * */
    @DeleteMapping("/v3/cmdb/module/config/delete")
    Map<String, Object> delete(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  分页查询列表数据
     * */
    @PostMapping("/v3/cmdb/module/config/list")
    Result<CmdbConfig> list(@RequestBody CmdbConfigRequest req);

    /*
     *  依据编码查询唯一
     * */
    @PostMapping("/v3/cmdb/module/config/getOne")
    Map<String,Object> getOne(@RequestParam("configCode") String configCode);

}
