package com.aspire.mirror.composite.service.cmdb.icon;

import com.aspire.ums.cmdb.icon.payload.CmdbIcon;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("${version}/cmdb/icon")
public interface ICmdbIconAPI {
    /**
     * 获取图片
     */
    @RequestMapping(value = "/getIcons", method = RequestMethod.POST)
    @ApiOperation(value = "获取图片列表", notes = "获取图片列表", tags = {"CMDB Icon API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getIcon(@RequestBody(required = false) CmdbIcon icon, @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
                   @RequestParam(value = "pageSize", required = true) Integer pageSize);

    /**
     * 上传图片
     */
    @RequestMapping(value = "/uploadIcon", method = RequestMethod.POST)
    @ApiOperation(value = "上传图片", notes = "上传图片", tags = {"CMDB Icon API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> uploadIcon(HttpServletRequest request);
}
