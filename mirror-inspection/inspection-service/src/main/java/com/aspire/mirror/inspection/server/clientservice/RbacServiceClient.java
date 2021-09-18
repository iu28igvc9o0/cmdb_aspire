package com.aspire.mirror.inspection.server.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.aspire.mirror.inspection.server.clientservice.payload.GetOrgUserDetailResponse;

@FeignClient(value = "rbac", url = "http://10.1.203.100:28101")
public interface RbacServiceClient {
	
	/**
     * 查询根账号(组织/公司)中单个成员详细信息
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     * @return 响应对象
     */
    @GetMapping(value = "/v1/orgs/{org_name}/accounts/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetOrgUserDetailResponse getOrgUserDetail(@PathVariable("org_name") String namespace,
            @PathVariable("username") String username);
}


