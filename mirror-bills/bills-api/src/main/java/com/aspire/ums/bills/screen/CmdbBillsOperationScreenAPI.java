package com.aspire.ums.bills.screen;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-14 16:32
 */
@RequestMapping("/v1/cmdb/bill/screen/operation")
public interface CmdbBillsOperationScreenAPI {

    @GetMapping("/loadDeptTypeList")
    @ApiOperation(value = "获取租户类型列表", notes = "获取租户类型列表", tags = {"账单大屏-运营侧"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getDeptTypeList();

    @GetMapping("/expenses")
    @ApiOperation(value = "租户月度费用总计", notes = "租户月度费用总计", tags = {"账单大屏-运营侧"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getTotalExpense(@RequestParam("chargeTime") String chargeTime);

    @GetMapping("/resources")
    @ApiOperation(value = "各类产品月度总账单展示", notes = "各类产品月度总账单展示", tags = {"账单大屏-运营侧"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, List<Map<String, String>>> getResourcesMonthBills(@RequestParam("idcId") String idcId,
                                                    @RequestParam("chargeTime") String chargeTime,
                                                    @RequestParam("deptTypeId") String deptTypeId);

    @GetMapping("/list")
    @ApiOperation(value = "租户月度账单列表", notes = "租户月度账单列表", tags = {"账单大屏-运营侧"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getMonthBills(@RequestParam("deptTypeId") String deptTypeId,
                                            @RequestParam("chargeTime") String chargeTime);

}
