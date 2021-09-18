package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.biz.AuthBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.AuthServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.CompAuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserViewAction;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListActionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * @author baiwenping
 * @Title: AuthRbacBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 15:10
 */
@Component
public class AuthRbacBizImpl implements AuthBiz {
    @Autowired
    private AuthServiceClient authServiceClient;

    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * 用户拥有的资源view权限展示
     *
     * @param namespace
     * @param username
     * @param isAdmin
     * @return
     */
    @Override
    public List<String> listUserActions(String namespace, String username, Boolean isAdmin) {
        List<String> userActions = new ArrayList<String>();
        List<ListActionsResponse> listActions = authServiceClient.listActions(username, namespace, isAdmin);
        if (null != listActions) {
            // 转化成封装层的数据类型UserViewAction
            List<UserViewAction> listUserViewActions = listActions.stream().map(item -> {
                UserViewAction userViewAction = new UserViewAction();
                userViewAction.setActions(item.getActions());
                userViewAction.setResource(item.getResource());
                return userViewAction;
            }).collect(Collectors.toList());
            // 数据处理，拿出有view权限的资源类型
            List<String> actions;
            for (UserViewAction userPermission : listUserViewActions) {
                actions = userPermission.getActions();
                if (actions.contains("*:view")) {
                    userActions.add("*");
                    break;
                } else {
                    for (String item : actions) {
                        if (item != null && !listActions.contains(item)) {
                            userActions.add(item);
                        }
                    }
                }
            }
            if (userActions.contains("*")) {
                List<String> list = new ArrayList<String>();
                list.add("*");
                return list;
            } else {
                return userActions;
            }
        } else {
            return userActions;
        }
    }

    /**
     * 验证用户是否有资源的操作权限,如果没有操作权限，直接抛出异常.<br/>
     * <p>
     * 作者： pengguihua
     *
     * @param user
     * @param rbacResource
     * @param resTypeAction
     * @param flatConstraints 扁平化约束(res:cluster=xxx)
     */
    @Override
    public void resourceActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource, String resTypeAction, Map<String, String> flatConstraints) {
        AuthVerifyRequest request = new AuthVerifyRequest();
        request.setNamespace(user.getNamespace());
        request.setUsername(user.getUsername());
        request.setIsAdmin(user.isAdmin());
        request.setAction(resTypeAction);
        request.setContext(flatConstraints);

        Map<String, String> authDto = Maps.newHashMap();
        authDto.put("action", resTypeAction);
        request.setResource(authDto);
        AuthVerifyResponse authResult = authServiceClient.authVerify(request);
        if (authResult == null || !authResult.getOk()) {
            throw new ResourceActionAuthException();
        }
    }

    /**
     * <p>
     * 增加一个混合接口，先验证功能操作权限，通过后再返回资源权限数据。
     * 验证功能操作权限的如果不通过，直接抛出无权限异常，不再返回资源权限数据
     * </p>
     *
     * @param user
     * @param resTypeAction
     * @param flatConstraints
     * @author 曾祥华
     * @version 0.1 2019年3月15日
     */
    @Override
    public Map<String, Set<String>> verifyActionAndGetResource(RequestAuthContext.RequestHeadUser user, String resTypeAction, Map<String, String> flatConstraints) {
        //返回资源权限数据
        Map<String, Set<String>> dataConstraints = this.dataConstraints(user);
        return dataConstraints;
    }

    @Override
    public Map<String, Set<String>> dataConstraints(RequestAuthContext.RequestHeadUser user) {
        Map<String, Set<String>> map = authServiceClient.dataConstraints(user.getUsername(), user.getNamespace(),
                Constants.Rbac.ROLE_TYPE_RESOURCE.toString());
        return map;
    }

    /**
     * resourceActions: 返回给定约束或给定资源的有权限的操作. <br/>
     * 请求RABC服务    /auth/actions
     * <p>
     * 作者： pengguihua
     *
     * @param user
     * @param resourceType
     * @param flatConstraints 扁平化约束(res:cluster=xxx)
     * @param rbacResource
     * @return
     */
    @Override
    public List<String> resourceActions(RequestAuthContext.RequestHeadUser user, String resourceType, Map<String, String> flatConstraints, RbacResource rbacResource) {
        CompAuthActionsRequest compReq = new CompAuthActionsRequest();
        compReq.setNamespace(user.getNamespace());
        compReq.setUsername(user.getUsername());
        compReq.setIsAdmin(user.isAdmin());
        compReq.setResourceType(resourceType);
        compReq.setContext(flatConstraints);
        rbacResource = rbacResource == null ? new RbacResource() : rbacResource;
        compReq.setRabcResource(rbacResource);
        AuthActionsRequest rbacReq = jacksonBaseParse(AuthActionsRequest.class, compReq);
        return authServiceClient.authActions(rbacReq);
    }
}
