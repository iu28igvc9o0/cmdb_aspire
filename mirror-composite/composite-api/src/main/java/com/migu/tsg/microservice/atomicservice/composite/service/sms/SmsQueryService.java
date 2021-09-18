package com.migu.tsg.microservice.atomicservice.composite.service.sms;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.migu.tsg.microservice.atomicservice.composite.service.sms.payload.SmsQueryResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 项目名称: 咪咕微服务运营平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.service.sms 类名称:
 * SmsQueryService.java 类描述: 创建人: GaoYang 创建时间: 2017/12/18 17:19 版本: v1.0
 */
@RequestMapping("/${version}/sms")
@Api(value = "Composite Resource management(sms-query)", description = "Composite Resource management(sms-query)")
public interface SmsQueryService {
    /**
     * 响应成功
     */
    int RESPONSE_SUCCESS200 = 200;

    /**
     * 查询短信发送记录
     *
     * @param namespace
     *            命名空间
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param pageNo
     *            起始页
     * @param pageSize
     *            每页数量
     * @param bizType
     *            业务类型
     * @param addresses
     *            手机号码
     * @param message
     *            短信内容
     * @return 返回短信查询数据
     */
    @RequestMapping(value = "/{namespace}/search", method = RequestMethod.GET, produces = { "application/json" })
    @ApiOperation(value = "查询短信列表", notes = "查询短信列表", response = SmsQueryResponse.class, tags = {
            "Composite Resource management(sms-query)" })
    @ApiResponses(value = {
            @ApiResponse(code = RESPONSE_SUCCESS200, message = "短信列表", response = SmsQueryResponse.class) })
    @ResponseStatus(HttpStatus.OK)
    SmsQueryResponse listSms(@PathVariable("namespace") String namespace, @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime, @RequestParam("pageNo") Integer pageNo,
            @RequestParam("size") Integer pageSize, @RequestParam("bizType") String bizType,
            @RequestParam("addresses") String addresses, @RequestParam("message") String message);
}