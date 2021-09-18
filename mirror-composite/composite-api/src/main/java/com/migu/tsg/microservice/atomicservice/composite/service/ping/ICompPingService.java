package com.migu.tsg.microservice.atomicservice.composite.service.ping;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.ping.payload.AddExecLogRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: msp-composite
 * @Description:获取ping信息
 * @author: YangShiLei
 * @create: 2018-04-16 15:26
 **/
public interface ICompPingService {

    @ApiOperation(value = "获取ping值", notes = "获取ping值",
            response = String.class, tags = {"Composite Ping service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "composite:With ice and fire!",
            response = String.class)})
    @GetMapping(path = "/ping")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> getPingInfo ();

    @ApiOperation(value = "获取innerPing值", notes = "获取innerPing值",
            response = String.class, tags = {"Composite Ping service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "composite:With ice and fire!",
            response = String.class)})
    @GetMapping(path = "/_ping")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> getInnerPingInfo();

    /**
     *
     * @param accessibleRequest
     * @return
     */
    @ApiOperation(value = "应用或服务是否可以访问", notes = "应用或服务是否可以访问",
            response = String.class, tags = {"Composite Ping service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "is accessible",
            response = String.class)})
    @GetMapping(path = "${version}/is_accessible")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> isAccessible(@RequestParam(value = "username")String username,
                                        @RequestParam("privilege")String privilege,
                                        @RequestParam("namespace")String namespace,
                                        @RequestParam(value ="application",required = false)String application,
                                        @RequestParam("res_name")String resName,
                                        @RequestParam(value="project",required = false)String project);

    /**
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "回调添加事件记录", notes = "回调添加事件记录", tags = {"Composite Ping service API"})
    @ApiResponses(value = {@ApiResponse(code = 201,message = "add exec log success")})
    @PostMapping(path = "${version}/callback/add_exec_log", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    BaseResponse addExeclog (@RequestBody AddExecLogRequest request);
}
