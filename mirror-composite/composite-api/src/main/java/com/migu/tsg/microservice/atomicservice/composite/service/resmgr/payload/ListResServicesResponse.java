package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
* 服务列表查询响应model
* Project Name:composite-api
* File Name:ListResServicesResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: ListResServicesResponse <br/>
* date: 2017年9月15日 下午3:39:42 <br/>
* 服务列表查询响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ListResServicesResponse {
    private int count;
    
    private List<ListResServiceResult> results = new ArrayList<ListResServiceResult>();
    
    
    /**
    * 资源展示model
    * Project Name:composite-api
    * File Name:ListResServicesResponse.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
    * ClassName: ListResServiceResult <br/>
    * date: 2017年9月15日 下午4:36:01 <br/>
    * 资源展示model
    * @author pengguihua
    * @version ListResServicesResponse
    * @since JDK 1.6
    */
    @Data
    public static class ListResServiceResult {
        @JsonProperty("region_id")
        private String regionId;
        
        private String uuid;
        
        private String unique_name;
        
        private String namespace;
        
        @JsonProperty("space_id")
        private String spaceId;
        
        @JsonProperty("space_name")
        private String spaceName;
        
        @JsonProperty("knamespace")
        private String knamespaceName;
        
        @JsonProperty("subnet_id")
        private String subnetId;
        
        @JsonProperty("target_state")
        private String targetState;
        
        @JsonProperty("current_status")
        private String currentStatus;
        // serviceVolume
        private JSONArray volumes;
        private String[] ports;
        
        @JsonProperty("raw_container_ports")
        private JSONArray rawContainerPorts;
        
        @JsonProperty("network_mode")
        private String networkMode;
        
        @JsonProperty("autoscaling_config")
        private JSONObject autoscalingConfig;
        
        @JsonProperty("scaling_mode")
        private String scalingMode;
        
        @JsonProperty("current_num_instances")
        private String currentNumInstances;
        
        @JsonProperty("target_num_instances")
        private String targetNumInstances;
        
        private JSONArray instances;
        
        @JsonProperty("instance_envvars")
        private JSONObject instanceEnvvars;
        
        private JSONArray envfiles;
        
        @JsonProperty("linked_from_apps")
        private JSONObject linkedFromapps;
        
        @JsonProperty("linked_to_apps")
        private JSONObject linked2apps;
        
        @JsonProperty("linked_to_services")
        private JSONArray linked2services;
        
        @JsonProperty("created_at")
        private String createdAt;
        
        @JsonProperty("updated_at")
        private String updatedAt;
        
        @JsonProperty("last_autoscale_datetime")
        private String lastAutoscaleDatetime;
        
        @JsonProperty("health_checks")
        private JSONArray healthChecks;
        
        @JsonProperty("healthy_num_instances")
        private String healthyNumInstances;
        
        @JsonProperty("health_status")
        private String healthStatus;
        
        @JsonProperty("image_name")
        private String imageName;
        
        @JsonProperty("image_tag")
        private String imageTag;
        
        
        private String entrypoint;
        
        @JsonProperty("run_command")
        private String runCommand;
        
        @JsonProperty("instance_size")
        private String instanceSize;
        
        @JsonProperty("custom_instance_size")
        private JSONObject customInstanceSize;
        
        @JsonProperty("node_tag")
        private String nodeTag;
        
        @JsonProperty("service_name")
        private String serviceName;
        
        @JsonProperty("resource_actions")
        private List<String> resourceActions;

        private JSONArray podLabels;
    }
}
