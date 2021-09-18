package com.migu.tsg.microservice.atomicservice.composite.helper;
import com.migu.tsg.microservice.atomicservice.composite.biz.AuthBiz;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽象鉴权和资源过滤工具类
 * Created by @author Liuyihua. on Y2K.
 */
@Component
public class ResourceAuthV2Helper {

    @Autowired
    private List<AuthBiz> authBizList;

    //资源类型
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
    public void resourceActionVerify(RequestHeadUser user, RbacResource rbacResource,
                                              String resTypeAction, Map<String, String> flatConstraints) {
        for (AuthBiz authBiz: authBizList) {
            if (!authBiz.isBasic()) {
                authBiz.resourceActionVerify(user, rbacResource, resTypeAction, flatConstraints);
                break;
            }
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
    public Map<String, Set<String>> verifyActionAndGetResource(RequestHeadUser user,
                                                                        String resTypeAction, Map<String, String> flatConstraints) {
        for (AuthBiz authBiz: authBizList) {
            if (!authBiz.isBasic()) {
                return authBiz.verifyActionAndGetResource(user, resTypeAction, flatConstraints);
            }
        }
        return authBizList.get(0).verifyActionAndGetResource(user, resTypeAction, flatConstraints);
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
        for (AuthBiz authBiz: authBizList) {
            if (!authBiz.isBasic()) {
                return authBiz.resourceActions(user, resourceType, flatConstraints, rbacResource);
            }
        }
        return authBizList.get(0).resourceActions(user, resourceType, flatConstraints, rbacResource);
    }

    /**
     * 用户拥有的资源view权限展示
     * @param namespace
     * @param username
     * @param isAdmin
     * @return
     */
    public List<String> listUserActions(String namespace, String username, Boolean isAdmin) {
        for (AuthBiz authBiz: authBizList) {
            if (!authBiz.isBasic()) {
                return authBiz.listUserActions(namespace, username, isAdmin);
            }
        }
        return authBizList.get(0).listUserActions(namespace, username, isAdmin);
    }
}