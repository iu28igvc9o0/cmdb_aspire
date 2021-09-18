package com.aspire.ums.cmdb.report;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProvinceReportTableAPI
 * Author:   hangfang
 * Date:     2020/5/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/province/report/table")
public interface IProvinceReportTableAPI {

    @GetMapping("/listByOwnerAndPage")
    @ApiOperation(value = "根据归属和类型返回表", notes = "根据类型返回表", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Cmdb31ProvinceTable> listByOwnerAndPage(@RequestParam(value = "resourceOwner", required = false) String resourceOwner,
                                         @RequestParam("type") String type);
}
