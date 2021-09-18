package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompApplicationCreateRequest {
    
//    private String app_name;
//    private String region;
    private String knamespace;
    private String namespace;
}
