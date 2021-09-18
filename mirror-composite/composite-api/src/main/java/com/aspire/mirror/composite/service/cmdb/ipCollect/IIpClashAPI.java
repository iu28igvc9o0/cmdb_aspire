package com.aspire.mirror.composite.service.cmdb.ipCollect;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashFindPageRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashResult;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 冲突IP接口
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/28 11:31
 */
@Api(value = "CMDB 冲突IP接口类")
@RequestMapping(value = "${version}/cmdb/ipClash")
public interface IIpClashAPI {

    @RequestMapping(value = "/findPageAndTotal", method = RequestMethod.POST)
    @ApiOperation(value = "查询列表及头栏统计接口", notes = "查询列表及头栏统计接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbIpClashResult findPage(@RequestBody CmdbIpClashFindPageRequest request);

    @RequestMapping(value = "/updateHandleStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更新处理状态接口", notes = "更新处理状态接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateHandleStatus(@RequestBody CmdbIpClashUpdateRequest request);

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation(value = "冲突IP列表-导出", notes = "冲突IP列表-导出", tags = {"CMDB ipClash API"})
    void export(@RequestBody CmdbIpClashFindPageRequest cmdbIpClashFindPageRequest, HttpServletResponse response);

}
