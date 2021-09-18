package com.aspire.mirror.composite.service.scada.payload;


import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller
 * 类名称:    Node.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/12/16 15:32
 * 版本:      v1.0
 */
@Data
public class ComPathRequest {
    private String content;
    private String sourceIp;
    private String destIp;
}
