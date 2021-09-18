package com.aspire.ums.bills.log;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.common.ListResult;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.payload.BillsLogRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BillsLogAPI
 * Author:   hangfang
 * Date:     2021/3/5
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "账单日志接口类")
@RequestMapping("/v1/cmdb/bill/log")
public interface BillsLogAPI {

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "获取账单操作日志", notes = "获取账单操作日志",
            response = Map.class, tags = {"CMDB Bill logs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ListResult<Map<String, Object>> listBillLogs(@RequestBody BillsLogRequest billsLogRequest);

    @RequestMapping(value = "/saveBillLog",method = RequestMethod.POST)
    @ApiOperation(value = "存储账单操作日志", notes = "存储账单操作日志",
            response = Map.class, tags = {"CMDB Bill logs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存储成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result saveBillLog(BillsLog bizLogEntity);


}
