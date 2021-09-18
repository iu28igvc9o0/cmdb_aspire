package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {
    private String app_name;
    private String avavtar;
    private String uuid;
    private String display_name;
    private Info display_info;
    private int app_num;
    private List<String> type;
    
    @Data
    @NoArgsConstructor
    static class Info{
        private String en;
        private String zh;
    }
    
}
