package com.migu.tsg.microservice.atomicservice.composite.clientservice;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
* 服务客户端调用异常包装类
* Project Name:composite-service
* File Name:ClientServiceHttpStatusErrorException.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientService
* ClassName: HttpStatusMarkException <br/>
* date: 2017年8月29日 下午7:07:17 <br/>
* 服务客户端调用异常包装类
* @version 
* @since JDK 1.6
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientServiceHttpStatusErrorException extends RuntimeException {
    private static final long serialVersionUID = 5539724724595671080L;
    private int httpCode;
    private String errorTip;
    
    public ClientServiceHttpStatusErrorException(int httpCode, String errorTip) {
        this.httpCode = httpCode;
        this.errorTip = errorTip;
    }
    
    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder("[httpCode = ");
        msg.append(httpCode).append(", errorTip = ").append(errorTip).append("]");
        return msg.toString();
    }
}
