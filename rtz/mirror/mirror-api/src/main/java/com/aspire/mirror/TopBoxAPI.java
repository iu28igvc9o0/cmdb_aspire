package com.aspire.mirror;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.IRealMirrorAPI
 */
@Api(value = "机顶盒", description = "机顶盒API")
@RequestMapping(value = "/topBox")
public interface TopBoxAPI {

    /**
     * 获取机顶盒指标数据
     * @return
     */
    @RequestMapping(value = "/getIndicationData", method = RequestMethod.GET)
    @ApiOperation(value = "获取机顶盒指标数据", notes = "获取机顶盒指标数据", response = Object.class, tags = {
                  "Top Bpx API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getIndicationData(@RequestParam(value = "indicationOwner") String indicationOwner,
                             @RequestParam(value = "day") String day);

    /**
     * 更新机顶盒指标数据
     * @return
     */
    @RequestMapping(value = "/updateIndicationData", method = RequestMethod.POST)
    @ApiOperation(value = "更新机顶盒指标数据", notes = "更新机顶盒指标数据", response = Object.class, tags = {
            "Top Bpx API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "修改成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object updateIndicationData(@RequestBody Map<String, Object> param);

    /**
     * 手动发送邮件
     * @return
     */
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    @ApiOperation(value = "手动发送邮件", notes = "手动发送邮件", response = Object.class, tags = {
            "Top Bpx API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "邮件发送成功"),
            @ApiResponse(code = 500, message = "邮件发送出现异常") })
    Object sendEmail(@RequestBody Map<String, Object> param);

    /**
     * 手动发送邮件
     * @return
     */
    @RequestMapping(value = "/autoSendEmail", method = RequestMethod.GET)
    @ApiOperation(value = "自动发送邮件", notes = "自动发送邮件", response = Object.class, tags = {
            "Top Bpx API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "邮件发送成功"),
            @ApiResponse(code = 500, message = "邮件发送出现异常") })
    Object autoSendEmail(@RequestParam String indicationOwner, @RequestParam String dateTime);

}
