package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
* 服务更新请求model
* Project Name:composite-api
* File Name:CompServiceUpdateRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
* ClassName: CompServiceUpdateRequest <br/>
* date: 2017年9月15日 下午3:58:04 <br/>
* 服务更新请求model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompServiceUpdateRequest {
    private String custom_domain_name;
    private String run_command;
    private String entrypoint;
    private String image_tag;
    private String instance_size;
    
    @JsonProperty("custom_instance_size")
    private InstanceSize customInstSize;
    
    private String node_tag;
    private String pod_controller;
    private String pod_labels;
    @JsonProperty("node_selector")
    private NodeSelector nodeSelector;
    
    @JsonProperty("envfiles")
    private List<CompositeServiceEnvFile> envfiles;
    
    @JsonProperty("instance_envvars")
    private Map<String, String> insEnvVars;
    
    private List<HealthCheck> health_checks;
    
    private AutoScaleConfig autoscaling_config;
    
    private List<MountPoint> mount_points;
    
    private Map<String, Object> update_strategy;
    
    private String scaling_mode;
    private String namespace;
    private String service_name;
    private String target_num_instances;
    
    /**
     * InstanceSize对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateRequest.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:37:23 <br/>
     * InstanceSize对象
     * @author pengguihua
     * @version CompServiceUpdateRequest
     * @since JDK 1.6
     */
    @Data
    @JsonInclude(Include.NON_NULL)
    private static class InstanceSize {
        private float cpu;
        private int mem;
    }
    
    /**
     * NodeSelector对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateRequest.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:37:23 <br/>
     * NodeSelector对象
     * @author pengguihua
     * @version CompServiceUpdateRequest
     * @since JDK 1.6
     */
    @Data
    private static class NodeSelector {
        private String ip;
    }
    
    /**
     * EnvFile对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateRequest.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:37:23 <br/>
     * EnvFile对象
     * @author pengguihua
     * @version CompServiceUpdateRequest
     * @since JDK 1.6
     */
    @Data
    @JsonInclude(Include.NON_NULL)
    public static class EnvFile {
        private String name;
        private Map<String, String> content;
    } 
    
    /**
     * HealthCheck对象
     * Project Name:composite-api
     * File Name:CompServiceUpdateRequest.java
     * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
     * ClassName: MountPoint <br/>
     * date: 2017年9月15日 下午3:37:23 <br/>
     * HealthCheck对象
     * @author pengguihua
     * @version CompServiceUpdateRequest
     * @since JDK 1.6
     */
    @Data
    @JsonInclude(Include.NON_NULL)
    private static class HealthCheck {
        private String protocol;
        private String health_type;
        private String command;
        private String path;
        private int port;
        private int grace_period_seconds;
        private int interval_seconds;
        private int max_consecutive_failures;
        private int timeout_seconds;
        private boolean ignore_http1xx;
    } 

    /**
    * AutoScaleConfig对象
    * Project Name:composite-api
    * File Name:CompServiceUpdateRequest.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
    * ClassName: MountPoint <br/>
    * date: 2017年9月15日 下午3:37:23 <br/>
    * AutoScaleConfig对象
    * @author pengguihua
    * @version CompServiceUpdateRequest
    * @since JDK 1.6
    */
    @Data
    @JsonInclude(Include.NON_NULL)
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
    * File Name:CompServiceUpdateRequest.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr.payload
    * ClassName: MountPoint <br/>
    * date: 2017年9月15日 下午3:37:23 <br/>
    * MountPoint对象
    * @author pengguihua
    * @version CompServiceUpdateRequest
    * @since JDK 1.6
    */
    @Data
    @JsonInclude(Include.NON_NULL)
    public static class MountPoint {
        private String type;
        private String path;
//        private Value value;
        private Object value;
    }
}
