package com.migu.tsg.microservice.atomicservice.composite.clientservice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;


/**
* FeignClient调用HttpStatus异常解析
* Project Name:composite-service
* File Name:FeignExceptionDecoder.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientService
* ClassName: FeignExceptionDecoder <br/>
* date: 2017年8月29日 下午7:29:15 <br/>
* FeignClient调用HttpStatus异常解析
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public class FeignExceptionDecoder implements ErrorDecoder {
    private final Logger logger = LoggerFactory.getLogger(FeignExceptionDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        String respBody = null;
        try {
            respBody = Util.toString(response.body().asReader());
        } catch (IOException e) {
            logger.error(null, e);
        }
        return new ClientServiceHttpStatusErrorException(response.status(), respBody);
    }

}
