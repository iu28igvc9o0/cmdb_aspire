package com.migu.tsg.microservice.atomicservice.ldap.biz.mapper;

import static com.migu.tsg.microservice.atomicservice.ldap.enums.BadRequestFieldMessageEnum.*;
import static com.migu.tsg.microservice.atomicservice.ldap.enums.ResultErrorEnum.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;

import com.migu.tsg.microservice.atomicservice.ldap.biz.bo.LdapUser;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;
import com.migu.tsg.microservice.atomicservice.ldap.exception.BadRequestFieldException;

/**
* 项目名称: ldap-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.biz.mapper <br>
* 类名称: LdapUserMapper.java <br>
* 类描述: LDAP数据与用户实体对象映射关系类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:31:47 <br>
* 版本: v1.0
*/
public class LdapUserMapper implements AttributesMapper<LdapUser> {

    public static final String OBJECTCLASS = "objectClass";
    public static final String INETORGPERSON = "inetOrgPerson";
    public static final String UID = "uid";
    public static final String USERPASSWORD = "userPassword";
    public static final String EMPLOYEETYPE = "employeeType";
    public static final String OU = "ou";
    public static final String CN = "cn";
    public static final String SN = "sn";
    public static final String MOBILE = "mobile";
    public static final String MAIL = "mail";
    public static final String O = "o";
    public static final String DEPARTMENTNUMBER = "departmentNumber";
    public static final String JPEGPHOTO = "jpegPhoto";
    public static final String CREATETIME = "createTime";
    public static final String DESCRIPTION = "description";
    public static final String UPDATETIME = "updateTime";
    public static final String PROJECT = "project";
    public static final String CHARACTER_ENCODING = "UTF-8";

    public LdapUser mapFromAttributes(final Attributes attributes) throws NamingException {
        LdapUser user = new LdapUser();
        if (attributes == null) {
            return user;
        }
        Attribute attribute = null;
        attribute = attributes.get(UID);
        if (attribute != null) {
            user.setUsername((String) attribute.get());
        }
        attribute = attributes.get(USERPASSWORD);
        if (attribute != null) {
            try {
                user.setPassword(new String((byte[]) attribute.get(), CHARACTER_ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new BadRequestFieldException(INVALID_ARGS, "password",
                        new String[] { dynamicMessage(MAP_LDAP_USER_USERPASSWORD_FAILED, USERPASSWORD,
                                attribute.get() != null ? attribute.get().toString() : "",
                                CHARACTER_ENCODING) });
            }
        }
        // 用户类型
        attribute = attributes.get(EMPLOYEETYPE);
        if (attribute != null) {
            String value = (String) attribute.get();
            if (StringUtils.isBlank(value)) {
                throw new BadRequestFieldException(INVALID_ARGS, "user_type",
                        new String[] { dynamicMessage(MAP_LDAP_USER_EMPLOYEETYPE_FAILED, EMPLOYEETYPE) });
            }
            String employeetype = "";
            try {
                if (value.indexOf(".") != -1) {
                    String[] split = value.split("\\.");
                    user.setUserType(LdapUserTypeEnum.valueOf(split[0]));
                    employeetype = split[0];
                    if (split.length > 1) {
                        user.setNamespace(split[1]);// 根账号
                    }
                } else {
                    user.setUserType(LdapUserTypeEnum.valueOf(value));
                    employeetype = value;
                }
            } catch (Exception e) {
                throw new BadRequestFieldException(INVALID_ARGS, "user_type", new String[] {
                        dynamicMessage(MAP_LDAP_USER_EMPLOYEETYPE_FAILED2, EMPLOYEETYPE, employeetype) }, e);
            }
        }
        // 真实姓名
        attribute = attributes.get(SN);
        if (attribute != null) {
            user.setName((String) attribute.get());
        }
        attribute = attributes.get(MOBILE);
        if (attribute != null) {
            user.setMobile((String) attribute.get());
        }
        attribute = attributes.get(MAIL);
        if (attribute != null) {
            user.setMail((String) attribute.get());
        }
        attribute = attributes.get(DEPARTMENTNUMBER);
        if (attribute != null) {
            user.setDept((String) attribute.get());
        }
        attribute = attributes.get(O);
        if (attribute != null) {
            user.setCompany((String) attribute.get());
        }
        attribute = attributes.get(JPEGPHOTO);
        if (attribute != null) {
            try {
                user.setJpegPhoto(new String((byte[]) attribute.get(), CHARACTER_ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new BadRequestFieldException(INVALID_ARGS, "user_type",
                        new String[] { dynamicMessage(MAP_LDAP_USER_JPEGPHOTO_FAILED, JPEGPHOTO,
                                attribute.get() != null ? attribute.get().toString() : "",
                                CHARACTER_ENCODING) },
                        e);
            }
        }
        attribute = attributes.get(CREATETIME);
        if (attribute != null) {
            user.setCreateTime((String) attribute.get());
        }
        attribute = attributes.get(DESCRIPTION);
        if (attribute != null) {
            user.setDescription((String) attribute.get());
        }
        attribute = attributes.get(UPDATETIME);
        if (attribute != null) {
            user.setUpdateTime((String) attribute.get());
        }
        // 用户所属项目集合
        attribute = attributes.get(PROJECT);
        if (attribute != null) {
            List<String> projects = new ArrayList<>();
            for (int i = 0; i < attribute.size(); i++) {
                projects.add((String) attribute.get(i));
            }
            user.setProjects(projects);
        }
        return user;
    }
}
