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
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;


/**
 * 鉴权和资源过滤工具类
 * Project Name:composite-service
 * File Name:ResourceAuthHelper.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.util
 * ClassName: ResourceAuthHelper <br/>
 * date: 2017年8月28日 下午5:27:27 <br/>
 * 鉴权和资源过滤工具类
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Deprecated
@Component
public class ResourceAuthHelper2 {


    //资源类型
    public static final String AREA = "area";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String DEVICE = "device";

    public static final String BIZ_SYSTEM = "bizSystem";

    @Autowired
    private AuthServiceClient authServiceClient;

    /**
     * 验证用户是否有资源的操作权限,如果没有操作权限，直接抛出异常.<br/>
     * <p>
     * 作者： pengguihua
     *
     * @param user
     * @param resource
     * @param resTypeAction
     * @param flatConstraints 扁平化约束(res:cluster=xxx)
     */
    public void resourceActionVerify(
            RequestHeadUser user, CompositeResource resource,
            String resTypeAction, Map<String, String> flatConstraints) {
        this.resourceActionVerify(user, resource.getFlatten(), resTypeAction, flatConstraints);
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
    @SuppressWarnings("unchecked")
    public void resourceActionVerify(
            RequestHeadUser user, RbacResource rbacResource,
            String resTypeAction, Map<String, String> flatConstraints) {
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
     * @author 曾祥华
     * @version 0.1 2019年3月15日
     */
    public Map<String, Set<String>> verifyActionAndGetResource(
            RequestHeadUser user, String resTypeAction, Map<String, String> flatConstraints) {
        //验证功能操作权限
        this.resourceActionVerify(user, new RbacResource(), resTypeAction, flatConstraints);
        //返回资源权限数据
        Map<String, Set<String>> dataConstraints = this.dataConstraints(user);
        return dataConstraints;
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
    @SuppressWarnings("unchecked")
    public boolean pingActionVerify(
            RequestHeadUser user, RbacResource rbacResource,
            String resTypeAction, Map<String, String> flatConstraints) {
        AuthVerifyRequest request = new AuthVerifyRequest();
        request.setNamespace(user.getNamespace());
        request.setUsername(user.getUsername());
        request.setIsAdmin(user.isAdmin());
        request.setAction(resTypeAction);
        request.setContext(flatConstraints);
        Map<String, String> authDto = (Map<String, String>) jacksonBaseParse(Map.class, rbacResource);
        request.setResource(authDto);
        AuthVerifyResponse authResult = authServiceClient.authVerify(request);
        return authResult != null && authResult.getOk();
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
    public List<String> resourceActions(RequestHeadUser user,
                                        String resourceType, Map<String, String> flatConstraints, RbacResource
                                                rbacResource) {
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


    /**
     * 批量资源获取actions.<br/>
     * <p>
     * 作者： pengguihua
     *
     * @param user
     * @param resourceType
     * @param flatConstraints  扁平化约束(res:cluster=xxx)
     * @param rbacResourceList
     * @return
     */
    public List<CompAuthFilterResponse> resourceBulkActions(RequestHeadUser user,
                                                            String resourceType, Map<String, String> flatConstraints,
                                                            List<RbacResource> rbacResourceList) {
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

    /**
     * 查询数据资源过滤约束
     * <p>
     * Map<String,String[]> 格式
     * 其中：
     * key：约束的维度，例如：区域area是一个维度，设备类型devicetype是一个维度
     * value:所属约束维度的值
     *
     * @param user
     * @return
     */
    public Map<String, Set<String>> dataConstraints(RequestHeadUser user) {
        Map<String, Set<String>> map = authServiceClient.dataConstraints(user.getUsername(), user.getNamespace(),
                Constants.Rbac.ROLE_TYPE_RESOURCE.toString());
        return map;
    }

    /**
     * proceeds to filter the given resources. an 'action' key is expected in each item <br/>
     * 返回格式：
     * [
     * {
     * "resource": {
     * "name": "my-web",
     * "uuid": "123-321-123",
     * "res:space": "space"
     * },
     * "actions": ["service:update", "service:view"]
     * }
     * ]
     * 作者： pengguihua
     *
     * @param user
     * @param rbacResList list of resources. each item should contain name, uuid and the list of constrains. should
     *                    be one-level dict
     * @param context     扁平化约束(res:cluster=xxx)
     * @return the already filtered list of resources with the following format:
     * {'resource': {resource data}, 'resource_actions': [list of possible actions]}
     */
    public List<CompAuthFilterResponse> resourcesMixedFilter(
            RequestHeadUser user, List<RbacResource> rbacResList, Map<String, String> context) {
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

    /**
     * proceeds to filter the given resources using the related action. the constraints should be
     * embed in each resource item.<br/>
     * <p>
     * 返回格式：
     * [
     * {
     * "resource": {
     * "name": "my-web",
     * "uuid": "123-321-123",
     * "res:space": "space"
     * },
     * "actions": ["service:update", "service:view"]
     * }
     * ]
     * <p>
     * 作者： pengguihua
     *
     * @return the already filtered list of resources with the following format:
     * {'resource': {resource data}, 'resource_actions': [list of possible actions]}
     */
    public ThreeValue<List<CompositeResource>, List<CompAuthFilterResponse>, Map<String, Set<String>>>
    filterResourceList(
            RequestHeadUser user, String resTypeAction, List<CompositeResource> resList2Filter,
            Map<String, String> flattenConstraints) {
//
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

    /**
     * proceeds to filter the given rbac resources using the related action. the constraints should be
     * embed in each resource item.<br/>. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param user
     * @param resTypeAction
     * @param rbacResList
     * @param flattenConstraints
     * @return
     */
    public List<CompAuthFilterResponse> filterRbacResourceList(
            RequestHeadUser user, String resTypeAction, List<RbacResource> rbacResList,
            Map<String, String> flattenConstraints) {

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

    /**
     * 判断指定的资源是否存在于鉴权过滤列表中.<br/>
     * <p>
     * 作者： pengguihua
     *
     * @param comRes
     * @param authResList
     * @return
     */
    private boolean isResourceAuth(CompositeResource comRes, final List<CompAuthFilterResponse> authResList) {
        for (CompAuthFilterResponse authItem : authResList) {
            RbacResource rbacRes = authItem.getResource();
            if (rbacRes == null) {
                continue;
            }

            String uuid = rbacRes.getUuid();
            if (StringUtils.isNotBlank(uuid) && uuid.equals(comRes.getUuid())) {
                return true;
            }

            if (StringUtils.isNotBlank(comRes.getUuid())) {
                continue;
            }

            String name = rbacRes.getName();
            if (StringUtils.isNotBlank(name) && name.equals(comRes.getName())) {
                return true;
            }
        }
        return false;
    }
}
