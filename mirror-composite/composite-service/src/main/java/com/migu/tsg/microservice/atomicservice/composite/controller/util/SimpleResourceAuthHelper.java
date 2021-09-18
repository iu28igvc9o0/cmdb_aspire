package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.AuthServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.AuthMixedFilterRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.ThreeValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 去除了RBAC模块的资源权限工具类
 * Created by @author Liuyihua. on Y2K.
 */
@Component
@ConditionalOnProperty(value = "systemType", havingValue = "simple")
public class SimpleResourceAuthHelper extends ResourceAuthHelper {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public void resourceActionVerify(RequestAuthContext.RequestHeadUser user, CompositeResource resource, String resTypeAction, Map<String, String> flatConstraints) {
        this.resourceActionVerify(user, resource.getFlatten(), resTypeAction, flatConstraints);
    }

    @Override
    public void resourceActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource, String resTypeAction, Map<String, String> flatConstraints) {

    }

    @Override
    public Map<String, Set<String>> verifyActionAndGetResource(RequestAuthContext.RequestHeadUser user, String resTypeAction, Map<String, String> flatConstraints) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Set<String>> verifyGetResource(RequestAuthContext.RequestHeadUser user) {
        return new HashMap<>();
    }

    @Override
    public boolean pingActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource, String resTypeAction, Map<String, String> flatConstraints) {
        return true;
    }

    @Override
    public List<String> resourceActions(RequestAuthContext.RequestHeadUser user, String resourceType, Map<String, String> flatConstraints, RbacResource rbacResource) {
        return new ArrayList<>();
    }

    @Override
    public List<CompAuthFilterResponse> resourceBulkActions(RequestAuthContext.RequestHeadUser user, String resourceType, Map<String, String> flatConstraints, List<RbacResource> rbacResourceList) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Set<String>> dataConstraints(RequestAuthContext.RequestHeadUser user) {
        Map<String, Set<String>> map = authServiceClient.dataConstraints(user.getUsername(), user.getNamespace(), Constants.Rbac.ROLE_TYPE_RESOURCE.toString());
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
        return null;
    }

    @Override
    public List<CompAuthFilterResponse> filterRbacResourceList(RequestAuthContext.RequestHeadUser user, String resTypeAction, List<RbacResource> rbacResList, Map<String, String> flattenConstraints) {
        return null;
    }
}
