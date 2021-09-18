package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Get application label 响应类
 * Project Name:composite-api
 * File Name:ApplicationLabel.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: ApplicationLabel <br/>
 * date: 2017年9月21日 上午10:16:22 <br/>
 * Get application label 响应类
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationLabel {
    private String key;
    private String value;
    private boolean editable;

}
