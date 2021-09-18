package com.aspire.ums.cmdb.code;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * cmdb资产同步表字段映射controller
 *
 * @author jiangxuwen
 * @date 2020/5/28 17:37
 */
@RequestMapping("/cmdb/sync/fieldMapping")
public interface ICmdbFieldMappingAPI {

    /**
     * 全量刷新redis
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ApiOperation(value = "刷新资产同步表字段映射的redis缓存", notes = "刷新资产同步表字段映射的redis缓存", tags = { "CMDB SYNC API" })
    @ApiResponses(value = { @ApiResponse(code = 204, message = "刷新成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, String> refresh(@RequestParam("mappingType") String mappingType, @RequestParam("mappingKey") String mappingKey);
}
