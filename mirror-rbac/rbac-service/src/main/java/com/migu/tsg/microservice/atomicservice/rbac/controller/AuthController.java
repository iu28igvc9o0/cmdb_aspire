package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListActionsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AuthResourceActionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.AuthService;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: AuthController.java <br>
 * 类描述: 所有验证和授权相关方法控制层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月17日下午3:57:14 <br>
 * 版本: v1.0
 */
@RestController
public class AuthController implements AuthService {

    @Autowired
    private AuthBiz authBiz;

    /**
     * 验证给定范围和资源属性是否可以执行特定操作
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105010101")
    public AuthVerifyResponse authVerify(@RequestBody final AuthVerifyRequest request) {
        return authBiz.authVerify(request.getUsername(), request.getNamespace(), request.getIsAdmin(),
                request.getResource(), request.getAction(), request.getContext())
                ? new AuthVerifyResponse(true)
                : new AuthVerifyResponse(false);
    }

    /**
     * 过滤资源列表,返回用户操作允许的列表数据,以及资源操作集合
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105010102")
    public List<AuthFilterResponse> authFilter(@RequestBody final AuthFilterRequest request) {
        List<AuthResourceActionDTO> dto = authBiz.authFilter(request.getUsername(), request.getNamespace(),
                request.getIsAdmin(), request.getAction(), request.getResources(), request.getContext());
        List<AuthFilterResponse> reponse = dto.stream().map(authFilterDTO -> {
            AuthFilterResponse response = new AuthFilterResponse();
            BeanUtils.copyProperties(authFilterDTO, response);
            return response;
        }).collect(Collectors.toList());
        return reponse;
    }

    /**
     * 获取给定范围允许资源操作的列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105010103")
    public List<String> authActions(@RequestBody final AuthActionsRequest request) {
        return authBiz.authActions(request.getUsername(), request.getNamespace(), request.getIsAdmin(),
                request.getResourceType(), request.getContext(), request.getResource());
    }

    /**
     * 批量添加资源操作的资源列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105010104")
    public List<AuthActionsBulkResponse> authActionsBulk(@RequestBody final AuthActionsBulkRequest request) {
        List<AuthResourceActionDTO> result = authBiz.authActionsBulk(request.getUsername(),
                request.getNamespace(), request.getIsAdmin(), request.getResourceType(), request.getContext(),
                request.getResources());
        return result.stream().map(dto -> {
            AuthActionsBulkResponse response = new AuthActionsBulkResponse();
            BeanUtils.copyProperties(dto, response);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 筛选混合资源类型和操作的列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105010105")
    public List<AuthFilterMixedResponse> authFilterMixed(@RequestBody final AuthFilterMixedRequest request) {
        List<AuthResourceActionDTO> result = authBiz.authFilterMixed(request.getUsername(),
                request.getNamespace(), request.getIsAdmin(), request.getContext(), request.getResources());
        return result.stream().map(dto -> {
            AuthFilterMixedResponse response = new AuthFilterMixedResponse();
            BeanUtils.copyProperties(dto, response);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 获取有权限的资源操作列表
     * @param username 成员名称
     * @param namespace 空间名称/根账号
     * @param isAdmin 是否为管理员
     * @return 响应对象
     */
    @ResultCode("105010106")
    public List<ListActionsResponse> listActions(@PathVariable("username") final String username,
            @PathVariable("namespace") final String namespace,
            @RequestParam(value = "is_admin", defaultValue = "false") final Boolean isAdmin) {
        List<ResourceSchema> listActions = authBiz.listActions(username, namespace, isAdmin);
        return listActions.stream().map(resourceSchema -> {
            ListActionsResponse response = new ListActionsResponse();
            response.setResource(resourceSchema.getResource());
            response.setActions(resourceSchema.getActions().stream().map(ResourceSchemaActions::getAction)
                    .collect(Collectors.toList()));
            return response;
        }).collect(Collectors.toList());
    }

    /**
     *  根据用户查询权限列表
     * 
     */
    @ResultCode("105010107")
	public Map<String, Set<String>> dataConstraints(@PathVariable("username") final String username,
            @PathVariable("namespace") final String namespace,
            @RequestParam(value = "roleType",required=false) final String roleType) {
		return authBiz.dataConstraints(username, namespace, roleType);
	}

}
