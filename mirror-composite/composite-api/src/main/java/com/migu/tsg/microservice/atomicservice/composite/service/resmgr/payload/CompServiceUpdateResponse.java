package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;


/**
* 更新服务响应model
* Project Name:composite-api
* File Name:CompServiceUpdateResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: CompServiceUpdateResponse <br/>
* date: 2017年9月14日 下午1:52:08 <br/>
* 更新服务响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompServiceUpdateResponse {
    private String region_id;
    private List<Object> mount_points;
    private String updated_at;
    private Map<String, Object> update_strategy;
    private String target_state;
    private Map<String, Object> instance_envvars;
    private String service_namespace;
    private String uuid;
    private Map<String, Object> linked_to_apps;
    
    @JsonProperty("custom_instance_size")
    private InstanceSize customInstSize;
    
    private String subnet_id;
    
    @JsonProperty("envfiles")
    private List<CompositeServiceEnvFile> envfiles;
    
    private String entrypoint;
    
    private AutoScaleConfig autoscaling_config;
    
    private String run_command;
    private String space_id;
    private String custom_domain_name;
    private List<Object> linked_to_services;
    private List<Object> raw_container_ports;
    private String healthy_num_instances;
    private String scaling_mode;
    private String image_name;
    private String network_mode;
    private List<Object> health_checks;
    private Object linked_from_apps;
    private List<Object> load_balancers;
    private String target_num_instances;
    private String current_num_instances;
    private List<Map<String, Object>> instances;
    private String created_at;
    private String last_autoscale_datetime;
    private String container_manager;
    private String current_status;
    private List<Integer> ports;
    private List<Object> volumes;
    private Boolean is_stateful;
    private String image_tag;
    private String instance_size;
    private Boolean mipn_enabled;
    private String node_tag;
    private String pod_controller;
    private String pod_labels;
    @JsonGetter("update_strategy")
    public Map<String, Object> getUpdateStrategy() {
        return update_strategy != null ? update_strategy : new HashMap<String, Object>();
    }
    
    @JsonSetter("update_strategy")
    public void setUpdateStrategy(Map<String, Object> update_strategy) {
        this.update_strategy = update_strategy;
    }
    
    
    /**
     * InstanceSize对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateResponse.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:38:48 <br/>
     * InstanceSize对象
     * @author pengguihua
     * @version CompServiceUpdateResponse
     * @since JDK 1.6
     */
    @Data
    private static class InstanceSize {
        private float cpu;
        private int mem;
    }
    
    /**
     * AutoScaleConfig对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateResponse.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:38:48 <br/>
     * AutoScaleConfig对象
     * @author pengguihua
     * @version CompServiceUpdateResponse
     * @since JDK 1.6
     */
    @Data
    private static class AutoScaleConfig {
        private String metric_name;
        private String metric_stat;
        private float upper_threshold;
        private int decrease_delta;
        private int increase_delta;
        private int minimum_num_instances;
        private int maximum_num_instances;
        private int wait_period;
    } 
    
    /**
    * MountPoint对象
    * Project Name:composite-api
    * File Name:CompServiceUpdateResponse.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
    * ClassName: MountPoint <br/>
    * date: 2017年9月15日 下午3:38:48 <br/>
    * MountPoint对象
    * @author pengguihua
    * @version CompServiceUpdateResponse
    * @since JDK 1.6
    */
    @Data
    private static class MountPoint {
        private String type;
        private String path;
        private String value;
    } 
}
