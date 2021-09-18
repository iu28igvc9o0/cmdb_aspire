package com.aspire.mirror.composite.service.exceptionAlert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.exceptionAlert.CompAlertConfigExceptionReq;
import com.aspire.mirror.composite.payload.exceptionAlert.CompAlertConfigExceptionResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author baiwp
 * @title: AlertConfigExceptionService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/${version}/alerts/config-exception")
@Api(value = "告警异常信息")
public interface ICompAlertConfigExceptionService {

    /**
     * 查询异常信息规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询异常信息规则列表", notes = "查询异常信息规则列表", response = PageResponse.class, tags = {"AlertConfigException API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertConfigExceptionResp> list(@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "status", required = false) String status,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 查询异常信息规则详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "查询异常信息规则详情", notes = "查询异常信息规则详情",
            tags = {"AlertConfigException API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertConfigExceptionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertConfigExceptionResp.class)})
    CompAlertConfigExceptionResp detail(@PathVariable("id") String id);

    /**
     * 新增异常信息规则
     *
     * @param compAlertConfigExceptionReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增异常信息规则", notes = "新增异常信息规则", response = CompAlertConfigExceptionResp.class, tags = {"AlertConfigException API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertConfigExceptionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertConfigExceptionResp.class)})
    CompAlertConfigExceptionResp insert(@RequestBody CompAlertConfigExceptionReq compAlertConfigExceptionReq);

    /**
     * 修改异常信息规则
     *
     * @param compAlertConfigExceptionReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改异常信息规则", notes = "修改异常信息规则", response = void.class, tags = {"AlertConfigException API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody CompAlertConfigExceptionReq compAlertConfigExceptionReq);

    /**
     * 批量修改异常信息启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}")
    @ApiOperation(value = "批量修改异常信息启动状态", notes = "批量修改异常信息启动状态",
            tags = {"AlertConfigException API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable("status") String status,
                @RequestBody String... ids);

    /**
     * 批量删除异常信息规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "批量删除异常信息规则", notes = "批量删除异常信息规则",
            tags = {"AlertConfigException API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@RequestBody String... ids);
}
