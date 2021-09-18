package com.aspire.mirror;

import com.aspire.mirror.bean.IndicationLimitEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.bean.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.IRealMirrorAPI
 */
@Api(value = "集群", description = "集群API")
@RequestMapping(value = "/exception/indication/gateway")
public interface IGatewayAPI {

	 /**
     * 获取网关指标数据
     * @return
     */
    @RequestMapping(value = "listGatewayMerit", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    String getGatewayIndicationData(@RequestParam("indicationOwner") String indicationOwner, @RequestParam("catalogBox") String catalogBox,
                                    @RequestParam("indicationFrequency") String indicationFrequency, @RequestParam("group") String group);

    /**
     * 获取网关指标数据
     * @return
     */
    @RequestMapping(value = "saveGatewayMerit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "保存网关指标数据", notes = "保存网关指标数据", response = Result.class, tags = {
            "Indication Information API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "保存成功"),
            @ApiResponse(code = 500, message = "保存数据异常") })
    @CrossOrigin(origins = "*")
    Result saveGatewayMerit(@RequestBody List<IndicationLimitEntity> limitList);
    
}
