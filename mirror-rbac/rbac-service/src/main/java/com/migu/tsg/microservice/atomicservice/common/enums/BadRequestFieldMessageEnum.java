/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.enums <br>
 * 类名称: BadRequestFieldDescribeEnum.java <br>
 * 类描述: 请求字段不合法的异常之无效原因的提示信息枚举类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月29日上午10:17:36 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
public enum BadRequestFieldMessageEnum {
    /**
     * 角色名称不能为空
     */
    ROLE_NAME_CANNOT_BE_EMPTY("Role name cannot be empty"),

    /**
     * 请求中重复名称
     */
    NAME_ALREADY_IN_THE_REQUEST("The name {} already in the request"),

    /**
     * 角色至少应该有一个权限
     */
    ROLE_SHOULD_HAVE_AT_LEAST_ONE_PERMISSION("The role {} should have at least one permission"),

    /**
     * 空间名称不能为空
     */
    NAMESPACE_NAME_CANNOT_BE_EMPTY("Namespace name cannot be empty"),

    /**
     * 资源名称不能为空
     */
    RESOURCE_NAME_CANNOT_BE_EMPTY("Resource name cannot be empty"),

    /**
     * 资源类型不能为空
     */
    RESOURCE_TYPE_CANNOT_BE_EMPTY("Resource type cannot be empty"),

    /**
     * 资源不能为空
     */
    RESOURCES_CANNOT_BE_EMPTY("Resources cannot be empty"),

    /**
     * 资源操作不能为空
     */
    RESOURCE_ACTIONS_CANNOT_BE_EMPTY("Resource actions cannot be empty"),

    /**
     * 资源操作不存在
     */
    RESOURCE_ACTIONS_NOT_EXIST("Resource actions {} does not exist"),

    /**
     * 资源约束(Key)不存在
     */
    RESOURCE_CONSTRAINTS_KEY_NOT_EXIST("Resource constaints key does not exist"),

    /**
     * 用户名称不能为空
     */
    USER_NAME_CANNOT_BE_EMPTY("User name cannot be empty"),

    /**
     * 用户邮箱不能为空
     */
    USER_EMAIL_CANNOT_BE_EMPTY("User email cannot be empty"),

    /**
     * 用户名称重复
     */
    USER_NAME_REPETITION("User name repetition"),

