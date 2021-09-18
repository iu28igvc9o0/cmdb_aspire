package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;

@Data
@NoArgsConstructor
public class CatalogAppnameRrsponse {

    private String name;
    private String avatar;
    private Info info;
    private Info instruction;
    private JSONArray basic_config;
    private JSONArray advanced_config;

    @Data
    @NoArgsConstructor
    static class Info {
        private String en;
        private String zh;
    }

}
