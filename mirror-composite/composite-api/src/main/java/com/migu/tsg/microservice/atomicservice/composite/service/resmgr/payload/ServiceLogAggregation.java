package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BucketInfoDTO;
import lombok.Data;

/**
* Get Service Aggregation Response
* Project Name:composite-api
* File Name:ServiceLogAggregation.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: ServiceLogAggregation <br/>
* date: 2017年10月1日 下午4:52:17 <br/>
* Get Service Aggregation Response
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data    
public class ServiceLogAggregation {
    
    private List<BucketInfoDTO> buckets;

}
