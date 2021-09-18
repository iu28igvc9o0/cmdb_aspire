package com.aspire.ums.cmdb.sync.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@FeignClient(value = "rbac", url = "http://10.1.203.100:28101")
public interface RbacServiceClient {

    /**
     * 创建信息
     *
     * @param userBatchCreateRequest 用户批量创建请求对象
     * @return UserCreateResponse 用户创建响应对象
     */
    @PostMapping(value = "/v1/user/batchInsert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = UserCreateResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = UserCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserCreateResponse.class)})
    int batchCreatedUser(@RequestBody UserBatchCreateRequest userBatchCreateRequest);

//    @PutMapping(value = "/v1/user/batchModify", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "修改", notes = "修改", response = UserUpdateResponse.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "返回", response = UserUpdateResponse.class),
//            @ApiResponse(code = 500, message = "Unexpected error", response = UserUpdateResponse.class)})
//    int modifyArrayByCode(@RequestBody
//            List<UserUpdateRequest> userUpdateRequest);
}


