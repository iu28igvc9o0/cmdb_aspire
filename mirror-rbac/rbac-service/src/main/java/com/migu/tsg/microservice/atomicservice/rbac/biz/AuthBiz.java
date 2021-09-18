package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.DocumentContext;
import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.util.JsonUtil;
import com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaConstraints;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AuthResourceActionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;

import net.sf.json.JSONArray;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.biz <br>
 * 类名称: AuthBiz.java <br>
 * 类描述: 所有验证和授权相关方法业务层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月31日下午1:55:36 <br>
 * 版本: v1.0
 */
@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AuthBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthBiz.class);

    @Autowired
    private CacheBiz cacheBiz;

    /**
     * payload中的资源对象名称的KEY
     */
    private static final String PAYLOAD_RESOURCE_NAME_KEY = "name";

    /**
     * payload中的资源对象UUID的KEY
     */
    private static final String PAYLOAD_RESOURCE_UUID_KEY = "uuid";

    /**
     * payload中的资源对象action的KEY
     */
    private static final String PAYLOAD_RESOURCE_ACTION_KEY = "action";

    /**
     * 资源操作中的*和资源名称或UUID中的*
     */
    private static final String RESOURCE_ACTION_REPLACE_TARGET = "*";

    /**
     * 资源操作中的*替换成.+
     */
    private static final String ACTION_REPLACE_REPLACEMENT = ".+";

    /**
     * 资源名称或UUID中的*替换成.+
     */
    private static final String RESOURCE_REPLACE_REPLACEMENT = ".+";

    /**
     * 验证给定资源名称和资源约束是否可以执行特定资源操作
     *
     * @param username  成员名称
     * @param namespace 空间名称
     * @param isAdmin   是否为管理员
     * @param resource  资源名称
     * @param action    资源操作
     * @param context   资源约束
     * @return dto对象
     */
    public boolean authVerify(final String username, final String namespace, final Boolean isAdmin,
                              final Map<String, String> resource, final String action, final Map<String, String>
                                      context) {
        validateParameterUsernameAndNamespace(username, namespace);
        // 若是根账号则返回true,默认有全部权限
        if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
            LOGGER.info("method[authVerify] The {} of {} authVerify success", username, namespace);
            return true;
        }
        validateParameterAction(action);
//        ResourceSchema resourceSchema = getResourceSchema(action.split(":")[0]);
//        // 获取资源操作集合
//        List<String> actionList = listOfActions(resourceSchema);
//        // 获取资源约束Key集合
//        Set<String> constKeySet = listOfconstKey(resourceSchema);
        List<Permission> listOfPermissionsActions = listOfPermissionsActions(username, namespace, context,
                action);
        LOGGER.info("method[authVerify] The {} of {} authVerify success", username, namespace);
        return CollectionUtils.isNotEmpty(listOfPermissionsActions);
    }

    /**
     * 过滤资源列表,返回用户操作允许的列表数据,以及资源操作集合
     *
     * @param username  成员名称
     * @param namespace 空间名称/根账号
     * @param isAdmin   是否为管理员
     * @param action    资源操作
     * @param resources 资源集合
     * @param context   资源约束
     * @return 用户操作允许的列表数据, 以及资源操作集合
     */
    public List<AuthResourceActionDTO> authFilter(final String username, final String namespace,
                                                  final Boolean isAdmin, final String action, final List<Map<String,
            String>> resources,
                                                  final Map<String, String> context) {
        validateParameterUsernameAndNamespace(username, namespace);
        validateParameterAction(action);
        List<Map<String, String>> listOfResource = validateListOfResource(resources);
//        ResourceSchema resourceSchema = getResourceSchema(action.split(":")[0]);
//        // 获取资源操作集合
        List<String> actionList = Lists.newArrayList();
        actionList.add(action);
        List<AuthResourceActionDTO> araDtoList = new ArrayList<>();
        // 若是根账号则默认有全部权限
        if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
            listOfResource.forEach(resource -> {
                araDtoList.add(new AuthResourceActionDTO(actionList, resource));
            });
            LOGGER.info("method[authFilter] The {} of {} authFilter success", username, namespace);
            return araDtoList;
        }
        // 获取资源约束Key集合
