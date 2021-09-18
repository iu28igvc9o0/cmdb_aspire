package com.aspire.mirror;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.IRealMirrorAPI
 */
@Api(value = "集群", description = "集群API")
@RequestMapping(value = "/exception")
public interface IMirrorAPI {

    /**
     * 手工计算指标
     */
    @RequestMapping(value = "/indication/calc", method = RequestMethod.GET)
    @ApiOperation(value = "手工计算指标", notes = "手工计算指标", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    void calcIndication(@RequestParam(value = "formPlat", required = false) String formPlat,
                        @RequestParam(value = "indicationOwner") String indicationOwner,
                        @RequestParam(value = "indicationFrequency") String indicationFrequency,
                        @RequestParam(value = "beginDate") String beginDate,
                        @RequestParam(value = "endDate") String endDate,
                        @RequestParam(value = "indicationId", required = false) Integer indicationId) throws ParseException;
    
    /**
     * 查询异常数据
     */
    @RequestMapping(value = "/indication/getExceptionData", method = RequestMethod.GET)
    @ApiOperation(value = "查询异常数据", notes = "查询异常数据", response = String.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    String getExceptionData(
            @RequestParam(value = "indicationOwner") String indicationOwner,
            @RequestParam(value = "catalogBox") String catalogBox,
            @RequestParam(value = "indicationCycle") String indicationCycle,
            @RequestParam(value = "indicationFrequency") String indicationFrequency,
            @RequestParam(value = "dateTime") String dateTime,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "provinceCode") String provinceCode);

    /**
     * 指标数据同步 mirror_indication_data --> mirror_indication_data_copy
     */
    @RequestMapping(value = "/indication/synchronizationIndicationData", method = RequestMethod.POST)
    @ApiOperation(value = "查询异常数据", notes = "查询异常数据", response = String.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
	void synchronizationIndicationData();

    /**
     * 查询异常数据
     */
    @RequestMapping(value = "/indication/getEmail", method = RequestMethod.GET)
    @ApiOperation(value = "查询发送邮件内容", notes = "查询发送邮件内容", response = String.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    JSONObject getEMail( @RequestParam(value = "indicationOwner") String indicationOwner,
                         @RequestParam(value = "catalogBox") String catalogBox,
                         @RequestParam(value = "indicationCycle") String indicationCycle,
                         @RequestParam(value = "indicationFrequency") String indicationFrequency,
                         @RequestParam(value = "dateTime") String dateTime);

}
