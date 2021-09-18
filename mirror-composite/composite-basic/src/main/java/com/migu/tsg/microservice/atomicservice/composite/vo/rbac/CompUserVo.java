package com.migu.tsg.microservice.atomicservice.composite.vo.rbac;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @author baiwenping
 * @Title: CompUserVO
 * @Package com.migu.tsg.microservice.atomicservice.composite.vo.rbac
 * @Description: TODO
 * @date 2021/3/15 20:29
 */
@Data
public class CompUserVo {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("user_type")
    private Integer userType;
    @JsonProperty("dept_id")
    private String deptId;
    @JsonProperty("no")
    private String no;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("ldapId")
    private String ldapId;
    @JsonProperty("namespace")
    private String namespace;
    @JsonProperty("code")
    private String code;
    @JsonProperty("fax")
    private String fax;
    @JsonProperty("post")
    private String post;
    @JsonProperty("relation_person")
    private String relationPerson;
    @JsonProperty("descr")
    private String descr;
    @JsonProperty("picture")
    private String picture;
    @JsonProperty("ldap_password_updatetime")
    private Timestamp ldapPasswordUpdatetime;
    @JsonProperty("ldap_status")
    private String ldapStatus;
}
