package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;

/**
 * List supported application/cluster response
 * Project Name:composite-api
 * File Name:CatalogListResponse.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: CatalogListResponse <br/>
 * date: 2017年10月3日 上午11:17:33 <br/>
 * List supported application/cluster response
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
public class CatalogListResponse {

    private int count;
    private String next;
    private List<Result> result;

}
