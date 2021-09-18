/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.client;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.migu.tsg.microservice.atomicservice.common.exception.LdapInterfaceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.client <br>
* 类名称: FeignExceptionDecoder.java <br>
* 类描述: FeignClient调用HttpStatus异常解析<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月10日下午4:03:09 <br>
* 版本: v1.0
*/
@Component
public class FeignExceptionDecoder implements ErrorDecoder {

    public Exception decode(String methodKey, Response response) {
        String respBody = null;
        try {
            respBody = Util.toString(response.body().asReader());
        } catch (IOException e) {
        }
        return new LdapInterfaceException(response.status(), respBody);
    }

}
