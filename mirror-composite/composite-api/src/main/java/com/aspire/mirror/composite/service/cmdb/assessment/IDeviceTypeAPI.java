package com.aspire.mirror.composite.service.cmdb.assessment;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IDeviceTypeAPI
 * Author:   hangfang
 * Date:     2019/6/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/device/")
public interface IDeviceTypeAPI {

    /**
     * 查询所有设备类型
     * @return 设备类型
     */
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有设备类型", notes = "查询所有设备类型", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbDeviceType> list();

}
