package com.aspire.ums.cmdb.resource.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResourcePreDistribution {

    private String id;
    private String primary_department ; //一级部门
    private String secondary_department; // 二级部门
    private String app_system;//应用系统
    private Integer vm_model1; // 16核、128G、200G系统磁盘
    private Integer vm_model2; //  8核、64G、200G系统磁盘
    private Integer vm_model3; //  8核、32G、200G系统磁盘
    private Integer vm_model4; //  8核、16G、200G系统磁盘
    private Integer vm_model5; //  4核、32G、200G系统磁盘
    private Integer vm_model6; //  4核、16G、200G系统磁盘
    private Integer vm_model7; //  4核、8G、200G系统磁盘
    private Integer vm_model8; //  2核、4G、200G系统磁盘
    private Integer pm_low_app_server; // 虚拟机宿主机低端应用服务器（台）
    private Integer pm_analytical_server; //分析型服务器（MPP服务器）（台）
    private Integer pm_distributed_server; //分布式服务器（Hadoop服务器）（台）
    private Integer pm_cache_server;//缓存型服务器（台）
    private Integer pm_high_app_server; // 高端应用服务器（台）
    private Double storage_distributd_file_capacity;//分布式文件存储（TB）
    private Double storage_distributd_block_performance;//分布式块存储（TB）
    private Double storage_object_capacity;//对象存储（TB）
    private Double storage_fc_san_capacity;//FC-SAN存储（TB）
    private Double storage_ip_san_capacity;//IP-SAN存储（TB）
    private Double storage_fc_san_naked;//FC-SAN裸盘（TB）
    private Double storage_ip_san_naked;//IP-SAN裸盘（TB）
    private Double storage_back_up;//备份存储（TB）
    private Integer cmnet_address; // CMNET地址需求（个）
    private Double bandwidth_gbps_cmnet;//至CMNET带宽（Gbps）
    private Integer ip_address; // CMNET地址需求（个）
    private Double bandwidth_gbps_ip;//至IP专网带宽（Gbps）
    private Integer db_mysql ;
    private Integer db_mongodb;
    private Integer db_postgresql;
    private Integer db_memory;
    private Integer db_other;
    private Integer cache_redis;
    private Integer cache_memcached;
    private Integer info_middleware_activemq;
    private Integer info_middleware_kafka;
    private Integer info_middleware_rocketmq;
    private Integer app_middleware_apache;
    private Integer app_middleware_jboos;
    private Integer app_middleware_tomcat;
    private Integer balancer_nginx;
    private Integer balancer_haproxy;
    private Integer server_zookeeper;
    private Integer server_etcd;
    private Integer search_middleware_es;
    private Integer server_docker_registry;
    private Integer stream_jbpm;
    private Integer stream_activity;
    private Integer image_openjdk;
    private Integer image_python;
    private Integer image_go;
    private Integer image_nodejs;
    private Integer image_ruby;
    private Integer image_net;
    private Integer cicd_jenkins;
    private Integer framework_springcloud;
    private Integer framework_other;
    private String status;
    private String create_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private String update_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_time;
    private String resource_pool;
    private String remark;

}
