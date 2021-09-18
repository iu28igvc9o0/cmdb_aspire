package com.aspire.ums.bills.discount;

import com.aspire.ums.bills.discount.payload.CmdbBillsDiscountRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 09:46
 */
@Api(value = "账单折扣管理接口类")
@RequestMapping("/v1/cmdb/bill/discount")
public interface CmdbBillsDiscountAPI {

    @GetMapping(value = "/loadDeptTreeData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取租户折扣树", notes = "获取租户折扣树",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getDeptTreeData();

    @GetMapping(value = "/loadPoolTreeData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取资源池折扣树", notes = "获取租户折扣树",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getPoolTreeData();

    @GetMapping(value = "/loadResourceTreeData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取资源折扣树", notes = "获取租户折扣树",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getResourceTreeData();

    @GetMapping(value = "/loadBusinessList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取业务系统列表", notes = "获取业务系统列表",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getBusinessList(@RequestParam(value = "dept1", required = false) String dept1,
                                              @RequestParam(value = "dept2", required = false) String dept2);

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取折扣列表", notes = "获取折扣列表",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getDiscountList(@RequestParam("type") String type);

    @PostMapping
    @ApiOperation(value = "提交折扣", notes = "更新折扣", tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Integer commitDiscount(@RequestBody CmdbBillsDiscountRequest discountRequest);

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询折扣", notes = "获取折扣",
            response = Map.class, tags = {"账单折扣管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> queryDiscount(@RequestBody List<String> ids);



}
