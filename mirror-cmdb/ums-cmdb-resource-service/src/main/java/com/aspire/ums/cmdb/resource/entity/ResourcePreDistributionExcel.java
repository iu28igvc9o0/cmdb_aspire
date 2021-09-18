package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourcePreDistributionExcel {
	
	
	private String id;
	private String primary_department ; //一级部门
    private String secondary_department; // 二级部门
    private String app_system;//应用系统
	private String vm_model1; // 16核、128G、200G系统磁盘
	private String vm_model2; //  8核、64G、200G系统磁盘
	private String vm_model3; //  8核、32G、200G系统磁盘
	private String vm_model4; //  8核、16G、200G系统磁盘
	private String vm_model5; //  4核、32G、200G系统磁盘
	private String vm_model6; //  4核、16G、200G系统磁盘
	private String vm_model7; //  4核、8G、200G系统磁盘
	private String vm_model8; //  2核、4G、200G系统磁盘
	private String pm_low_app_server; // 虚拟机宿主机低端应用服务器（台）
    private String pm_analytical_server; //分析型服务器（MPP服务器）（台）
    private String pm_distributed_server; //分布式服务器（Hadoop服务器）（台）
    private String pm_cache_server;//缓存型服务器（台）
    private String pm_high_app_server; // 高端应用服务器（台）
    private String storage_distributd_file_capacity;//分布式文件存储（TB）
    private String storage_distributd_block_performance;//分布式块存储（TB）
    private String storage_object_capacity;//对象存储（TB）
    private String storage_fc_san_capacity;//FC-SAN存储（TB）
	private String storage_ip_san_capacity;//IP-SAN存储（TB）
    private String storage_fc_san_naked;//FC-SAN裸盘（TB）
	private String storage_ip_san_naked;//IP-SAN裸盘（TB）
    private String storage_back_up;//备份存储（TB）
    private String cmnet_address; // CMNET地址需求（个）
    private String bandwidth_gbps_cmnet;//至CMNET带宽（Gbps）
	private String ip_address; // CMNET地址需求（个）
    private String bandwidth_gbps_ip;//至IP专网带宽（Gbps）
	private String db_mysql ;
	private String db_mongodb;
	private String db_postgresql;
	private String db_memory;
	private String db_other;
	private String cache_redis;
	private String cache_memcached;
	private String info_middleware_activemq;
	private String info_middleware_kafka;
	private String info_middleware_rocketmq;
	private String app_middleware_apache;
	private String app_middleware_jboos;
	private String app_middleware_tomcat;
	private String balancer_nginx;
	private String balancer_haproxy;
	private String server_zookeeper;
	private String server_etcd;
	private String search_middleware_es;
	private String server_docker_registry;
	private String stream_jbpm;
	private String stream_activity;
	private String image_openjdk;
	private String image_python;
	private String image_go;
	private String image_nodejs;
    private String image_ruby;
	private String image_net;
	private String cicd_jenkins;
	private String framework_springcloud;
	private String framework_other;
   
    private String status;
    
    private String resource_pool;


}
