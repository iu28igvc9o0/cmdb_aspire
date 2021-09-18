package com.aspire.mirror.composite.service.cmdb.instance;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 查询接口人信息.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:01
 */
@Api(value = "CMDB IP地址库 接口")
@RequestMapping("${version}/cmdb/addressLibrary")
public interface ICmdbContactsAPI {

    @ApiOperation(value = "获取接口人信息", notes = "获取接口人信息", tags = {"CMDB Address Library API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    @PostMapping(value = "/findContactsInfo")
    ResultVo<CmdbContactsResponse> findContactsInfo(@RequestParam(value = "instanceId") String instanceId,
                                                    @RequestParam(value = "moduleId") String moduleId);

    @ApiOperation(value = "分配/取消分配操作", notes = "分配/取消分配操作", tags = {"CMDB Address Library API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    @PostMapping(value = "/allocation")
    ResultVo<CmdbContactsResponse> allocation(@RequestParam(value = "instanceId") String instanceId,
                                              @RequestParam(value = "assignStatus") String assignStatus,
                                              @RequestParam(value = "namespace") String namespace,
                                              @RequestParam(value = "moduleId") String moduleId);

}
