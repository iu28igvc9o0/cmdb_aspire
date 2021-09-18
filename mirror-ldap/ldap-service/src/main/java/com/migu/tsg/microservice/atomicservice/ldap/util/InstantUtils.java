/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.ldap.util;

import java.time.Instant;

/**
* 项目名称: ldap-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.util <br>
* 类名称: InstantUtils.java <br>
* 类描述: 日期时间工具类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月7日下午1:19:13 <br>
* 版本: v1.0
*/
public class InstantUtils {

    /**
     * 获取当前时间的UTC格式的日期时间字符串
     * @return UTC格式的日期时间字符串
     */
    public static String now() {
        return Instant.now().toString();
    }
}
