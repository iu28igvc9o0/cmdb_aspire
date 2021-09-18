package com.aspire.mirror.ops.clientservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.ops.api.domain.GetRoleDetailResponse;
import com.aspire.mirror.ops.api.domain.RolesResponse;

import io.swagger.annotations.ApiOperation;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.clientservice
 * 类名称:    RbacClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/2/23 15:15
 * 版本:      v1.0
 */
@FeignClient(value = "rbac", url = "http://10.1.203.100:28101")
public interface RbacClient {
    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称(空间名称),通过相关的用户名过滤角色列表
     * @return 响应对象
     */
    @ApiOperation("查询角色列表")
    @GetMapping(value = "/v1/roles/instances", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RolesResponse> listRoles(@RequestParam("uuids") List<String> uuids,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "namespace", required = false) final String namespace,
                                         @RequestParam(value = "roleType", required = false) final Integer roleType,
                                         @RequestParam(value = "username", required = false) final String username);

    @GetMapping(value = "/v1/roles/instances/{role_uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetRoleDetailResponse getRoleDetail(@PathVariable("role_uuid") String roleUuid);
}
