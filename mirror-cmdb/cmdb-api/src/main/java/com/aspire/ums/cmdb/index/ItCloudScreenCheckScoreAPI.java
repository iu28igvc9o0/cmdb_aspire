package com.aspire.ums.cmdb.index;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenCheckScoreAPI
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/4/10 17:05
 * @Version 1.0
 */
@RequestMapping("/cmdb/index")
public interface ItCloudScreenCheckScoreAPI {

    /*
     *  获取租户扣分项列表
     * */
    @RequestMapping(value = "/score/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取租户扣分项列表", notes = "获取租户扣分项列表", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getCheckScoreList(@RequestBody ItCloudScreenRequest req);
}
