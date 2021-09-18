package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.Date;
import lombok.Data;

/**
 * 创建负载均衡器响应model
 * @author qianchunhui
 * @version
 * @since JDK 1.6
 */
@Data
public class CompLbCreateResponse {
	private String id;
	private String name;
	private String regionId;
	private String type;
	private String imageName;
	private String domainName;
	private String blockedPorts;
	private String version;
	private String knamespace;
	private Date createdAt;
	private Date updatedAt;

}
