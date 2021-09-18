package com.aspire.ums.cmdb.assessment;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/12/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "设备类型接口")
@RequestMapping("/cmdb/device")
public interface IDeviceTypeModelAPI {

    /**
     * 根据型号获取设备类型
     * @return 设备类型
     */
    @RequestMapping(value = "/type/model/getDeviceTypeByModel", method = RequestMethod.GET)
    @ApiOperation(value = "根据型号获取设备类型", notes = "根据型号获取设备类型", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbDeviceTypeModel getDeviceTypeByModel(@RequestParam("deviceModel") String deviceModel);

    /**
     * 根据类型获取设备型号
     * @return 设备类型
     */
    @RequestMapping(value = "/type/model/getModelByDeviceType", method = RequestMethod.GET)
    @ApiOperation(value = "根据类型获取设备型号", notes = "根据类型获取设备型号", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbDeviceTypeModel> getModelByDeviceType(@RequestParam("deviceType") String deviceType);

}
