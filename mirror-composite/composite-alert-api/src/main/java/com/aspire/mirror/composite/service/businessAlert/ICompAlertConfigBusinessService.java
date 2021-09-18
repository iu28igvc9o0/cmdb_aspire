package com.aspire.mirror.composite.service.businessAlert;


import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.businessAlert.CompAlertConfigBusinessReq;
import com.aspire.mirror.composite.payload.businessAlert.CompAlertConfigBusinessResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author baiwp
 * @title: AlertConfigBusinessService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/${version}/alerts/config-business")
@Api(value = "告警业务告警")
public interface ICompAlertConfigBusinessService {

    /**
     * 查询业务告警规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询业务告警规则列表", notes = "查询业务告警规则列表", response = PageResponse.class, tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertConfigBusinessResp> list(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "status", required = false) String status,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 查询业务告警规则详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "查询业务告警规则详情", notes = "查询业务告警规则详情",
            tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertConfigBusinessResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertConfigBusinessResp.class)})
    CompAlertConfigBusinessResp detail(@PathVariable("id") String id);

    /**
     * 新增业务告警规则
     *
     * @param compAlertConfigBusinessReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增业务告警规则", notes = "新增业务告警规则", response = CompAlertConfigBusinessResp.class, tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertConfigBusinessResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertConfigBusinessResp.class)})
    CompAlertConfigBusinessResp insert(@RequestBody CompAlertConfigBusinessReq compAlertConfigBusinessReq);

    /**
     * 修改业务告警规则
     *
     * @param compAlertConfigBusinessReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改业务告警规则", notes = "修改业务告警规则", response = void.class, tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody CompAlertConfigBusinessReq compAlertConfigBusinessReq);

    /**
     * 批量修改业务告警启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}")
    @ApiOperation(value = "批量修改业务告警启动状态", notes = "批量修改业务告警启动状态",
            tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable("status") String status,
                @RequestBody String... ids);

    /**
     * 批量删除业务告警规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "批量删除业务告警规则", notes = "批量删除业务告警规则",
            tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@RequestBody String... ids);
}
