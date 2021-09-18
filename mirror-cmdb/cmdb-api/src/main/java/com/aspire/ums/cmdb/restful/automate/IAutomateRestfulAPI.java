package com.aspire.ums.cmdb.restful.automate;

import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
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
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAutomateRestfulAPI
 * Author:   zhu.juwang
 * Date:     2019/9/11 10:55
 * Description: 该接口类用来专门提供接口给自动化运维工具使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/restful/automate")
public interface IAutomateRestfulAPI {
    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @RequestMapping(value = "/getInstanceList", method = RequestMethod.POST)
    @ApiOperation(value = "根据业务系统名称获取部门信息", notes = "根据业务系统名称获取部门信息", tags = {"Cmdb To Automate Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbInstance> getInstanceList(@RequestBody Map<String, Object> params);
}
