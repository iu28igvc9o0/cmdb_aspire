package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
* 加载服务详情信息响应model
* Project Name:composite-api
* File Name:RetrieveResServiceDetaileResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: RetrieveResServiceDetaileResponse <br/>
* date: 2017年9月15日 下午3:40:04 <br/>
* 加载服务详情信息响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class RetrieveResServiceDetaileResponse {
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
    private JSONArray envfiles;
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
    
    @JsonProperty("service_name")
    private String serviceName;
    
    private String unique_name;
    private String namespace;
    private String knamespace;
    private String space_name;
    private final Map<String, Object> region = new HashMap<String, Object>();
    private String exec_endpoint;
    private String application;
    private String application_name;
    private String application_uuid;
    private String knamespace_uuid;
    private String space_uuid;
    private String project_uuid;
    private String project_name;
    private List<String> resource_actions;
    
    private Map<String, Object> update_strategy;
    private Map<String, Object> kube_config;
    
    private JSONArray mount_points;

    private JSONArray podLabels;
    
    @JsonGetter("update_strategy")
    public Map<String, Object> getUpdateStrategy() {
        return update_strategy != null ? update_strategy : new HashMap<String, Object>();
    }
    @JsonGetter("kube_config")
    public Map<String, Object> getKubeConfig() {
        return kube_config != null ? kube_config : new HashMap<String, Object>();
    }
}
