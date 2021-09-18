package com.aspire.mirror.composite.service.isolate;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.isolate.CompAlertIsolateReq;
import com.aspire.mirror.composite.payload.isolate.CompAlertIsolateResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author baiwp
 * @title: AlertIsolateService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/${version}/alerts/isolate")
@Api(value = "告警屏蔽")
public interface ICompAlertIsolateService {

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
    PageResponse<CompAlertIsolateResp> list(@RequestParam(value = "name", required = false) String name,
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertIsolateResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertIsolateResp.class)})
    CompAlertIsolateResp detail(@PathVariable("id") String id);

    /**
     * 新增屏蔽规则
     *
     * @param alertIsolateReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增屏蔽规则", notes = "新增屏蔽规则", response = CompAlertIsolateResp.class, tags = {"AlertIsolate API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertIsolateResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertIsolateResp.class)})
    CompAlertIsolateResp insert(@RequestBody CompAlertIsolateReq alertIsolateReq);

    @PostMapping(value = "/insertFromBpm", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(BPM)新增屏蔽规则", notes = "(BPM)新增屏蔽规则", response = CompAlertIsolateResp.class, tags = {"AlertIsolate API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String,Object> insertFromBpm(@RequestBody CompAlertIsolateReq alertIsolateReq);

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
    void update(@RequestBody CompAlertIsolateReq alertIsolateReq);

    /**
     * 批量修改屏蔽启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}")
    @ApiOperation(value = "批量修改屏蔽启动状态", notes = "批量修改屏蔽启动状态",
            tags = {"AlertIsolate API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable(value = "status") String status,
                @RequestParam(value = "operater",required = false) String operater,
                @RequestBody String... ids);

    /**
     * 批量删除屏蔽规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "批量删除屏蔽规则", notes = "批量删除屏蔽规则",
            tags = {"AlertIsolate API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@RequestBody String... ids);
}
