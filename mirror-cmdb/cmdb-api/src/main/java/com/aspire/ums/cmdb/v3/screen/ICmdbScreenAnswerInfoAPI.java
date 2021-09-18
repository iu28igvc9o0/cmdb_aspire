package com.aspire.ums.cmdb.v3.screen;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @projectName: CmdbScreenAnswerInfoAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-07-03 13:35
 **/
@RequestMapping("/v3/cmdb/screen/answer")
public interface ICmdbScreenAnswerInfoAPI {

    /*
    *  添加回答
    * */
    @PostMapping("/save")
    @ApiOperation(value = "添加回答", notes = "添加回答", tags = {"Cmdb Screen Answer API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> save(@RequestBody CmdbScreenAnswerInfo req,
                            @RequestParam("isAdmin") Boolean isAdmin);
}
