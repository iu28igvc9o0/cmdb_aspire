package com.aspire.mirror.alert.api.service.businessAlert;

import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessReq;
import com.aspire.mirror.alert.api.dto.businessAlert.AlertConfigBusinessResp;
import com.aspire.mirror.common.entity.PageResponse;
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
@RequestMapping("/v1/alerts/config-business")
@Api(value = "告警业务告警")
public interface AlertConfigBusinessService {

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
    PageResponse<AlertConfigBusinessResp> list(@RequestParam(value = "name", required = false) String name,
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertConfigBusinessResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertConfigBusinessResp.class)})
    AlertConfigBusinessResp detail(@PathVariable("id") String id);

    /**
     * 新增业务告警规则
     *
     * @param AlertConfigBusinessReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增业务告警规则", notes = "新增业务告警规则", response = AlertConfigBusinessResp.class, tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertConfigBusinessResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertConfigBusinessResp.class)})
    AlertConfigBusinessResp insert(@RequestBody AlertConfigBusinessReq AlertConfigBusinessReq);

    /**
     * 修改业务告警规则
     *
     * @param AlertConfigBusinessReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改业务告警规则", notes = "修改业务告警规则", response = void.class, tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody AlertConfigBusinessReq AlertConfigBusinessReq);

    /**
     * 批量修改业务告警启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}/{operater}")
    @ApiOperation(value = "批量修改业务告警启动状态", notes = "批量修改业务告警启动状态",
            tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable("status") String status,
                @PathVariable("operater") String operater,
                @RequestBody String... ids);

    /**
     * 批量删除业务告警规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete/{operater}")
    @ApiOperation(value = "批量删除业务告警规则", notes = "批量删除业务告警规则",
            tags = {"AlertConfigBusiness API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@PathVariable("operater") String operater, @RequestBody String... ids);
}
