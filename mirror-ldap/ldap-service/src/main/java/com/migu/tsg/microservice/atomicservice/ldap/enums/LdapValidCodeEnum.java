package com.migu.tsg.microservice.atomicservice.ldap.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称: ldap-api <br>
 * 包: com.migu.tsg.msp.microservice.atomicservice.ldap.enums <br>
 * 类名称: LdapValidCodeEnum.java <br>
 * 类描述: 验证码反馈代码类型<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年10月13日上午9:22:21 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
public enum LdapValidCodeEnum {

    VALID_CODE_CHECK_TIME_OUT("TIME_OUT", " valid code is time out"),

    VALID_CODE_CHECK_ERROR("ERROR", " valid code is error"),

    VALID_CODE_CHECK_CORRECT("CORRECT", " valid code is correct");


    /**
     * 返回代码
     */
    @Getter
    private String callbackCode;

    /**
     * 返回码描述
     */
    @Getter
    private String describe;

}
