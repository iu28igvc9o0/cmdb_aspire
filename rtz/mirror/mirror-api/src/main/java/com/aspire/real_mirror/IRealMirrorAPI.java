package com.aspire.real_mirror;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.IRealMirrorAPI
 */
@Api(value = "指标", description = "指标API")
@RequestMapping(value = "/exception")
public interface IRealMirrorAPI {

    /**
     * 获取指标列表
     */
    @RequestMapping(value = "/indication/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取指标列表", notes = "获取指标列表", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    Object getIndicationList(@RequestParam(value = "indicationOwner") String indicationOwner,
                                             @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                             @RequestParam(value = "indicationPosition", required = false) String indicationPosition,
                                             @RequestParam(value = "indicationFrequency") String indicationFrequency) throws RuntimeException;

    /**
     * 获取指标列表
     */
    @RequestMapping(value = "/indication/list/data", method = RequestMethod.GET)
    @ApiOperation(value = "获取指标列表", notes = "获取指标列表", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    JSONObject getIndicationData(@RequestParam(value = "indicationOwner") String indicationOwner,
                                 @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                 @RequestParam(value = "indicationPosition", required = false) String indicationPosition,
                                 @RequestParam(value = "indicationFrequency") String indicationFrequency,
                                 @RequestParam(value = "calcDate") String calcDate,
                                 @RequestParam(value = "provinceCode", required = false) String provinceCode,
                                 @RequestParam(value = "period", required = false) String period) throws RuntimeException;

    /**
     * 获取指标列表
     */
    @RequestMapping(value = "/indication/email/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取指标列表", notes = "获取指标列表", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    JSONObject getSendEmailContent(@RequestParam(value = "indicationOwner") String indicationOwner,
                                   @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                   @RequestParam(value = "indicationPosition", required = false) String indicationPosition,
                                   @RequestParam(value = "indicationFrequency") String indicationFrequency,
                                   @RequestParam(value = "calcDate") String calcDate) throws RuntimeException;

    /**
     * 获取指标列表
     */
    @RequestMapping(value = "/indication/indication/reload", method = RequestMethod.GET)
    @ApiOperation(value = "重新获取指标列表", notes = "重新获取指标列表", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    JSONArray reloadIndication() throws RuntimeException;

    /**
     * 重新计算指标数据
     */
    @RequestMapping(value = "/indication/indication/reCalc", method = RequestMethod.GET)
    @ApiOperation(value = "重新计算指标数据", notes = "重新计算指标数据", response = void.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
            @ApiResponse(code = 500, message = "执行数据异常") })
    void reCalcIndication(@RequestParam(value = "indicationOwner") String indicationOwner,
                          @RequestParam(value = "indicationCatalog") String indicationCatalog,
                          @RequestParam(value = "indicationPosition", required = false) String indicationPosition,
                          @RequestParam(value = "indicationFrequency") String indicationFrequency,
                          @RequestParam(value = "beginDate") String beginDate,
                          @RequestParam(value = "endDate") String endDate) throws RuntimeException;

//    /**
//     *保存阈值
//     */
//    @RequestMapping(value = "/indication/saveLimits", method = RequestMethod.POST)
//    @ApiOperation(value = "保存阈值", notes = "保存阈值", response = void.class, tags = {
//            "Indication Information API" })
//    @ApiResponses(value = { @ApiResponse(code = 200, message = "执行成功"),
//            @ApiResponse(code = 500, message = "执行数据异常") })
//    String saveGatewayMerit(JSONObject meritsLimits);

    /**
     * 同步阈值
     */
    @RequestMapping(value = "/indication/syncLimits", method = RequestMethod.POST)
    @ApiOperation(value = "同步产品化指标阈值", notes = "同步产品化指标阈值", response = void.class, tags = {
            "Indication Sync API" })
	void synchronizationLimits();

//    /**
//     * 同步主题数据
//     */
//    @RequestMapping(value = "/theme/syncTheme", method = RequestMethod.POST)
//    @ApiOperation(value = "同步产品化主题数据", notes = "同步产品化主题数据", response = JSONArray.class, tags = {
//            "Indication Sync API" })
//    JSONArray syncTheme();

//    /**
//     * 同步指标列表
//     */
//    @RequestMapping(value = "/theme/syncIndication", method = RequestMethod.POST)
//    @ApiOperation(value = "同步产品化指标列表", notes = "同步产品化指标列表", response = JSONArray.class, tags = {
//            "Indication Sync API" })
//    JSONArray syncIndication();
}
