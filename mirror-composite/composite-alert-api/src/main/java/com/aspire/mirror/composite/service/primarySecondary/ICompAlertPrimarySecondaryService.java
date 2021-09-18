package com.aspire.mirror.composite.service.primarySecondary;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.primarySecondary.CompAlertPrimarySecondaryReq;
import com.aspire.mirror.composite.payload.primarySecondary.CompAlertPrimarySecondaryResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @author baiwp
 * @title: AlertPrimarySecondaryService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/${version}/alerts/primary_secondary")
@Api(value = "告警主次")
public interface ICompAlertPrimarySecondaryService {

    /**
     * 查询主次规则列表
     *
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询主次规则列表", notes = "查询主次规则列表", response = PageResponse.class, tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertPrimarySecondaryResp> list(@RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "status", required = false) String status,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 查询主次规则详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "查询主次规则详情", notes = "查询主次规则详情",
            tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertPrimarySecondaryResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertPrimarySecondaryResp.class)})
    CompAlertPrimarySecondaryResp detail(@PathVariable("id") String id);

    /**
     * 校验名称是否可用
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/check/{name}")
    @ApiOperation(value = "校验名称是否可用", notes = "校验名称是否可用",
            tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})

    Map<String, Object> checkName(@PathVariable("name") String name);

    /**
     * 新增主次规则
     *
     * @param CompAlertPrimarySecondaryReq
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增主次规则", notes = "新增主次规则", response = CompAlertPrimarySecondaryResp.class, tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertPrimarySecondaryResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertPrimarySecondaryResp.class)})
    CompAlertPrimarySecondaryResp insert(@RequestBody CompAlertPrimarySecondaryReq CompAlertPrimarySecondaryReq);

    /**
     * 修改主次规则
     *
     * @param CompAlertPrimarySecondaryReq
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改主次规则", notes = "修改主次规则", response = void.class, tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody CompAlertPrimarySecondaryReq CompAlertPrimarySecondaryReq);

    /**
     * 批量修改主次启动状态 ,0-停用，1-启用
     *
     * @param status
     * @param ids
     */
    @PutMapping(value = "/status/{status}")
    @ApiOperation(value = "批量修改主次启动状态", notes = "批量修改主次启动状态",
            tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void status(@PathVariable String status, @RequestBody String... ids);

    /**
     * 批量删除主次规则
     *
     * @param ids
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "批量删除主次规则", notes = "批量删除主次规则",
            tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void delete(@RequestBody String... ids);

    /**
     * 拷贝衍生规则
     */
    @GetMapping(value = "/copyDerive")
    @ApiOperation(value = "拷贝衍生规则", notes = "拷贝衍生规则",
            tags = {"AlertPrimarySecondary API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void copyDerive(@RequestParam("id") String id);
}
