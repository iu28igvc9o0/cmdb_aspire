package com.aspire.ums.bills.screen;

import com.aspire.ums.bills.screen.payload.ScreenResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsScreenAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-05 19:51
 **/
@RequestMapping("/v1/cmdb/bill/screen")
public interface CmdbBillsScreenAPI {

    @RequestMapping(value = "/resourceType",method = RequestMethod.GET)
    @ApiOperation(value = "查询该租户下资源类型的月账单统计", notes = "查询该租户下资源类型的月账单统计", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<Map<String,Object>> getMonthBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                     @RequestParam("department") String department,
                                                                     @RequestParam("idcType") String idcType,
                                                                     @RequestParam("resourceType") String resourceType);

    @RequestMapping(value = "/quotaAndBill/biz",method = RequestMethod.GET)
    @ApiOperation(value = "查询业务系统-资源池的配额和总价", notes = "查询业务系统-资源池的配额和总价", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(@RequestParam("chargeTime") String chargeTime,
                                                                      @RequestParam("department") String department);

    @RequestMapping(value = "/quotaAndBill/resource",method = RequestMethod.GET)
    @ApiOperation(value = "查询业务系统-资源池 具体设备的配额和总价", notes = "查询业务系统-资源池 具体设备的配额和总价", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<List<Map<String,Object>>> getQuotaAndBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                              @RequestParam("department") String department,
                                                                              @RequestParam("bizSystem") String bizSystem,
                                                                              @RequestParam("idcType") String idcType,
                                                                              @RequestParam("resourceType") String resourceType);

    @RequestMapping(value = "/account/record",method = RequestMethod.GET)
    @ApiOperation(value = "获取年份的缴费记录", notes = "获取年份的缴费记录", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<Map<String,Object>> getAccountRevenueRecord(@RequestParam("year") String year,
                                                               @RequestParam("department") String department);

    @RequestMapping(value = "/unitPrice",method = RequestMethod.GET)
    @ApiOperation(value = "获取单价", notes = "获取单价", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<List<Map<String,Object>>> getUnitPrice(@RequestParam("bizSystem") String bizSystem,
                                                          @RequestParam("department") String department,
                                                          @RequestParam("idcType") String idcType);

    @RequestMapping(value = "/account/info",method = RequestMethod.GET)
    @ApiOperation(value = "获取租户账号余额信息", notes = "获取租户账号余额信息", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ScreenResponse<Map<String,Object>> getAccountInfo(@RequestParam("department") String department,
                                                      @RequestParam("monthEnd") String monthEnd);

    @RequestMapping(value = "/download/bills",method = RequestMethod.GET)
    @ApiOperation(value = "下载明细", notes = "下载明细", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = ScreenResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> exportBillsDetail(@RequestParam("department") String department,
                                               @RequestParam("chargeTime") String chargeTime);

    @RequestMapping(value = "/bills/detail",method = RequestMethod.GET)
    @ApiOperation(value = "账单具体详情明细", notes = "账单具体详情明细", tags = {"CMDB Bills Screen API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getSpecificBillsWithResourceType(@RequestParam("department") String department,
                                                              @RequestParam("chargeTime") String chargeTime);
}
