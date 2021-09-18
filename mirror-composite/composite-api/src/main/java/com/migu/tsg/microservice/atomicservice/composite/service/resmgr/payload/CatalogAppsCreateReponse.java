package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;

@Data
@NoArgsConstructor
public class CatalogAppsCreateReponse {

    private String space_uuid;
    private String project_name;
    private String uuid;
    private String region_name;
    private String project_uuid;
    private String region_uuid;
    private String region;
    private String created_by;
    private JSONArray services;
    private String operator;
    private String space_name;
    private String operation;
    private String app_name;

    @Data
    @NoArgsConstructor
    static class Service {
        private String service_name;
        private String uuid;
        private String region_id;
    }

}