//        Set<String> constKeySet = listOfconstKey(resourceSchema);
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
        if (CollectionUtils.isEmpty(permissionList)) {
            LOGGER.info("method[authFilter] The {} of {} authFilter success", username, namespace);
            return araDtoList;
        }
        listOfResource.forEach(resource -> {
            List<Permission> listOfPermissionsActions = listOfPermissionsActions(username, namespace, context,
                    action);
            if (CollectionUtils.isNotEmpty(listOfPermissionsActions)) {
//                araDtoList.add(new AuthResourceActionDTO(listOfPermissionsActions, resource));
                araDtoList.add(new AuthResourceActionDTO(actionList, resource));
            }
        });
        LOGGER.info("method[authFilter] The {} of {} authFilter success", username, namespace);
        return araDtoList;
    }

    /**
     * 获取数据资源约束
     *
     * @param username
     * @param namespace
     * @param roleType
     * @return
     */
    public Map<String, Set<String>> dataConstraints(final String username, final String namespace, final String
            roleType) {
        Map<String, Set<String>> returnMap = new HashMap<>();
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace, roleType);
        if (CollectionUtils.isEmpty(permissionList)) {
            LOGGER.info("method[dataConstraints] The {} of {} {}  authFilter success", username, namespace, roleType);
            return returnMap;
        }

        permissionList.stream().forEach(permission -> {
            DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(permission.getConstraints());
            List<Map<String, String>> cons = jsonCtx.read("$");
            cons.stream().forEach(con -> {
                con.entrySet().stream().forEach(item -> {
                    if (StringUtils.isNotEmpty(item.getValue())) {
                        Set<String> itemSet = null;
                        String key = item.getKey();
                        if (!returnMap.containsKey(key)) {
                            itemSet = new HashSet<String>();
                        } else {
                            itemSet = returnMap.get(key);
                        }
                        itemSet.addAll(new HashSet<>(Arrays.asList(item.getValue().split(","))));
                        returnMap.put(key, itemSet);
                    }
                });
            });
        });

        LOGGER.info("method[authFilter] The {} of {} authFilter success", username, namespace);
        return returnMap;
    }

    /**
     * 获取给定范围允许资源操作的列表
     *
     * @param username     成员名称
     * @param namespace    空间名称/根账号
     * @param isAdmin      是否为管理员
     * @param resourceType 资源类型
     * @param context      资源约束
     * @param resource     资源
     * @return 给定范围允许资源操作的列表
     */
    public List<String> authActions(final String username, final String namespace, final Boolean isAdmin,
                                    final String resourceType, final Map<String, String> context,
                                    final Map<String, String> resource) {
        validateParameterUsernameAndNamespace(username, namespace);
        ResourceSchema resourceSchema = getResourceSchema(resourceType);
        // 获取资源操作集合
        List<String> actionList = listOfActions(resourceSchema);
        // 若是根账号则默认有全部权限
        if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
            LOGGER.info("method[authActions] The {} of {} authActions success", username, namespace);
            return actionList;
        }
        // 获取资源约束Key集合
        Set<String> constKeySet = listOfconstKey(resourceSchema);
        List<String> listOfPermissionsActions = listOfPermissionsActions(username, namespace, context,
                resource, actionList, constKeySet);
        LOGGER.info("method[authActions] The {} of {} authActions success", username, namespace);
        return listOfPermissionsActions;
    }

    /**
     * 批量添加资源操作的资源列表
     *
     * @param username     成员名称
     * @param namespace    空间名称/根账号
     * @param isAdmin      是否为管理员
     * @param resourceType 资源类型
     * @param context      资源约束
     * @param resources    资源集合
     * @return 资源操作的资源列表
     */
    public List<AuthResourceActionDTO> authActionsBulk(final String username, final String namespace,
                                                       final Boolean isAdmin, final String resourceType, final
                                                       Map<String, String> context,
                                                       final List<Map<String, String>> resources) {
        validateParameterUsernameAndNamespace(username, namespace);
        List<Map<String, String>> listOfResource = validateListOfResource(resources);
        ResourceSchema resourceSchema = getResourceSchema(resourceType);
        // 获取资源操作集合
        List<String> actionList = listOfActions(resourceSchema);
        List<AuthResourceActionDTO> araDtoList = new ArrayList<>();
        // 若是根账号则默认有全部权限
        if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
            listOfResource.forEach(resource -> {
                araDtoList.add(new AuthResourceActionDTO(actionList, resource));
            });
            LOGGER.info("method[authActionsBulk] The {} of {} authActionsBulk success", username, namespace);
            return araDtoList;
        }
        // 获取资源约束Key集合
        Set<String> constKeySet = listOfconstKey(resourceSchema);
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
        if (CollectionUtils.isEmpty(permissionList)) {
            return araDtoList;
        }
        listOfResource.forEach(resource -> {
            List<String> listOfPermissionsActions = listOfPermissionsActions(context, resource, actionList,
                    constKeySet, permissionList);
            if (CollectionUtils.isNotEmpty(listOfPermissionsActions)) {
                araDtoList.add(new AuthResourceActionDTO(listOfPermissionsActions, resource));
            }
        });
        LOGGER.info("method[authActionsBulk] The {} of {} authActionsBulk success", username, namespace);
        return araDtoList;
    }

    /**
     * 筛选混合资源类型和操作的列表
     *
     * @param username  成员名称
     * @param namespace 空间名称/根账号
     * @param isAdmin   是否为管理员
     * @param context   资源约束
     * @param resources 资源集合
     * @return 允许的混合资源类型和操作的列表
     */
    public List<AuthResourceActionDTO> authFilterMixed(final String username, final String namespace,
                                                       final Boolean isAdmin, final Map<String, String> context,
                                                       final List<Map<String, String>> resources) {
        validateParameterUsernameAndNamespace(username, namespace);
        List<Map<String, String>> listOfResource = validateListOfResource(resources);
        List<AuthResourceActionDTO> araDtoList = new ArrayList<>();
        List<Permission> permissionList = new ArrayList<>();
        // 若是根账号则默认有全部权限,若不是根账号则获取对应角色权限
        if (!(isAdmin != null && isAdmin == true || isAdmin(username, namespace))) {
            // 根据空间名称和成员名称获取对应的权限集合
            permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
            if (CollectionUtils.isEmpty(permissionList)) {
                LOGGER.info("method[authFilterMixed] The {} of {} authFilterMixed success", username,
                        namespace);
                return araDtoList;
            }
        }
        for (Map<String, String> resource : listOfResource) {
            // payload中的资源操作为空,则抛异常
            if (StringUtils.isBlank(resource.get(PAYLOAD_RESOURCE_ACTION_KEY))) {
                LOGGER.info("Resource actions cannot be empty");
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "action", new String[]{
                        BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY.getMessage()});
            }
//            ResourceSchema resourceSchema = getResourceSchema(
//                    resource.get(PAYLOAD_RESOURCE_ACTION_KEY).split(":")[0]);
//            // 获取资源操作集合
            List<String> actionList = Lists.newArrayList();
            actionList.add(resource.get(PAYLOAD_RESOURCE_ACTION_KEY));
//            // 获取资源约束Key集合
            Set<String> constKeySet = Sets.newHashSet();
            // 若是根账号则默认有全部权限
            if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
                araDtoList.add(new AuthResourceActionDTO(actionList, resource));
                continue;
            }
            List<String> listOfPermissionsActions = listOfPermissionsActions(context, resource, actionList,
                    constKeySet, permissionList);
            if (CollectionUtils.isNotEmpty(listOfPermissionsActions)
                    && listOfPermissionsActions.contains(resource.get(PAYLOAD_RESOURCE_ACTION_KEY))) {
                araDtoList.add(new AuthResourceActionDTO(listOfPermissionsActions, resource));
            }
        }
        LOGGER.info("method[authFilterMixed] The {} of {} authFilterMixed success", username, namespace);
        return araDtoList;
    }

    /**
     * 获取有权限的资源操作列表
     *
     * @param username  成员名称
     * @param namespace 空间名称/根账号
     * @param isAdmin   是否为管理员
     * @return 有权限的资源操作列表
     */
    public List<ResourceSchema> listActions(final String username, final String namespace,
                                            final Boolean isAdmin) {
        validateParameterUsernameAndNamespace(username, namespace);
        // 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
        List<ResourceSchema> fetchResourceSchemaList = cacheBiz.listResourceSchema();
        // 若是根账号则默认有全部权限
        if (isAdmin != null && isAdmin == true || isAdmin(username, namespace)) {
            LOGGER.info("method[listActions] The {} of {} listActions success", username, namespace);
            return fetchResourceSchemaList;
        }
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
        for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
            if (CollectionUtils.isNotEmpty(resourceSchema.getActions())) {
                resourceSchema.setActions(resourceSchema.getActions().stream().filter(resourceSchemaActions ->
                        resourceSchemaActions.getAction() != null).filter(resourceSchemaActions -> {
                    return permissionList.stream().map(permission -> fromPermissionPo(permission))
                            .flatMap(permissionDTO -> permissionDTO.getActions().stream()).distinct()
                            .anyMatch(permissionAction -> Pattern
                                    .compile(permissionAction.replace(RESOURCE_ACTION_REPLACE_TARGET,
                                            ACTION_REPLACE_REPLACEMENT))
                                    .matcher(resourceSchemaActions.getAction()).matches());
                }).collect(Collectors.toList()));
            }
        }
        LOGGER.info("method[listActions] The {} of {} listActions success", username, namespace);
        return fetchResourceSchemaList;
    }

    /**
     * 验证username,namespace请求参数
     *
     * @param username  成员名称
     * @param namespace 空间名称
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateParameterUsernameAndNamespace(final String username, final String namespace)
            throws BadRequestFieldException {
        if (StringUtils.isBlank(username)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "username",
                    new String[]{BadRequestFieldMessageEnum.USER_NAME_CANNOT_BE_EMPTY.getMessage()});
        }
        if (StringUtils.isBlank(namespace)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "namespace",
                    new String[]{BadRequestFieldMessageEnum.NAMESPACE_NAME_CANNOT_BE_EMPTY.getMessage()});
        }
    }

    /**
     * 验证action请求参数
     *
     * @param action 资源操作
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateParameterAction(final String action) throws BadRequestFieldException {
        if (StringUtils.isBlank(action)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "action", new String[]{
                    BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY.getMessage()});
        }
    }

    /**
     * payload中的resource.name或resource.uuid是否匹配权限中的资源字段值
     *
     * @param permission 单个权限对象
     * @param resource   单个资源对象
     * @return 匹配返回true, 不匹配返回false
     */
    private boolean isPermissionResourcesMatchesResourceName(final Permission permission,
                                                             final Map<String, String> resource) {
        // 资源允许为空
        if (resource == null || resource.isEmpty()) {
            return true;
        }
        // 数据库中存储资源名称的JSON数组字符串格式:["web","web*","*web"]或["*"]
        // 循环匹配资源名称或资源UUID,只要匹配到一个满足条件则返回true,反之返回false
        return JSONArray.fromObject(permission.getResources()).stream().anyMatch(permissionResource -> Pattern
                .compile(permissionResource.toString().replace(RESOURCE_ACTION_REPLACE_TARGET,
                        RESOURCE_REPLACE_REPLACEMENT))
                .matcher(StringUtils.trimToEmpty(resource.get(PAYLOAD_RESOURCE_NAME_KEY))).matches()
                || Pattern
                .compile(permissionResource.toString().replace(RESOURCE_ACTION_REPLACE_TARGET,
                        RESOURCE_REPLACE_REPLACEMENT))
                .matcher(StringUtils.trimToEmpty(resource.get(PAYLOAD_RESOURCE_UUID_KEY))).matches());
//      return JSONArray.fromObject(permission.getActions()).stream().anyMatch(permissionResource -> Pattern
//      .compile(permissionResource.toString().replace(RESOURCE_ACTION_REPLACE_TARGET,
//              RESOURCE_REPLACE_REPLACEMENT))
//      .matcher(StringUtils.trimToEmpty(resource.get("action"))).matches());
    }

    private boolean isPermissionResourcesMatchesAction(final Permission permission,
                                                       final String action) {
        // 资源允许为空
        if (action == null || action.isEmpty()) {
            return true;
        }
        // 数据库中存储资源名称的JSON数组字符串格式:["web","web*","*web"]或["*"]
        // 循环匹配资源名称或资源UUID,只要匹配到一个满足条件则返回true,反之返回false
        return JSONArray.fromObject(permission.getActions()).stream().anyMatch(permissionResource -> Pattern
                .compile(permissionResource.toString().replace(RESOURCE_ACTION_REPLACE_TARGET,
                        RESOURCE_REPLACE_REPLACEMENT))
                .matcher(StringUtils.trimToEmpty(action)).matches());
    }

    /**
     * payload中的conext和resource中的资源约束是否匹配权限中的资源约束
     *
     * @param permission  单个权限对象
     * @param constraints 资源约束
     * @return 匹配返回true, 不匹配返回false
     */
    private boolean isPermissionConstraintsMatchesResourceConstraints(final Permission permission,
                                                                      final Map<String, String> constraints) {
        // 约束为空,则表示无约束,直接返回true
        if (constraints == null || constraints.isEmpty()) {
            return true;
        } else {
            List<Map<String, String>> constraintsList = JSONArray.fromObject(permission.getConstraints());
            return constraintsList.stream().anyMatch(constrMap -> {
                // 数据中权限存储约束字段值为 [{}] ,表示权限没有约束则直接返回true
                if (constrMap.isEmpty()) {
                    return true;
                }
                // payload中的context必须满足在权限的资源约束中匹配
                return constraints.entrySet().stream().allMatch(constraint -> {
                    String key = constraint.getKey();
                    String value = constraint.getValue();
                    if (!constrMap.containsKey(key)) {
                        return true;
                    }
                    return constrMap.containsKey(key) && constrMap.get(key).equals(value);
                });

            });
        }
    }

    /**
     * 匹配资源操作字典表,返回有权限的资源操作集合
     *
     * @param actionList          资源操作集合
     * @param finalPermissionList 最终的权限集合(资源操作,资源名称,资源约束过滤之后的权限集合)
     * @return 允许资源操作的集合
     */
    private List<String> listOfAllowedActions(final List<String> actionList,
                                              final List<Permission> finalPermissionList) {
        // 匹配资源操作字典表,返回有权限的资源操作集合
        return actionList.stream().filter(actionn -> {
            return finalPermissionList.stream().map(permission -> fromPermissionPo(permission))
                    .flatMap(permissionDTO -> permissionDTO.getActions().stream()).distinct()
                    .anyMatch(permissionAction -> Pattern.compile(permissionAction
                            .replace(RESOURCE_ACTION_REPLACE_TARGET, ACTION_REPLACE_REPLACEMENT))
                            .matcher(actionn).matches());
        }).collect(Collectors.toList());
    }

    /**
     * payload中的资源可以为null或无元素的对象<br>
     * 根据资源约束字典表中的KEY获取payload中每个资源对应的资源约束KEY-VALUE<br>
     * conext约束不为空,则和资源对象中的约束合并<br>
     *
     * @param conext      payload中的conext
     * @param resource    payload中的单个资源对象
     * @param constKeySet 资源约束集合
     * @return 满足条件的资源约束KEY-VALUE
     */
    private Map<String, String> getResourceConstraints(final Map<String, String> context,
                                                       final Map<String, String> resource, final Set<String>
                                                               constKeySet) {
        Map<String, String> constr = new HashMap<>();
        // constKeySet为空,表示无约束
        if (CollectionUtils.isEmpty(constKeySet)) {
            return constr;
        }
        // 根据资源约束字典表中的KEY获取payload中context对应的资源约束KEY-VALUE
        // 根据资源约束字典表中的KEY获取payload中resource对应的资源约束KEY-VALUE
        constKeySet.forEach(key -> {
            if (context != null && !context.isEmpty()) {
                if (StringUtils.isNotBlank(context.get(key))) {
                    constr.put(key, context.get(key));
                }
            }
            if (resource != null && StringUtils.isNotBlank(resource.get(key))) {
                constr.put(key, resource.get(key));
            }
        });
        return constr;
    }

    /**
     * 获取指定类型的单个资源模式对象
     *
     * @param resourceType 资源类型
     * @return
     */
    private ResourceSchema getResourceSchema(final String resourceType) {
        return cacheBiz.getResourceSchema(resourceType, null);
    }

    /**
     * 获取资源操作集合
     *
     * @param resourceSchema 资源模式对象
     * @return 资源操作集合
     */
    private List<String> listOfActions(final ResourceSchema resourceSchema) {
        // 获取资源操作集合
        return resourceSchema.getActions().stream().map(ResourceSchemaActions::getAction)
                .filter(action -> StringUtils.isNotBlank(action)).distinct().collect(Collectors.toList());
    }

    /**
     * 获取资源约束Key集合
     *
     * @param resourceSchema 资源模式对象
     * @return 资源约束Key集合
     */
    private Set<String> listOfconstKey(final ResourceSchema resourceSchema) {
        // 获取资源约束Key集合
        return resourceSchema.getConstraints().stream().map(ResourceSchemaConstraints::getConstKey)
                .filter(constKey -> StringUtils.isNotBlank(constKey)).collect(Collectors.toSet());
    }

    /**
     * payload中的资源集合可以为空
     *
     * @param resources 资源集合
     * @return 资源集合
     */
    private List<Map<String, String>> validateListOfResource(final List<Map<String, String>> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            // throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "resources",
            // new String[] { BadRequestFieldMessageEnum.RESOURCES_CANNOT_BE_EMPTY.getMessage() });
            return new ArrayList<>();
        }
        return resources;
    }

    /**
     * 获取有权限的资源操作集合
     *
     * @param context        约束
     * @param resource       资源
     * @param actionList     资源操作集合
     * @param constKeySet    资源约束KEY集合
     * @param permissionList 权限集合
     * @return 有权限的资源操作集合
     * //
     */
    private List<String> listOfPermissionsActions(final Map<String, String> context,
                                                  final Map<String, String> resource, final List<String> actionList,
                                                  final Set<String> constKeySet,
                                                  final List<Permission> permissionList) {
        Map<String, String> constr = getResourceConstraints(context, resource, constKeySet);
        List<Permission> finalPermissionList = permissionList.stream()
                // 过滤资源名称
                .filter(permission -> isPermissionResourcesMatchesResourceName(permission, resource))
                // 过滤资源约束
                .filter(permission -> isPermissionConstraintsMatchesResourceConstraints(permission, constr))
                .collect(Collectors.toList());
        return listOfAllowedActions(actionList, finalPermissionList);
    }

    /**
     * 获取有权限的资源操作集合
     *
     * @param username       成员名称
     * @param namespace      空间名称
     * @param context        约束
     * @param resource       资源
     * @param actionList     资源操作集合
     * @param constKeySet    资源约束KEY集合
     * @param permissionList 权限集合
     * @return 有权限的资源操作集合
     */
    private List<String> listOfPermissionsActions(final String username, final String namespace,
                                                  final Map<String, String> context, final Map<String, String> resource,
                                                  final List<String> actionList, final Set<String> constKeySet) {
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        Map<String, String> constr = getResourceConstraints(context, resource, constKeySet);
        List<Permission> finalPermissionList = permissionList.stream()
                // 过滤资源名称
                .filter(permission -> isPermissionResourcesMatchesResourceName(permission, resource))
                // 过滤资源约束
                .filter(permission -> isPermissionConstraintsMatchesResourceConstraints(permission, constr))
                .collect(Collectors.toList());
        return listOfAllowedActions(actionList, finalPermissionList);
    }

    private List<Permission> listOfPermissionsActions(final String username, final String namespace,
                                                      final Map<String, String> context, final String action) {
        // 根据空间名称和成员名称获取对应的权限集合
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid(username, namespace);
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        List<Permission> finalPermissionList = permissionList.stream()
                // 过滤资源名称
                .filter(permission -> isPermissionResourcesMatchesAction(permission, action))
                .collect(Collectors.toList());
        return finalPermissionList;
    }

    /**
     * 判断是否为管理员
     *
     * @param username  成员名称
     * @param namespace 空间名称
     * @return true为是;false为不是
     */
    private boolean isAdmin(final String username, final String namespace) {
        return StringUtils.trimToEmpty(username).equals(StringUtils.trimToEmpty(namespace));
    }

    /**
     * 封装PermissionDTO
     *
     * @param permission PO Entity
     * @return PermissionDTO
     */
    private PermissionDTO fromPermissionPo(final Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setUuid(permission.getUuid());
        permissionDTO.setRoleUuid(permission.getRoleUuid());
        permissionDTO.setActions(JSONArray.fromObject(permission.getActions()));
        permissionDTO.setResource(JSONArray.fromObject(permission.getResources()));
        permissionDTO.setConstraints(JSONArray.fromObject(permission.getConstraints()));
        return permissionDTO;
    }

}