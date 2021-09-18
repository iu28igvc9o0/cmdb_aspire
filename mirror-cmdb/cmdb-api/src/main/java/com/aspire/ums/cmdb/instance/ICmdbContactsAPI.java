package com.aspire.ums.cmdb.instance;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 查询接口人信息.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:01
 */
@Api(value = "CMDB IP地址库 接口")
@RequestMapping("/cmdb/addressLibrary")
public interface ICmdbContactsAPI {

    @ApiOperation(value = "获取接口人信息", notes = "获取接口人信息", tags = {"CMDB Address Library API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    @PostMapping(value = "/findContactsInfo")
    ResultVo<CmdbContactsResponse> findContactsInfo(@RequestBody Map<String, Object> param);

    @ApiOperation(value = "分配/取消分配操作", notes = "分配/取消分配操作", tags = {"CMDB Address Library API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    @PostMapping(value = "/allocation")
    ResultVo<CmdbContactsResponse> allocation(@RequestBody Map<String, Object> param);

}
