package com.aspire.ums.cmdb.vo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * @Author: huanggongrui
 * @Description: LDAP数据与用户实体对象映射关系类
 * @Date: create in 2020/8/17 14:47
 */
@Slf4j
public class KgLdapUserMapper implements AttributesMapper<KgLdapUser> {

    public static final String UID = "uid";
    public static final String EMPLOYEETYPE = "employeeType";
    public static final String EMPLOYEENUMBER = "employeeNumber";
    public static final String DISPLAYORDER = "displayOrder";
    public static final String BIRTHDAY = "birthday";
    public static final String OU = "ou";
    public static final String CN = "cn";
    public static final String SN = "sn";
    public static final String MOBILE = "mobile";
    public static final String MAIL = "mail";
    public static final String O = "o";
    public static final String JPEGPHOTO = "jpegPhoto";
    public static final String GENDER = "gender";
    public static final String ENDTIME = "endTime";
    public static final String PMODIFIEDDATE = "passwordModifiedDate";
    public static final String POSITIONLEVEL = "positionLevel";
    public static final String ROLE = "role";
    public static final String STARTTIME = "startTime";
    public static final String STATUS = "status";
    public static final String TELTPHONENUMBER = "telephoneNumber";
    public static final String USERP = "userpassword";

    @Override
    public KgLdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        KgLdapUser user = new KgLdapUser();
        if (attributes == null) {
            return user;
        }
        user.setCn(attributes.get(CN) == null ? null: (String) attributes.get(CN).get());
        user.setO(attributes.get(O) == null ? null: (String) attributes.get(O).get());
        user.setSn(attributes.get(SN) == null ? null: (String) attributes.get(SN).get());
        user.setUid(attributes.get(UID) == null ? null: (String) attributes.get(UID).get());
        user.setEmployeeType(attributes.get(EMPLOYEETYPE) == null ? null: (String) attributes.get(EMPLOYEETYPE).get());
        user.setEmployeeNumber(attributes.get(EMPLOYEENUMBER) == null ? null: (String) attributes.get(EMPLOYEENUMBER).get());
        user.setDisplayOrder(attributes.get(DISPLAYORDER) == null ? null: (String) attributes.get(DISPLAYORDER).get());
        user.setBirthday(attributes.get(BIRTHDAY) == null ? null: (String) attributes.get(BIRTHDAY).get());
        user.setOu(attributes.get(OU) == null ? null: (String) attributes.get(OU).get());
        user.setMobile(attributes.get(MOBILE) == null ? null: (String) attributes.get(MOBILE).get());
        user.setMail(attributes.get(MAIL) == null ? null: (String) attributes.get(MAIL).get());
        // user.setJpegPhoto(attributes.get(JPEGPHOTO) == null ? null: (String) attributes.get(JPEGPHOTO).get());
        user.setGender(attributes.get(GENDER) == null ? null: (String) attributes.get(GENDER).get());
        user.setEndTime(attributes.get(ENDTIME) == null ? null: (String) attributes.get(ENDTIME).get());
        user.setPasswordModifiedDate(attributes.get(PMODIFIEDDATE) == null ? null: (String) attributes.get(PMODIFIEDDATE).get());
        user.setPositionLevel(attributes.get(POSITIONLEVEL) == null ? null: (String) attributes.get(POSITIONLEVEL).get());
        user.setRole(attributes.get(ROLE) == null ? null: (String) attributes.get(ROLE).get());
        user.setStartTime(attributes.get(STARTTIME) == null ? null: (String) attributes.get(STARTTIME).get());
        user.setStatus(attributes.get(STATUS) == null ? null: (String) attributes.get(STATUS).get());
        user.setTelephoneNumber(attributes.get(TELTPHONENUMBER) == null ? null: (String) attributes.get(TELTPHONENUMBER).get());
        Attribute attribute = attributes.get(USERP);
        if (attribute != null && attribute.get() != null) {
            String s = new String((byte[]) attributes.get(USERP).get());
//            if (s.indexOf("}") > 0) {
//                user.setUserPassword(s.split("}")[1]);
//            } else {
                user.setUserPassword(s);
//            }
        }
        return user;
    }
}
