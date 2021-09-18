package com.migu.tsg.microservice.atomicservice.composite.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称: rbac-service 包: com.migu.tsg.microservice.atomicservice.rbac.enums 类名称:
 * ResultErrorEnum.java 类描述: 错误码 通常 RESTful 服务是通过 HTTP 的错误状态码来标示错误，
 * 但是状态码无法用来表示具体业务逻辑错误，多种业务逻辑错误通常会对应一样的状态码。 因此，我们在状态码的基础之上针对业务逻辑制定了错误码，来帮助 API
 * 的调用者和程序更加方便地定位错误。 创建人: WangSheng 创建时间: 2017年7月24日上午11:30:15
 * 
 */
@AllArgsConstructor
public enum ResultErrorEnum {
    USER_CONNECTED_USER_DELETE("User does'nt have the permission", 403, "用户被其他用户关联，不允许删除"),
    USER_CONNECTED_ACCTOUNT_DELETE("User does'nt have the permission", 403, "用户关联账号，不允许删除"),
    PARENT_DEPARTEMNT_NOT_EXIST("The_father_department_is_not_exists", 403, "上级部门不存在"),
    TEMP_DEPARTEMNT_NOT_ALLOWED("Temp_department_is_not_allowed", 403, "不允许在临时部门下创建正式部门"),
    FATHER_DEPARTEMNT_NOT_DELETE("The_father_departemnt_not_delete", 403, "存在子部门不允许删除"),
    DEPARTEMNT_EXIST_USER_NOT_DELETE("Departemnt_exist_user_not_delete", 403, "部门下存在用户不允许删除"),
    DEPARTMENT_NOT_FIND("department_not_find", 403, "部门不存在"),
    FORMAL_DEPARTEMNT_NOT_ALLOW_MULTIPLE("formal_departemnt_not_allow_multiple", 403, "用户不允许归属多个正式部门"),
    INVALID_LOGIN_CODE("invalid_login_code", 403, "登录验证码不正确"),
    /**
     * 请求参数格式不正确(非json或其它规定的格式)
     */
    MALFORMED_REQUEST("malformed_request", 400, "The requested parameter format is incorrect"),

    /**
     * 请求方法?不被允许 注:?为占位符
     */
    METHOD_NOT_ALLOWED("method_not_allowed", 405, "Method '?' not allowed"),

    /**
     * 请求的数据格式不支持(通常我们只接收json格式的请求数据)
     */
    UNSUPPORTED_MEDIA_TYPE("unsupported_media_type", 415, "Requested data format is not supported"),

    /**
     * 未知错误
     */
    UNKNOWN_ISSUE("unknown_issue", 500, "Unknown issue"),


    /**
     * servlet请求绑定错误
     */
    BAD_REQUEST_BINDING("bad_request_binding", 404, "Bad request binding"),

    HTTP_MESSAGE_CONVERSION_ERROR("http_message_conversion_exception", 404, "http message conversion exception"),

    GENERAL_SERVLET_EXCEPTION("general_servlet_exception", 404, "general servlet exception"),

    SPRING_DAO_EXCEPTION("spring_general_dao_exception", 404, "spring general dao exception"),

    /**
     * 所需数据无效
     */
    BAD_REQUEST("bad_request", 404, "Required data not valid"),

    /**
     * 请求地址无效
     */
    BAD_REQUEST_URL("bad_request_url", 404, "Bad request url"),

    FEIGN_UNKNOW_ERROR("feign_client_service_unkown_error", 404, "feign client service unkown error."),

    FEIGN_BAD_REQUEST("feign_client_service_bad_request", 404, "feign client service bad request."),

    FEIGN_TIMEOUT_EXCEPTION("feign_client_service_timeout", 408, "feign client service timeout."),

    FEIGN_RETURN_ERROR("feign_client_service_error", 500, "feign client service return error."),

    SERVICE_CLIENT_BIZ_ERROR("service_client_business_error", 408, "service client business error."),

    BIZ_OBJECT_PARSE_JSON_FAIL("Fail_to_parse_object_to_json", 500, "Fail to parse object to json."),

    BIZ_ABSENT_USER_AUTH("No_user_auth_info_in_the_request_head", 403, "No user auth info in the request head."),

    BIZ_ADMIN_AUTH_REQUIRED("Only_admin_is_authorized", 403, "Only admin is authorized."),

    BIZ_RESOURCE_ACTION_DENY("User does'nt have the permission", 403, "用户没有操作权限."),

    BIZ_RESOURCE_NOT_EXIST("The_resource_is_not_exist", 404, "The resource is not exist."),
    
