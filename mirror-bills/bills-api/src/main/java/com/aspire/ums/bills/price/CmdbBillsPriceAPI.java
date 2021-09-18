package com.aspire.ums.bills.price;

import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 10:32
 */
@Api(value = "账单单价管理接口类")
@RequestMapping("/v1/cmdb/bill/price")
public interface CmdbBillsPriceAPI {

    @GetMapping(value = "/loadIdcList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取资源池列表", notes = "获取资源池列表",
            response = Map.class, tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> loadIdcList();

    @GetMapping(value = "/loadDeviceTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取设备类型列表", notes = "获取设备类型列表",
            response = Map.class, tags = {"账单单价管理"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> loadDeviceTypeList();

    @PostMapping
    @ApiOperation(value = "新增单价", notes = "新增单价", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Response insertPrice(@RequestBody CmdbBillsPriceRequest priceRequest);

    @GetMapping
    @ApiOperation(value = "条件获取单价", notes = "条件获取单价", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CmdbBillsPriceResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbBillsPriceResponse getPriceByCondition(@RequestParam("idcId") String idcId,
                                               @RequestParam("deviceTypeId") String deviceTypeId);

    @GetMapping("/detail")
    @ApiOperation(value = "获取单价详情", notes = "获取单价详情", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CmdbBillsPriceResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbBillsPriceResponse getPriceDetailById(@RequestParam("id") String id);

    @PutMapping
    @ApiOperation(value = "修改单价", notes = "修改单价", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "修改成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Integer updatePrice(@RequestBody CmdbBillsPriceRequest priceRequest);

    @DeleteMapping
    @ApiOperation(value = "删除单价", notes = "删除单价", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Integer deletePrice(@RequestParam("id") String id);

    @GetMapping(value = "/list" )
    @ApiOperation(value = "查询单价列表", notes = "查询单价列表", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CmdbBillsPriceResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbBillsPriceResponse> getPriceList();

    @GetMapping(value = "/unit")
    @ApiOperation(value = "查询单价单位列表", notes = "查询单价单位列表", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getPriceUnitList();

    @GetMapping(value = "/device")
    @ApiOperation(value = "查询设备月单价列表", notes = "查询单价列表", tags = {"CMDB Bills Price API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getDevicePriceList(@RequestParam("idcId") String idcId);
}
