package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.AuthServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.AuthMixedFilterRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.CompAuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.CompAuthBulkActionsRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.CompAuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.ThreeValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 包含RBAC模块的资源权限工具类
 * Created by @author Liuyihua. on Y2K.
 */
@SuppressWarnings("all")
@Component
@ConditionalOnProperty(value = "systemType", havingValue = "normal")
public class NormalResourceAuthHelper extends ResourceAuthHelper {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public void resourceActionVerify(RequestAuthContext.RequestHeadUser user, CompositeResource resource, String resTypeAction, Map<String, String> flatConstraints) {
        this.resourceActionVerify(user, resource.getFlatten(), resTypeAction, flatConstraints);
    }

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

    @Override
    public Map<String, Set<String>> verifyActionAndGetResource(RequestAuthContext.RequestHeadUser user, String resTypeAction, Map<String, String> flatConstraints) {
        //验证功能操作权限
        //this.resourceActionVerify(user, new RbacResource(), resTypeAction, flatConstraints);
        //返回资源权限数据
        Map<String, Set<String>> dataConstraints = this.dataConstraints(user);
        return dataConstraints;
    }

    @Override
    public Map<String, Set<String>> verifyGetResource(RequestAuthContext.RequestHeadUser user) {
        //返回资源权限数据
        Map<String, Set<String>> dataConstraints = this.dataConstraints(user);
        return dataConstraints;
    }

    @Override
    public boolean pingActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource, String resTypeAction, Map<String, String> flatConstraints) {
        AuthVerifyRequest request = new AuthVerifyRequest();
        request.setNamespace(user.getNamespace());
        request.setUsername(user.getUsername());
        request.setIsAdmin(user.isAdmin());
        request.setAction(resTypeAction);
        request.setContext(flatConstraints);
        Map<String, String> authDto = (Map<String, String>) jacksonBaseParse(Map.class, rbacResource);
        request.setResource(authDto);
        AuthVerifyResponse authResult = authServiceClient.authVerify(request);
        if (authResult == null || !authResult.getOk()) {
            return false;
        }
        return true;
    }

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

    @Override
    public List<CompAuthFilterResponse> resourceBulkActions(RequestAuthContext.RequestHeadUser user, String resourceType, Map<String, String> flatConstraints, List<RbacResource> rbacResourceList) {
        CompAuthBulkActionsRequest request = new CompAuthBulkActionsRequest();
        request.setNamespace(user.getNamespace());
        request.setUsername(user.getUsername());
        request.setIsAdmin(user.isAdmin());
        request.setResourceType(resourceType);
        request.setContext(flatConstraints);
        rbacResourceList = rbacResourceList == null ? new ArrayList<RbacResource>() : rbacResourceList;
        request.setRabcResourceList(rbacResourceList);
        AuthActionsBulkRequest clientReq = jacksonBaseParse(AuthActionsBulkRequest.class, request);
        List<AuthActionsBulkResponse> resActionList = authServiceClient.authActionsBulk(clientReq);
        return jacksonBaseParse(CompAuthFilterResponse.class, resActionList);
    }

    @Override
    public Map<String, Set<String>> dataConstraints(RequestAuthContext.RequestHeadUser user) {
        Map<String, Set<String>> map = authServiceClient.dataConstraints(user.getUsername(), user.getNamespace(),
                Constants.Rbac.ROLE_TYPE_RESOURCE.toString());
        return map;
    }

    @Override
    public List<CompAuthFilterResponse> resourcesMixedFilter(RequestAuthContext.RequestHeadUser user, List<RbacResource> rbacResList, Map<String, String> context) {
        AuthMixedFilterRequest compReq = new AuthMixedFilterRequest();
        compReq.setNamespace(user.getNamespace());
        compReq.setUsername(user.getUsername());
        compReq.setIsAdmin(user.isAdmin());
        compReq.setResources(rbacResList);
        compReq.setContext(context);
        AuthFilterMixedRequest rbacReq = jacksonBaseParse(AuthFilterMixedRequest.class, compReq);
        List<AuthFilterMixedResponse> rabcResp = authServiceClient.authFilterMixed(rbacReq);
        return jacksonBaseParse(CompAuthFilterResponse.class, rabcResp);
    }

    @Override
    public ThreeValue<List<CompositeResource>, List<CompAuthFilterResponse>, Map<String, Set<String>>> filterResourceList(RequestAuthContext.RequestHeadUser user, String resTypeAction, List<CompositeResource> resList2Filter, Map<String, String> flattenConstraints) {
        if (resList2Filter == null || resList2Filter.isEmpty()) {
            List<CompositeResource> resList = new ArrayList<CompositeResource>();
            List<CompAuthFilterResponse> authList = new ArrayList<>();
            Map<String, Set<String>> map = new HashMap<>();
            return new ThreeValue<List<CompositeResource>, List<CompAuthFilterResponse>, Map<String, Set<String>>>
                    (resList, authList, map);
        }

        CompAuthFilterRequest compReq = new CompAuthFilterRequest();
        compReq.setNamespace(user.getNamespace());
        compReq.setUsername(user.getUsername());
        compReq.setIsAdmin(user.isAdmin());
        compReq.setResTypeAction(resTypeAction);
        compReq.setResources(CompositeResource.toFlattenList(resList2Filter));
        compReq.setContext(flattenConstraints);
        AuthFilterRequest rbacReq = jacksonBaseParse(AuthFilterRequest.class, compReq);
        for (Map<String, String> item : rbacReq.getResources()) {
            item.put("action", resTypeAction);
        }
        List<AuthFilterResponse> rbacFilterList = authServiceClient.authFilter(rbacReq);
        List<CompAuthFilterResponse> authList
                = jacksonBaseParse(CompAuthFilterResponse.class, rbacFilterList);

        List<CompositeResource> filterResult = new ArrayList<CompositeResource>();
//        for (CompositeResource comRes : resList2Filter) {
//            if (isResourceAuth(comRes, authList)) {
//                filterResult.add(comRes);
//            }
//        }
        Map<String, Set<String>> map = authServiceClient.dataConstraints(user.getUsername(), user.getNamespace(),
                "data");
        return new ThreeValue<List<CompositeResource>, List<CompAuthFilterResponse>, Map<String, Set<String>>>
                (filterResult, authList, map);
    }

    @Override
    public List<CompAuthFilterResponse> filterRbacResourceList(RequestAuthContext.RequestHeadUser user, String resTypeAction, List<RbacResource> rbacResList, Map<String, String> flattenConstraints) {
        if (rbacResList == null || rbacResList.isEmpty()) {
            return new ArrayList<CompAuthFilterResponse>();
        }

        CompAuthFilterRequest compReq = new CompAuthFilterRequest();
        compReq.setNamespace(user.getNamespace());
        compReq.setUsername(user.getUsername());
        compReq.setIsAdmin(user.isAdmin());
        compReq.setResTypeAction(resTypeAction);
        compReq.setResources(rbacResList);
        compReq.setContext(flattenConstraints);

        AuthFilterRequest rbacReq = jacksonBaseParse(AuthFilterRequest.class, compReq);
        List<AuthFilterResponse> rbacResp = authServiceClient.authFilter(rbacReq);
        return jacksonBaseParse(CompAuthFilterResponse.class, rbacResp);
    }
}
