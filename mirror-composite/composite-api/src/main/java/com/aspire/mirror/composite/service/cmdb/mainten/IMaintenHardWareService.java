package com.aspire.mirror.composite.service.cmdb.mainten;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 类名称: IMaintenHardWareService
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/31 11:16
 * 版本: v1.0
 */
@Api(value = "维保硬件管理")
@RequestMapping("${version}/cmdb/mainten/hardware")
public interface IMaintenHardWareService {

    /**
     *  分页查询硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/selectHardwareByPage" )
    @ApiOperation(value = "查询硬件维保数据", notes = "查询硬件维保数据", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Hardware> selectHardwareByPage(@RequestBody HardwareRequest request);

    /**
     *  更新硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/updateHardware" )
    @ApiOperation(value = "修改硬件维保", notes = "修改硬件维保", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> updateHardware(@RequestBody Hardware hardware);


    /**
     *  批量更新硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/batchUpdateHardware" )
    @ApiOperation(value = "批量更新硬件维保", notes = "批量更新硬件维保", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> batchUpdateHardware(@RequestBody Hardware hardwareList);



    /**
     *  查询硬件维保列表
     * @return 模型列表
     */
    @PostMapping(value = "/getHardwareList" )
    @ApiOperation(value = "导出硬件维保数据", notes = "导出硬件维保数据", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Hardware> getHardwareList(@RequestBody HardwareRequest request);


    /**
     *  导出硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/export" )
    @ApiOperation(value = "导出硬件维保", notes = "导出硬件维保", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> export (HttpServletResponse response,
                                @RequestBody HardwareRequest sendRequest);

    /**
     *  查询硬件维保数据是否已存在
     * @return
     */
    @GetMapping(value = "/queryIsExist" )
    @ApiOperation(value = "查询硬件维保数据是否已存在", notes = "查询硬件维保数据是否已存在", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> queryIsExist(@RequestParam("deviceSerialNumber") String deviceSerialNumber, @RequestParam("warrantyDate") String warrantyDate);

    /**
     *  查询信息，依据维保项目名称 和 序列号
     * @return 模型列表
     */
    @GetMapping(value = "/getNeedInfo" )
    @ApiOperation(value = "依据维保项目名称和序列号查询信息", notes = "依据维保项目名称和序列号查询信息", tags = {"CMDB MaintenHardWare API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> queryInfoByNameAndDeviceSn(@RequestParam("projectName") String projectName,@RequestParam("deviceSn") String deviceSn);
}
