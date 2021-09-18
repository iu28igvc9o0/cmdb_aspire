package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
* 创建服务响应model
* Project Name:composite-api
* File Name:CompServiceCreateResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: CompServiceCreateResponse <br/>
* date: 2017年9月15日 下午3:34:47 <br/>
* 创建服务响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompServiceCreateResponse {
    private String region_id;
    private String uuid;
    private String subnet_id;
    private String target_state;
    private String current_status;
    // serviceVolume
    private JSONArray volumes;
    private String[] ports;
    private JSONArray raw_container_ports;
    private String network_mode;
    private JSONObject autoscaling_config;
    private String scaling_mode;
    private String current_num_instances;
    private String target_num_instances;
    private JSONArray instances;
    private JSONObject instance_envvars;
    @JsonProperty("envfiles")
    private List<CompositeServiceEnvFile> envfiles;
    private JSONObject linked_from_apps;
    private JSONObject linked_to_apps;
    private JSONArray linked_to_services;
    private String created_at;
    private String updated_at;
    private String last_autoscale_datetime;
    private JSONArray health_checks;
    private String healthy_num_instances;
    private String health_status;
    private String image_name;
    private String image_tag;
    private String entrypoint;
    private String run_command;
    private String instance_size;
    private JSONObject custom_instance_size;
    private String node_tag;
    private String pod_controller;
    private String pod_labels;
    private String service_name;
    private String knamespace;
    private String space_name;
    private JSONArray podLabels;
}
