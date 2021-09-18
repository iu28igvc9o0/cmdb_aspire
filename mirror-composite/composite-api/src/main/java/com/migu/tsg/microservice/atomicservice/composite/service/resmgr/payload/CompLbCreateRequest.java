package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;

/**
 * 创建服务请求model
 *
 * @author qianchunhui
 * @since JDK 1.6
 */
@Data
public class CompLbCreateRequest {
    private String name;
    private String regionId;
    private String region_name;
    private String type;
    private String imageName;
    private String domainName;
    private String blockedPorts;
    private String version;
    private String space_name;
    private String knamespace;
}
