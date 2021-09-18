package com.migu.tsg.microservice.atomicservice.ldap.biz.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 项目名称: LDAP-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.LDAP.biz.bo <br>
 * 类名称: ValidCode.java <br>
 * 类描述: LDAP验证码实体类<br>
 * 创建人: Hangfang <br>
 * 创建时间: 2018年12月11日下午4:26:29 <br>
 * 版本: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidCode implements Serializable {

    /*实际验证码*/
    private String code;

    /*存储时间*/
    private Timestamp date;
}
