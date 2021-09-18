package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author baiwenping
 * @Title: AuthBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz
 * @Description: TODO
 * @date 2021/3/16 15:04
 */
public interface AuthBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    /**
     * 用户拥有的资源view权限展示
     * @param namespace
     * @param username
     * @param isAdmin
     * @return
     */
    List<String> listUserActions(String namespace, String username, Boolean isAdmin);

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
    void resourceActionVerify(RequestAuthContext.RequestHeadUser user, RbacResource rbacResource,
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
    Map<String, Set<String>> verifyActionAndGetResource(RequestAuthContext.RequestHeadUser user,
                                                               String resTypeAction, Map<String, String> flatConstraints);

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
    Map<String, Set<String>> dataConstraints(RequestAuthContext.RequestHeadUser user);

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
    List<String> resourceActions(RequestAuthContext.RequestHeadUser user,
                                        String resourceType, Map<String, String> flatConstraints, RbacResource
                                                rbacResource);
}