    /**
     * 用户名称已存在
     */
    USER_WITH_THE_USERNAME_ALREADY_EXISTS("User with the {} username already exists"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST("The role {} does not exist"),

    /**
     * 角色已经存在
     */
    ROLE_ALREADY_EXIST("The project template {} already exist"),

    /**
     * 至少有一个成员名存在
     */
    AT_LEAST_ONE_MEMBER_NAME_IS_PRESENT("At least one member name is present"),

    /**
     * 至少分配一个角色给用户
     */
    AT_LEAST_ONE_ROLE_ASSIGN_TO_USER("At least one role is assigned to the user"),

    /**
     * 至少撤销用户的一个角色
     */
    AT_LEAST_REVOKE_USER_ONE_ROLE("Revoke user's one role at least"),

    /**
     * 至少撤销一个用户的所有角色
     */
    AT_LEAST_REVOKE_ONE_USER_ALL_ROLE("Revoke at least one user's all roles"),

    /**
     * 请求中重复用户名称
     */
    DUPLICATED_USERNAME_IN_THE_PAYLOAD("Duplicated username {} in the payload"),

    /**
     * 角色UUID必填
     */
    ROLE_UUID_IS_REQUIRED("Role UUID is required"),

    /**
     * 同一个父角色不能被继承多次
     */
    PARENT_ROLE_CONNOT_INHERITED_MORE("The same parent role can not be inherited many times"),

    /**
     * 禁止删除被继承的角色
     */
    ROLE_INHERITED_NOT_REMOVED("禁止删除被继承的角色"),

    /**
     * 关联了用户的功能角色不允许删除
     */
    IS_CONNECTED_USER_ROLE_NOT_REMOVED("已关联用户的角色不允许删除"),

    /**
     * 角色不能相互继承
     */
    ROLES_CANNOT_MUTUAL_INHERITED("Roles cannot be inherited from one another"),

    /**
     * 角色继承层级不能超过4层
     */
    ROLES_INHERITED_CANNOT_OUTNUMBER4("Roles inherited must not outnumber 4 layers"),

    /**
     * 当前角色没有继承指定的父角色
     */
    ROLE_NOT_INHERIT_PARENT_ROLE("The role did not inherit the parent role"),

    /**
     * 父角色不存在
     */
    PARENT_NOT_EXIST("Parent Role not found"),

    /**
     * 角色和父角色不属于同一个命名空间下
     */
    ROLE_AND_PARENT_NOT_SAME_NAMESPACE("The roles and the parent roles do not belong to the same namespace"),

    /**
     * 角色权限不能为空
     */
    PERMISSIONS_OF_ROLE_NOT_EMPTY("Role permissions cannot be empty"),

    /**
     * 父角色不能是自己
     */
    PARENT_ROLE_NOT_SELF("Parent Role is not this self"),

    /**
     * 父角色已存在
     */
    PARENT_ALREADY_ADDED("The parent was already added"),

    /**
     * 资源不存在
     */
    RESOURCE_NOT_FOUND("Resource not found"),

    /**
     * 资源模式(资源类型,资源操作,资源约束)不存在
     */
    RESOURCE_SCHEMA_NOT_FOUND("Resource schema {} not found"),

    /**
     * 用户已经分配角色 [USER]占位符 成员名称 [ROLE]占位符 角色名称
     */
    USER_HAVE_ROLE("User {} already have role {} assigned"),
    /**
     * 用户没有分配角色
     */
    USER_DONT_HAVE_ROLE("User {} does not assign roles to {}"),

    /**
     * 权限不存在
     */
    PERMISSION_NOT_EXIST("Permission not in role"),

    /**
     * 空间名称(根账号)不存在
     */
    NAMESPACE_NOT_EXIST("Namespace not found"),

    /**
     * 空间名称(根账号)下成员不存在
     */
    NAMESPACE_USERNAME_NOT_EXIST("Username of namespace not found"),

    /**
     * 用户上不存在的角色
     */
    ROLE_NOT_PRESENT_ON_USER("Role not present on user"),

    /**
     * UUID不合法的/无效的
     */
    UUID_REQUEST_INVALID("Required uuid not valid"),

    /**
     * 请求中重复角色名称
     */
    DUPLICATED_ROLENAME_IN_THE_PAYLOAD("Duplicated rolename {} in the payload"),

    /**
     * 公司LOGO无效
     */
    LOGO_FILE_IS_INVALID("Logo file is invalid"),

    /**
     * 公司名称不能为空
     */
    COMPANY_NAME_CANNOT_BE_EMPTY("Company name cannot be empty"),

    /**
     * 项目模板名称不能为空
     */
    PROJECT_TEMPLATE_NAME_CANNOT_BE_EMPTY("Project template name cannot be empty"),

    /**
     * 项目名称不能为空
     */
    PROJECT_NAME_CANNOT_BE_EMPTY("Project name cannot be empty"),

    /**
     * 项目模版声明的角色的ID不能为空
     */
    PROJECP_TEMPLATE_ROLE_ID_CANNOT_BE_EMPTY("Project template role id cannot be empty"),

    /**
     * 项目模版声明的角色,
     * 角色ID，必须在同一个项目模版内是唯一的
     */
    PROJECP_TEMPLATE_ROLE_ID_ALREADY_IN_THE_REQUEST("Project template role id already in the request"),

    /**
     * 项目模版声明的角色,
     * 角色ID，必须在同一个项目模版内是唯一的
     */
    PROJECP_TEMPLATE_ROLE_ID_ALREADY_EXISTS("Project template role id already exists"),

    /**
     * 项目模版声明的角色的NAME不能为空
     */
    PROJECP_TEMPLATE_ROLE_NAME_CANNOT_BE_EMPTY("Project template role name cannot be empty"),

    /**
     * 项目模版声明的角色,
     * 角色Name，必须在同一个项目模版内是唯一的
     */
    PROJECP_TEMPLATE_ROLE_NAME_ALREADY_IN_THE_REQUEST("Project template role name already in the request"),

    /**
     * 项目模版声明的角色,
     * 角色NAME，必须在同一个项目模版内是唯一的
     */
    PROJECP_TEMPLATE_ROLE_NAME_ALREADY_EXISTS("Project template role name already exists"),

    /**
     * 项目模版声明的角色,
     * 角色依赖不存在
     */
    PROJECP_TEMPLATE_ROLE_DEPEND_NOT_EXISTS("Project template role depend not exists"),

    /**
     * 项目模版声明的角色,
     * 角色不能依赖自己
     */
    PROJECP_TEMPLATE_ROLE_DEPEND_CONNOT_SELF("Project template role depend connot be self"),

    /**
     * 项目模版声明的角色,
     * 父角色ITEM不能为空
     */
    PROJECP_TEMPLATE_ROLE_PARENT_ITEM_CONNOT_EMPTY("Project template role parent item connot be empty"),

    /**
     * 项目模版声明的角色,
     * 父角色ITEM不存在
     */
    PROJECP_TEMPLATE_ROLE_PARENT_ITEM_NOT_EXISTS("Project template role parent item not exists"),

    /**
     * 项目模版声明的角色,
     * 父角色ITEM不能是自己
     */
    PROJECP_TEMPLATE_ROLE_PARENT_ITEM_CONNOT_SELF("Project template role parent item connot be self"),

    /**
     * 项目模版声明的角色,
     * 父角色NAME不能为空
     */
    PROJECP_TEMPLATE_ROLE_PARENT_NAME_CONNOT_EMPTY("Project template role parent name connot be empty"),

    /**
     * 项目模版声明的角色,
     * 父角色NAME不存在
     */
    PROJECP_TEMPLATE_ROLE_PARENT_NAME_NOT_EXISTS("Project template role parent name not exists"),

    /**
     * 项目模版声明的角色,
     * 依赖集合和父角色集合不全等
     */
    PROJECP_TEMPLATE_ROLE_DEPEND_AND_PARENT_NOT_EQUALS("Project template role depend and parent not equals"),

    /**
     * 项目模版声明的资源的ID不能为空
     */
    PROJECP_TEMPLATE_RESOURCE_ID_CANNOT_BE_EMPTY("Project template resource id cannot be empty"),

    /**
     * 项目模版声明的角色,
     * 资源ID，必须在同一个项目模版内是唯一的
     */
    PROJECP_TEMPLATE_RESOURCE_ID_ALREADY_IN_THE_REQUEST(
            "Project template resource id already in the request"),

    /**
     * 项目模版声明的资源的NAME不能为空
     */
    PROJECP_TEMPLATE_RESOURCE_NAME_CANNOT_BE_EMPTY("Project template resource name cannot be empty"),

    /**
     * 项目模板不存在
     */
    PROJECT_TEMPLATE_NOT_EXIST("The project template {} does not exist"),

    /**
     * 项目模板已经存在
     */
    PROJECT_TEMPLATE_ALREADY_EXIST("The project template {} already exist"),

    /**
     * 项目不存在
     */
    PROJECT_NOT_EXIST("The project {} does not exist"),

    /**
     * 项目已经存在
     */
    PROJECT_ALREADY_EXIST("The project {} already exist"),;

    @Getter
    private String message;

    /**
     * 动态描述信息
     * @param enumm 枚举对象
     * @param name 动态名称
     * @return 信息描述
     */
    public static String dynamicMessage(BadRequestFieldMessageEnum enumm, String... names) {
        String message = enumm.getMessage();
        for (String name : names) {
            message = Pattern.compile("\\{\\}").matcher(message)
                    .replaceFirst(Matcher.quoteReplacement(StringUtils.trimToEmpty(name)));
        }
        return Pattern.compile("\\s{2,}").matcher(message).replaceAll(" ");
    }

}
