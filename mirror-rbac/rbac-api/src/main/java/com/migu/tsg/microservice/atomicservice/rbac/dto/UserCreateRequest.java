package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * monitor_user新增对象类
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.entity 类名称: User 类描述: monitor_actions创建响应对象 创建人: 曾祥华 创建时间:
 * 2019-03-07 16:05:29
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreateRequest implements Serializable {

    /**
     * @fields serialVersionUID
     */

    private static final long serialVersionUID = -6483844706951320951L;

    /** 姓名 */
    @JsonProperty("name")
    private String name;

    /** 用户类型 */
    @JsonProperty("user_type")
    private Integer userType;

    /** 所属部门 */
    @JsonProperty("dept_id")
    private String deptId;

    /** 人员编号 */
    @JsonProperty("no")
    private String no;

    /** 性别 */
    @JsonProperty("sex")
    private Integer sex;

    /** 邮箱 */
    @JsonProperty("mail")
    private String mail;

    /** 地址 */
    @JsonProperty("address")
    private String address;

    /** 电话号码 */
    @JsonProperty("phone")
    private String phone;

    /** 移动号码 */
    @JsonProperty("mobile")
    private String mobile;

    /** 账号id */
    @JsonProperty("ldapId")
    private String ldapId;

    /** 空间 */
    @JsonProperty("namespace")
    private String namespace;

    /** 人员代码 */
    @JsonProperty("code")
    private String code;

    /** 传真 */
    @JsonProperty("fax")
    private String fax;

    /** 职责 */
    @JsonProperty("post")
    private String post;

    /** 关联人 */
    @JsonProperty("relation_person")
    private String relationPerson;

    /** 描述 */
    @JsonProperty("descr")
    private String descr;

    @JsonProperty("dept_ids")
    private List<String> deptIds;

    /**
     * 头像
     */
    private String picture;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("excel")
    private boolean excel = false;

    /** 网管同步的. */
    @JsonProperty("user_id")
    private String userId;
}