    REGION_NOT_EXIST("region_not_exist", 404, "the region is not exist"),

    CODE_REPO_NOT_EXIST("code_repo_not_exist", 404, "can not find code_repo in build_config"),

    BUILD_CONFIG_NOT_EXIST("build_config_not_exist", 404, "can not find build config by web hook code"),

    DEST_REPO_NOT_EXIST("dest_repo_not_exist", 404, "dest image repo is delete, please choose a new one"),

    BIZ_RESOURCE_ALREADY_EXIST("The_resource_is_already_exists", 403, "The resource is already exists."),

    BIZ_RESOURCE_NOT_MATCH("The_resource_is_not_match", 404, "The resource is not match."),

    BIZ_RESOURCE_NOT_RELEASED("The_resource_is_not_released", 409, "The resource is not released."),

    BIZ_PARAMETER_INVALID_VALUE("The_request_parameter_value_invalid", 400, "The request parameter value is invalid."),

    BIZ_PARAMETER_NOT_PROVIDED("The_request_parameter_must_provide", 400, "The request parameter must provide."),

    BIZ_PERTIAL_PERMISSION_DENIED("partial permission is denied", 400, "partial permission is denied"),

    BIZ_REGIONNAME_AND_SPACENAME_REQUIRED("region_name and space_name is required", 400,
            "region_name and space_name is required"),

    BIZ_DELETE_FAILED("failed to delete", 404, "failed to delete"),


    BIZ_PARAMETER_CHECK_FAIL("parameter_check_fail", 400, "parameter check is failed."),

    BIZ_PERTIAL_RESOURCE_NOT_PROVIDED("partial resources have no permissions", 404,
            "partial resources have no permissions"),

    BIZ_REGION_IS_NOT_EMPTY("region_is_not_empty", 400, "Region is not empty."),

    BIZ_REGISTRY_PROJECT_CREATE_FAIL("create resource falied", 400, "create_project_failed"),

    BIZ_PROJECT_IS_NOT_EMPTY("project_is_not_empty", 400, "project_is_not_empty"),

    BIZ_NAME_ALREADY_EXIST("The_name_is_already_exists", 403, "The name is already exists."),

    BIZ_VERSION_IS_ERROR("The_version_is_error", 403, "The version is error."),

    BIZ_PORT_IS_INVALID("The_port_is_invalid", 403, "The version is invalid."),

    BIZ_PARAMETER_TOO_LONG("the_parameter_is_too_long", 400, "the parameter is too long."),

    BIZ_PARAMETER_IN_BLACKLIST("the_parameter_is_in_the_blackList", 400,

            "the parameter is in the blackList"),

    BIZ_APPLICATION_NOT_ALLOWED("The_application_is_not_allowed", 400, "The application is not allowed."),

    BIZ_PRIMARY_KEY_DUPLICATED("primary_key_duplicated", 500, "primary key is duplicate"),

    BIZ_FILE_DELETE_UNSUCCESSFULLY("The_temporary_file_is_deleted_unsuccessfully", 500,
            "The temporary file is deleted unsuccessfully"),

    TEMPLATE_DELETE_UNSUCCESSFULLY("The_template_delete_unsuccessfully", 500,
            "The template of related tasks cannot be removed"),

    THEME_DELETE_UNSUCCESSFULLY("the_theme_delete_unsuccessfully", 500,
            "The theme is associated with a monitoring item and is not allowed to be deleted"),

    BIZ_FILE_CREATE_UNSUCCESSFULLY("The_temporary_file_is_created_unsuccessfully",

            500, "The temporary file is created unsuccessfully"),

    BIZ_RESOURCE_NAME_INVALID("resource_name_invalid", 400, "resource_name_invalid"),

	BIZ_NO_PERMISSION_DELETE_SERVICES("Deleting_resources_has_no_permissions ", 400,
			"Deleting resources has no permissions "),

	BIZ_INVALID_BASIC_HEADER("Invalid basic header. No credentials provided.", 400,
			"Invalid basic header. No credentials provided."),

	BIZ_INVALID_BASIC_HEADER_SPACES("Invalid basic header. Credentials string should not contain spaces.", 400,
			"Invalid basic header. Credentials string should not contain spaces."),

    BIZ_ZONE_IS_NOT_EMPTY("The zone is not empty.", 400,"The zone is not empty");
    /**
     * 错误码
     */
    @Getter
    private final String code;

    /**
     * HTTP 状态码
     */
    @Getter
    private final int httpCode;

    /**
     * 错误描述
     */
    @Getter
    private final String message;
}
