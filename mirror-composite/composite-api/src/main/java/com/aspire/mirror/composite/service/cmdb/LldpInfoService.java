package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.cmdb.physical.PhysicalTopoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * lldp物理设备关联数据
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb
 * 类名称:    LldpInfoService.java
 * 类描述:    lldp物理设备关联数据
 * 创建人:    JinSu
 * 创建时间:  2019/9/20 14:03
 * 版本:      v1.0
 */
@Api(value = "lldp物理设备关联数据")
@RequestMapping("${version}/lldp")
public interface LldpInfoService {
    @GetMapping(value = "/getPhysicalTopology")
    @ApiOperation(value = "根据pod获取物理topo数据", notes = "根据pod获取物理topo数据", tags = {"LLDP INFO API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PhysicalTopoResponse getPhysicalTopology(@RequestParam(value = "idcType") String idcType, @RequestParam(value = "podName") String podName);
}
