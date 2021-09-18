package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.Map;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* create集群model
* Project Name:composite-api
* File Name:CompRegionCreateRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionCreateRequest <br/>
* date: 2017年9月18日 下午9:08:58 <br/>
* create集群model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionCreateRequest {
    private String container_manager;
    private Map<String, Object> features;
    private Map<String, Object> attr;
    
    @NotEmpty(message="Region name should not be blank!")
    @Pattern(regexp="^[a-z][a-z0-9_]*", message="The region name should only contain characters of "
                                               +"a~z, 0~9, or _ , and it should only start with a-z")
    private String name;

    @NotEmpty(message="Region display-name should not be blank!")
    private String display_name;

    @NotEmpty(message="namespace should not be blank!")
    private String namespace;
    
    private String env_uuid;
    private Integer over_commit;
    private Map<String,Object> meta_info;
}
