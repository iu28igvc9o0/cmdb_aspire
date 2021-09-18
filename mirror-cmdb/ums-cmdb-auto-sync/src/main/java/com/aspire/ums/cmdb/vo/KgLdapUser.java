package com.aspire.ums.cmdb.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Entry;

/**
 * @Author: huanggongrui
 * @Description: 科管ldap用户实体
 * @Date: create in 2020/8/17 14:38
 */
@Data
@NoArgsConstructor
@Entry(objectClasses = {"cmcc-abstractPerson"}, base = "ou=users,dc=cmri")
public class KgLdapUser {

    private String cn;
    private String o;
    private String sn;
    private String uid;
    private String birthday;
    private String displayOrder;
    private String employeeNumber;
    private String employeeType;
    private String endTime;
    private String gender;
    private String jpegPhoto;
    private String mail;
    private String mobile;
    private String ou;
    private String passwordModifiedDate;
    private String positionLevel;
    private String role;
    private String startTime;
    private String status;
    private String telephoneNumber;
    private String userPassword;
}
