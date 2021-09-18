package com.aspire.ums.bills.account;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.screen.payload.ScreenResponse;
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
 * @projectName: CmdbBillsAccountAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-12 10:37
 **/
@RequestMapping("/v1/cmdb/bill")
public interface CmdbBillsAccountAPI {

    @RequestMapping(value = "/record/payList",method = RequestMethod.GET)
    @ApiOperation(value = "缴费明细列表", notes = "缴费明细列表", tags = {"CMDB Bills Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getAccountPayRecord(@RequestParam("type") String type ,
                                                 @RequestParam("chargeTime") String chargeTime);

    @RequestMapping(value = "/record/payList/detail",method = RequestMethod.GET)
    @ApiOperation(value = "租户缴费明细列表", notes = "租户缴费明细列表", tags = {"CMDB Bills Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getAccountPayRecordDetail(@RequestParam("departmentId") String departmentId,
                                                  @RequestParam("chargeTime") String chargeTime);

    @RequestMapping(value = "/record/payList",method = RequestMethod.PUT)
    @ApiOperation(value = "修改缴费明细信息", notes = "修改缴费明细信息", tags = {"CMDB Bills Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result updateAccountRecord(@RequestBody CmdbBillsMonthRecord monthRecord);



    @RequestMapping(value = "/account/getByDept",method = RequestMethod.GET)
    @ApiOperation(value = "修改缴费明细信息", notes = "修改缴费明细信息", tags = {"CMDB Bills Account  API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbBillsAccountBalance getAccountBydepartment(@RequestParam("department") String departmentId);

}
