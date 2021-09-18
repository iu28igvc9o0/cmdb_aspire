package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONObject;

/**
* Create an application/cluster request
* Project Name:composite-api
* File Name:CreateCatalogRrquest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CreateCatalogRrquest <br/>
* date: 2017年10月3日 下午2:54:27 <br/>
* Create an application/cluster request
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data
@NoArgsConstructor
public class CreateCatalogRrquest {
    
    private String name;
    private String region;
    private JSONObject basic_config;
    private JSONObject advanced_config;
    private String knamespace;

    @Data
    @NoArgsConstructor
    static class Basicconfig{
        private int num_of_nodes;
        private String cluster_size;
        private List<String> ip_tag;
    }
    
    @Data
    @NoArgsConstructor
    static class AdvancedConfig{
        private String data_dir;
        private int tick_time;
        private int init_limit;
    }
    
}
