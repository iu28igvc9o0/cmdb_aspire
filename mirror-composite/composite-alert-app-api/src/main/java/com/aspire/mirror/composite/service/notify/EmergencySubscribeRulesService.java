package com.aspire.mirror.composite.service.notify;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.notify.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订阅告警管理")
@RequestMapping(value = "/v1/alerts/SubscribeRules/")
public interface EmergencySubscribeRulesService {
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 订阅告警管理的查询", notes = "订阅告警管理的查询，通知订阅告警管理的查询", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertSubscribeRulesResponse> query(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                    @RequestParam(value = "deviceIp", required = false) String deviceIp,
                                                    @RequestParam(value = "alertLevel", required = false) String alertLevel,
                                                    @RequestParam(value = "idcType", required = false) String idcType,
                                                    @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);


    @PostMapping(value = "/queryRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 通知订阅告警管理的查询", notes = "订阅告警管理的查询，通知订阅告警管理的查询", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertSubscribeRulesResponse> queryRules(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                         @RequestParam(value = "isOpen", required = false) String isOpen,
                                                         @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    @GetMapping(value = "/deteleRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 批量删订阅告警", notes = "订阅告警管理的查询，通知订阅告警管理的查询", tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> deteleRules(@RequestParam("ids") String ids);

    @PostMapping(value = "/updateRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 批量更新订阅告警", notes = "订阅告警管理的查询，通知订阅告警管理的查询", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    ResponseEntity<String> updateRules(@RequestParam("ids") String ids, @RequestParam("isOpen") String isOpen);


    @ApiOperation(value = "导出订阅告警", notes = "导出订阅告警", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void export(@RequestParam("ids")String ids, HttpServletResponse response)throws Exception;

    @PostMapping(value = "/querySubscribeRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "规则名称，告警等级，归属资源池", notes = "规则名称，告警等级，归属资源池", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertSubscribeRulesManagementRespone.class)})
    List<AlertSubscribeRulesManagementRespone> querySubscribeRules();

    @PostMapping(value = "/emailNotify"  ,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "邮件内容组装", notes = "邮件内容组装",
            tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<EmergencySubscribeRulesEmailRespone> emailNotify(@RequestBody EmergencySubscribeRules emergencySubscribeRules);

    /**
     * 添加订阅告警
     * @param createAlertSubscribeRulesReq
     * @return
     */
    @PostMapping(value = "/CreateSubscribeRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加订阅告警", notes = "添加订阅告警", tags = {"AlertSubscribeRules API"}, response = ResponseEntity.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error",response = ResponseEntity.class)})
    ResponseEntity<String> CreateSubscribeRules(@RequestBody CreateAlertSubscribeRulesReq createAlertSubscribeRulesReq);
    /**
     *
     */
    @PostMapping(value = "/UpdateSubscribeRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新订阅告警", notes = "更新订阅告警", response = ResponseEntity.class,tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> UpdateSubscribeRules(@RequestBody UpdateAlertSubscribeRulesRequest updateAlertSubscribeRulesRequest);
    /**
     *
     */
    @GetMapping(value = "/GetSubscribeRulesById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "规则名称，告警等级，归属资源池", notes = "规则名称，告警等级，归属资源池", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertSubscribeRulesDetailShowDtoDetailResponse.class)})
    AlertSubscribeRulesDetailShowDtoDetailResponse GetSubscribeRulesById(@RequestParam("id")String id);




    @GetMapping(value = "/deleteAlertSubscribeRulesManagement", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 批量删除告警订阅详情", notes = "订阅告警管理的查询，通知订阅告警管理的查询", tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> deleteAlertSubscribeRulesManagement(@RequestParam("ids") String ids);

    @GetMapping(value = "/queryAlertSubscribeRules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "规则名称，告警等级，归属资源池", notes = "规则名称，告警等级，归属资源池", response = ResponseEntity.class, tags = {"AlertSubscribeRules API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertSubscribeRulesResponse.class)})

     List<AlertSubscribeRulesResponse>queryAlertSubscribeRules();
}
