package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Get collection of applications response
 * Project Name:composite-api
 * File Name:CatalogCollection.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: CatalogCollection <br/>
 * date: 2017年10月3日 下午2:32:56 <br/>
 * Get collection of applications response
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class CatalogCollection {
    private List<NameStatus> testint;
    private List<NameStatus> crawlertest1;

    @Data
    @NoArgsConstructor
    static class NameStatus {
        private String name;
        private String status;
    }
}
