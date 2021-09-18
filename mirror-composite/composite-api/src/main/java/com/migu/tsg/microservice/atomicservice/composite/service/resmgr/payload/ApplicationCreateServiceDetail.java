package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Data
public class ApplicationCreateServiceDetail {
    private String action;
    private String application_uuid;
    private JSONObject autoscaling_config;
    private String current_status;
    private JSONObject custom_instance_size;
    private String image_name;
    private String image_tag;
    private String instance_size;
    private String region_id;
    private List<String> resource_actions;
    private String service_name;
    private String uuid;
    private String target_num_instances;

    private JSONArray health_checks;
    private String healthy_num_instances;
    private String health_status;
    private String knamespace;
    private JSONArray instances;
}
