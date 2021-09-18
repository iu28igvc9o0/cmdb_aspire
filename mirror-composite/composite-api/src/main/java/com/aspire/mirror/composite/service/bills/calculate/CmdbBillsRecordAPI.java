package com.aspire.mirror.composite.service.bills.calculate;

import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsRecordAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-03 16:24
 **/
@RequestMapping("${version}/cmdb/bill")
public interface CmdbBillsRecordAPI {

    @RequestMapping(value = "/monthRecord/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询月账单", notes = "查询月账单", tags = {"CMDB Bills API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> listBillMonthRecord(@RequestParam("chargeTime") String chargeTime);

    @RequestMapping(value = "/monthRecord/update",method = RequestMethod.PUT)
    @ApiOperation(value = "冲销功能，修改月账单", notes = "冲销功能，修改月账单", tags = {"CMDB Bills API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> updateBillMonth(@RequestBody CmdbBillsMonthBill monthBill);

    @RequestMapping(value = "/monthRecord/manuallyCreate",method = RequestMethod.PUT)
    @ApiOperation(value = "手工生成月度账单", notes = "手工生成月度账单", tags = {"CMDB Bills API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> manuallyGeneratedMonthBills(@RequestParam("chargeTime") String chargeTime);
}
