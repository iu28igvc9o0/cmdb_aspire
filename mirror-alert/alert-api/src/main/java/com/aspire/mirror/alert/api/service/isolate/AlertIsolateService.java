package com.aspire.mirror.alert.api.service.isolate;

import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateReq;
import com.aspire.mirror.alert.api.dto.isolate.AlertIsolateResp;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author baiwp
 * @title: AlertIsolateService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/v1/alerts/isolate")
@Api(value = "告警屏蔽")
public interface AlertIsolateService {

    /**
     * 查询屏蔽规则列表
     *
     * @param name
     * @param status
     * @param effectiveDateFrom
     * @param effectiveDateTo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询屏蔽规则列表", notes = "查询屏蔽规则列表", response = PageResponse.class, tags = {"AlertIsolate API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertIsolateResp> list(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "status", required = false) String status,
                                        @RequestParam(value = "effectiveDateFrom", required = false) String effectiveDateFrom,
                                        @RequestParam(value = "effectiveDateTo", required = false) String effectiveDateTo,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 查询屏蔽规则详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "查询屏蔽规则详情", notes = "查询屏蔽规则详情",
            tags = {"AlertIsolate API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertIsolateResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertIsolateResp.class)})
    AlertIsolateResp detail(@PathVariable("id") String id);

    /**
     * 新增屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增屏蔽规则", notes = "新增屏蔽规则", response = AlertIsolateResp.class, tags = {"AlertIsolate API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertIsolateResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertIsolateResp.class)})
    AlertIsolateResp insert(@RequestBody AlertIsolateReq alertIsolateReq);

    /**
     * 修改屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改屏蔽规则", notes = "修改屏蔽规则", response = void.class, tags = {"AlertIsolate API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody AlertIsolateReq alertIsolateReq);

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}/{operater}")
    @ApiOperation(value = "批量修改屏蔽启动状态", notes = "批量修改屏蔽启动状态",
            tags = {"AlertIsolate API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable("status") String status, 
    		@PathVariable("operater") String operater, 
    		@RequestBody String... ids);

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete/{operater}")
    @ApiOperation(value = "批量删除屏蔽规则", notes = "批量删除屏蔽规则",
            tags = {"AlertIsolate API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@PathVariable("operater") String operater, @RequestBody String... ids);
}
