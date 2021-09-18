package com.aspire.mirror.composite.service.cmdb.mainten;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 类名称: IMaintenHardWareUseService
 * 类描述: 维保硬件使用
 * 创建人: PJX
 * 创建时间: 2019/8/4 13:39
 * 版本: v1.0
 */
@Api(value = "维保硬件使用管理")
@RequestMapping("${version}/cmdb/mainten/hardwareuse")
public interface IMaintenHardWareUseService {

    /**
     * 新增
     * @param hardwareUse
     * @return
     */
    @PostMapping(value = "/addHardwareUse")
    @ApiOperation(value = "新增硬件维保使用数据", notes = "新增硬件维保使用数据", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> addHardwareUse(@RequestBody HardwareUse hardwareUse);

    /**
     * 批量新增硬件维保使用记录
     * @param list
     * @return
     */
    @PostMapping(value = "/batchAddHardwareUse" )
    @ApiOperation(value = "批量新增硬件维保使用记录", notes = "批量新增硬件维保使用记录", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> batchAddHardwareUse(@RequestBody List<HardwareUse> list);

    /**
     *  分页查询硬件维保使用数据
     * @return 模型列表
     */
    @PostMapping(value = "/selectHardwareUseByPage" )
    @ApiOperation(value = "查询硬件维保使用数据", notes = "查询硬件维保使用数据", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<HardwareUse> selectHardwareUseByPage(@RequestBody HardwareUseRequest request);

    /**
     *  更新硬件维保使用
     * @return 模型列表
     */
    @PostMapping(value = "/updateHardwareUse" )
    @ApiOperation(value = "修改硬件维保使用", notes = "修改硬件维保使用", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> updateHardwareUse(@RequestBody HardwareUse hardwareUse);

    /**
     *  批量更新硬件维保使用
     * @return 模型列表
     */
    @PostMapping(value = "/batchUpdateHardwareUse" )
    @ApiOperation(value = "批量更新硬件维保使用", notes = "批量更新硬件维保使用", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "批量修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> batchUpdateHardwareUse(@RequestBody HardwareUse hardwareUseList);

    /**
     * 批量逻辑删除
     * @param id 用逗号隔开
     * @return
     */
    @DeleteMapping(value = "/deleteHardwareUse" )
    @ApiOperation(value = "删除硬件维保使用", notes = "删除硬件维保使用", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteHardwareUse(@RequestParam("id") String id);

    /**
     * 查询可用硬件维保数据
     * @return
     */
    @PostMapping(value = "/getHardwareTableList" )
    @ApiOperation(value = "查询硬件维保可选数据", notes = "查询硬件维保可选数据", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getHardwareTableList();

    /**
     *  导出硬件维保使用数据
     * @return 模型列表
     */
    @PostMapping(value = "/export" )
    @ApiOperation(value = "导出硬件维保使用", notes = "导出硬件维保使用", tags = {"CMDB MaintenHardWareUse API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> export(HttpServletResponse response, @RequestBody Map<String, Object> sendRequest);
}
