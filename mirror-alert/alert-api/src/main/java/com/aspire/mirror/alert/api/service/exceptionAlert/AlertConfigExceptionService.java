package com.aspire.mirror.alert.api.service.exceptionAlert;

import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionReq;
import com.aspire.mirror.alert.api.dto.exceptionAlert.AlertConfigExceptionResp;
import com.aspire.mirror.common.entity.PageResponse;
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
@RequestMapping("/v1/alerts/config-exception")
@Api(value = "告警异常信息")
public interface AlertConfigExceptionService {

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
    PageResponse<AlertConfigExceptionResp> list(@RequestParam(value = "name", required = false) String name,
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertConfigExceptionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertConfigExceptionResp.class)})
    AlertConfigExceptionResp detail(@PathVariable("id") String id);

    /**
     * 新增异常信息规则
     *
     * @param AlertConfigExceptionReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增异常信息规则", notes = "新增异常信息规则", response = AlertConfigExceptionResp.class, tags = {"AlertConfigException API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertConfigExceptionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertConfigExceptionResp.class)})
    AlertConfigExceptionResp insert(@RequestBody AlertConfigExceptionReq AlertConfigExceptionReq);

    /**
     * 修改异常信息规则
     *
     * @param AlertConfigExceptionReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改异常信息规则", notes = "修改异常信息规则", response = void.class, tags = {"AlertConfigException API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody AlertConfigExceptionReq AlertConfigExceptionReq);

    /**
     * 批量修改异常信息启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}/{operater}")
    @ApiOperation(value = "批量修改异常信息启动状态", notes = "批量修改异常信息启动状态",
            tags = {"AlertConfigException API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable("status") String status,
                @PathVariable("operater") String operater,
                @RequestBody String... ids);

    /**
     * 批量删除异常信息规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete/{operater}")
    @ApiOperation(value = "批量删除异常信息规则", notes = "批量删除异常信息规则",
            tags = {"AlertConfigException API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@PathVariable("operater") String operater, @RequestBody String... ids);
}
