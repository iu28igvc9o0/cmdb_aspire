package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 获取负载均衡器列表
* @author qianchunhui
* @version 
* @since JDK 1.8
*/
@Data    
@NoArgsConstructor
@AllArgsConstructor
public class LbListResponse {
    private String regionId;
    private String regionName;
    private String id;
    private String name;
    private String type;
    private String imageName;
    private String domainName;
    private String blockedPorts;
    private String version;
    private Date createdAt;
    private Date updatedAt;
    private String cteated_by;
    private String knamespace;
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
}
