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
@Entry(objectClasses = {"cmcc-abstractPerson"}, base = "ou=organizations,dc=cmri")
public class KgLdapOrganization {

    private String o;
    private String parentOrgId;
    private String displayOrder;
    private String displayName;
    private String initials;
    private String entryUUID;
    private String description;
}
