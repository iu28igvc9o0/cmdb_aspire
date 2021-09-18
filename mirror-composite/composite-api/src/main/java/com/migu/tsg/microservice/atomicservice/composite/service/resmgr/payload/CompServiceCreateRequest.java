package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
* 创建服务请求model
* Project Name:composite-api
* File Name:CompServiceCreateRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: CompServiceCreateRequest <br/>
* date: 2017年9月15日 下午3:34:21 <br/>
* 创建服务请求model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompServiceCreateRequest {
    private String uuid;
    private JSONArray mount_points;
    private String service_name;
    private String target_state;
    private JSONObject instance_envvars;
    private String knamespace;
    private String knamespace_uuid;
    private String space_name;
    private String space_uuid;
    private Object linked_from_apps;
    private Object linked_to_apps;
    private Object linked_to_services;
    private List<CompositeServiceEnvFile> envfiles;
    private String namespace;
    private String version;
    private String entrypoint;
    private String run_command;
    private String region_name;
    private String scaling_mode;
    private String image_name;
    private String network_mode;
    private JSONArray health_checks;
    private JSONArray load_balancers;
    private String target_num_instances;
    private JSONArray instances;
    private String instance_size;
    private JSONArray volumes;
    private String image_tag;
    private String[] ports;
    private String subnet_id;
    private String custom_domainName;
    private String container_manager;
    private String alauda_load_balancer;
    private JSONObject subnet;
    private Object autoscaling_config;
    private JSONArray raw_container_port;
    private String host_port;
    private String network_port_type;
    private String node_port;
    private String node_alone;
    private String projectId;
    private String serv_status_type;
    private String entry_point_type;
    private String node_tag;
    private String pod_controller;
    private String pod_labels;
    private JSONObject custom_instance_size;
    private String create_by;
}
