package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Data
@NoArgsConstructor
public class CatalogAppsCreateRequest {

    private JSONObject basic_config;
    private JSONObject advanced_config;
    private String space_name;
    private String name;
    private String region;
    private String region_alb_version;
    private String namespace;
    private String eureka_port;
    private String knamespace;

    @Data
    @NoArgsConstructor
    static class BaseConfig {
        private int num_of_instances;
        private String cluster_size;
        private JSONArray node_tag;
        private String image_tag;
        private int internal_port;
        private JSONArray app_tag;
        private JSONArray pod_anti_affinity;
    }

    @Data
    @NoArgsConstructor
    static class AdvancedConfig {
        private String kafka_hosts;
        private String kafka_port;
        private String kafka_topic;
        private String zookeeper_hosts;
        private String zookeeper_port;
        private String config_server_rules;
        private String config_server_selector;
        private String zuul_route_rules;
    }
}
