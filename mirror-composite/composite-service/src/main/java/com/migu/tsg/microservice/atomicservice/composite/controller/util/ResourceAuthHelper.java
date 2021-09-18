package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.ThreeValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

/**
 * 抽象鉴权和资源过滤工具类
 * Created by @author Liuyihua. on Y2K.
 */
public abstract class ResourceAuthHelper {

    //资源类型
//    public static final String AREA = "area";
//    public static final String ROOM = "room";
//    public static final String DEVICE_TYPE = "deviceType";
//    public static final String DEVICE = "device";
//    public static final String BIZ_SYSTEM = "bizSystem";
    
    public static final String AREA = "idcType";
    public static final String ROOM = "roomId";
    public static final String DEVICE_TYPE = "device_type";
    public static final String DEVICE = "device";
    public static final String BIZ_SYSTEM = "bizSystem";

    //自动化资源
    public static final String OPS_PIPELINE = "opsPipeline";

    public static final String OPS_SCRIPT = "opsScript";

    public static final String OPS_YUM = "opsYum";

    public static final String OPS_GROUP = "opsGroup";

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
    public abstract void resourceActionVerify(
            RequestAuthContext.RequestHeadUser user, CompositeResource resource,
            String resTypeAction, Map<String, String> flatConstraints);


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
    public abstract void resourceActionVerify(RequestHeadUser user, RbacResource rbacResource,
                                              String resTypeAction, Map<String, String> flatConstraints);

    /**
     * <p>
     * 增加一个混合接口，先验证功能操作权限，通过后再返回资源权限数据。
     * 验证功能操作权限的如果不通过，直接抛出无权限异常，不再返回资源权限数据
     * </p>
     *
     * @author 曾祥华
     * @version 0.1 2019年3月15日
     */
    public abstract Map<String, Set<String>> verifyActionAndGetResource(RequestAuthContext.RequestHeadUser user,
                                                                        String resTypeAction, Map<String, String> flatConstraints);


    public abstract Map<String, Set<String>> verifyGetResource(RequestHeadUser user);

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
    public abstract boolean pingActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource,
                                             String resTypeAction, Map<String, String> flatConstraints);

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
    public abstract List<String> resourceActions(RequestAuthContext.RequestHeadUser user,
                                                 String resourceType, Map<String, String> flatConstraints, RbacResource
                                                         rbacResource);


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
    public abstract List<CompAuthFilterResponse> resourceBulkActions(RequestAuthContext.RequestHeadUser user,
                                                                     String resourceType, Map<String, String> flatConstraints,
                                                                     List<RbacResource> rbacResourceList);

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
    public abstract Map<String, Set<String>> dataConstraints(RequestAuthContext.RequestHeadUser user);

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
    public abstract List<CompAuthFilterResponse> resourcesMixedFilter(RequestHeadUser user,
                                                                      List<RbacResource> rbacResList, Map<String, String> context);

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
    public abstract ThreeValue<List<CompositeResource>, List<CompAuthFilterResponse>, Map<String, Set<String>>>
    filterResourceList(RequestAuthContext.RequestHeadUser user, String resTypeAction, List<CompositeResource> resList2Filter,
                       Map<String, String> flattenConstraints);

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
    public abstract List<CompAuthFilterResponse> filterRbacResourceList(
            RequestAuthContext.RequestHeadUser user, String resTypeAction, List<RbacResource> rbacResList,
            Map<String, String> flattenConstraints);

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