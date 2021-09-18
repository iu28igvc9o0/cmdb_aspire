package com.aspire.ums.cmdb.demand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DemandExcel {
	
	
/*    private String id;
    private String primary_department ; //一级部门
    private String secondary_department; // 二级部门
    private String app_system;//应用系统
	private String is_project; //是否立项
	private String project_time;//立项时间
	private String resource_put_date;//资源提出时间
	private String resource_produce_date;//资源预期投资时间
    private String system_description;//系统描述
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
	private String special_requirement;//与前期上报需求有无变化，如有，请说明原因
	private String address_requirement;// 物理局址要求
    private String department_head;//部门负责人
	private String inter_contact;//接口联系人
	private String phone;//电话
	private String email;*/
	
	private String department;//部门
	private String tenant;//需求接口人
	private String tenant_phone;//需求接口人电话
	private String biz_system;//应用系统
	private String is_project;//是否立项
	private String project_time;//立项时间
	private String submit_time;//资源需求提出时间
	private String cycle_time;//需求满足周期
	private String is_host_maintenance;//是否主机代维
	private String is_disaster;//是否有容灾高可用
	private String disaster_type;//容灾类型
	private String wlan_requirement;//宽度要求
	private String commission_time;//资源预期投产时间
	private String is_idc_requirement;//部署资源池要求
	private String idc_requirement;//资源池要求
	private String vm_1;//16核、128G、200G系统磁盘
	private String vm_1_scene;//16核、128G、200G系统磁盘使用场景描述
	private String vm_1_use;//16核、128G、200G系统磁盘资源用途
	private String vm_2;//8核、32G、200G系统磁盘
	private String vm_2_scene;//8核、32G、200G系统磁盘使用场景描述
	private String vm_2_use;//8核、32G、200G系统磁盘资源用途
	private String vm_3;//4核、16G、200G系统磁盘
	private String vm_3_scene;//4核、16G、200G系统磁盘使用场景描述
	private String vm_3_use;//4核、16G、200G系统磁盘资源用途
	private String vm_4;//8核、16G、200G系统磁盘
	private String vm_4_scene;//8核、16G、200G系统磁盘使用场景描述
	private String vm_4_use;//8核、16G、200G系统磁盘资源用途
	private String vm_5;//4核、32G、200G系统磁盘
	private String vm_5_scene;//4核、32G、200G系统磁盘使用场景描述
	private String vm_5_use;//4核、32G、200G系统磁盘资源用途
	private String vm_6;//4核、8G、200G系统磁盘
	private String vm_6_scene;//4核、8G、200G系统磁盘使用场景描述
	private String vm_6_use;//4核、8G、200G系统磁盘资源用途
	private String vm_7;//2核、4G、200G系统磁盘
	private String vm_7_scene;//2核、4G、200G系统磁盘使用场景描述
	private String vm_7_use;//2核、4G、200G系统磁盘资源用途
	private String physical_1;//虚拟机宿主机/低端应用服务器
	private String physical_1_scene;//虚拟机宿主机/低端应用服务器使用场景描述
	private String physical_1_use;//虚拟机宿主机/低端应用服务器资源用途
	private String physical_2;//分析型服务器（MPP服务器）（台）
	private String physical_2_scene;//分析型服务器（MPP服务器）（台）使用场景描述
	private String physical_2_use;//分析型服务器（MPP服务器）（台）资源用途
	private String physical_3;//分布式服务器（Hadoop服务器）（台）
	private String physical_3_scene;//分布式服务器（Hadoop服务器）（台）使用场景描述
	private String physical_3_use;//分布式服务器（Hadoop服务器）（台）资源用途
	private String physical_4;//缓存型服务器（台）
	private String physical_4_scene;//缓存型服务器（台）使用场景描述
	private String physical_4_use;//缓存型服务器（台）资源用途
	private String physical_5;//高端应用服务器（台）
	private String physical_5_scene;//高端应用服务器（台）使用场景描述
	private String physical_5_use;//高端应用服务器（台）资源用途
	private String basic_1;//分布式文件存储（TB）
	private String basic_1_scene;//分布式文件存储（TB）使用场景描述
	private String basic_1_use;//分布式文件存储（TB）资源用途
	private String basic_2;//分布式块存储（TB）
	private String basic_2_scene;//分布式块存储（TB）使用场景描述
	private String basic_2_use;//分布式块存储（TB）资源用途
	private String basic_3;//对象存储（TB）
	private String basic_3_scene;//对象存储（TB）使用场景描述
	private String basic_3_use;//对象存储（TB）资源用途
	private String basic_4;//FC-SAN存储 (TB)
	private String basic_4_scene;//FC-SAN存储 (TB)使用场景描述
	private String basic_4_use;//FC-SAN存储 (TB)资源用途
	private String basic_5;//IP-SAN存储 (TB)
	private String basic_5_scene;//IP-SAN存储 (TB)使用场景描述
	private String basic_5_use;//IP-SAN存储 (TB)资源用途
	private String basic_6;//备份存储容量需求（TB）
	private String basic_6_scene;//备份存储容量需求（TB）使用场景描述
	private String basic_6_use;//备份存储容量需求（TB）资源用途
	private String database_1;//MySQL
	private String database_1_scene;//MySQL使用场景描述
	private String database_1_use;//MySQL资源用途
	private String database_2;//MongoDB
	private String database_2_scene;//MongoDB使用场景描述
	private String database_2_use;//MongoDB资源用途
	private String database_3;//PostgreSQL
	private String database_3_scene;//PostgreSQL使用场景描述
	private String database_3_use;//PostgreSQL资源用途
	private String database_4;//内存数据库
	private String database_4_scene;//内存数据库使用场景描述
	private String database_4_use;//内存数据库资源用途
	private String database_5;//其它
	private String database_5_scene;//其它使用场景描述
	private String database_5_use;//其它资源用途
	private String fbs_1;//Redis
	private String fbs_1_scene;//Redis使用场景描述
	private String fbs_1_use;//Redis资源用途
	private String fbs_2;//Memcached
	private String fbs_2_scene;//Memcached使用场景描述
	private String fbs_2_use;//Memcached资源用途
	private String message_1;//ActiveMQ
	private String message_1_scene;//ActiveMQ使用场景描述
	private String message_1_use;//ActiveMQ资源用途
	private String message_2;//Kafka
	private String message_2_scene;//Kafka使用场景描述
	private String message_2_use;//Kafka资源用途
	private String message_3;//RocketMQ
	private String message_3_scene;//RocketMQ使用场景描述
	private String message_3_use;//RocketMQ资源用途
	private String app_1;//Apache HTTP Server
	private String app_1_scene;//Apache HTTP Server使用场景描述
	private String app_1_use;//Apache HTTP Server资源用途
	private String app_2;//Tomcat
	private String app_2_scene;//Tomcat使用场景描述
	private String app_2_use;//Tomcat资源用途
	private String app_3;//Jboos EAP
	private String app_3_scene;//Jboos EAP使用场景描述
	private String app_3_use;//Jboos EAP资源用途
	private String haproxy_1;//Nginx
	private String haproxy_1_scene;//Nginx使用场景描述
	private String haproxy_1_use;//Nginx资源用途
	private String haproxy_2;//Haproxy
	private String haproxy_2_scene;//Haproxy使用场景描述
	private String haproxy_2_use;//Haproxy资源用途
	private String fbs_service_1;//Zookeeper
	private String fbs_service_1_scene;//Zookeeper使用场景描述
	private String fbs_service_1_use;//Zookeeper资源用途
	private String fbs_service_2;//ETCD
	private String fbs_service_2_scene;//ETCD使用场景描述
	private String fbs_service_2_use;//ETCD资源用途
	private String search_1;//Elastic search
	private String search_1_scene;//Elastic search使用场景描述
	private String search_1_use;//Elastic search资源用途
	private String registry_1;//Docker Registry
	private String registry_1_scene;//Docker Registry使用场景描述
	private String registry_1_use;//Docker Registry资源用途
	private String flow_1;//JBPM
	private String flow_1_scene;//JBPM使用场景描述
	private String flow_1_use;//JBPM资源用途
	private String flow_2;//activity
	private String flow_2_scene;//activity使用场景描述
	private String flow_2_use;//activity资源用途
	private String lang_1;//openjdk
	private String lang_1_scene;//openjdk使用场景描述
	private String lang_1_use;//openjdk资源用途
	private String lang_2;//Python
	private String lang_2_scene;//Python使用场景描述
	private String lang_2_use;//Python资源用途
	private String lang_3;//Go
	private String lang_3_scene;//Go使用场景描述
	private String lang_3_use;//Go资源用途
	private String lang_4;//Node.js
	private String lang_4_scene;//Node.js使用场景描述
	private String lang_4_use;//Node.js资源用途
	private String lang_5;//Ruby / Ruby on Rails
	private String lang_5_scene;//Ruby / Ruby on Rails使用场景描述
	private String lang_5_use;//Ruby / Ruby on Rails资源用途
	private String lang_6;//.NET Core
	private String lang_6_scene;//.NET Core使用场景描述
	private String lang_6_use;//.NET Core资源用途
	private String cicd_1;//Jenkins
	private String cicd_1_scene;//Jenkins使用场景描述
	private String cicd_1_use;//Jenkins资源用途
	private String develop_1;//Spring Cloud
	private String develop_1_scene;//Spring Cloud使用场景描述
	private String develop_1_use;//Spring Cloud资源用途
	private String develop_2;//其它
	private String develop_2_scene;//其它使用场景描述
	private String develop_2_use;//其它资源用途
	private String network_1;//CMNET地址需求（个）
	private String network_1_scene;//CMNET地址需求（个）使用场景描述
	private String network_1_use;//CMNET地址需求（个）资源用途
	private String network_2;//至CMNET带宽（Gbps）
	private String network_2_scene;//至CMNET带宽（Gbps）使用场景描述
	private String network_2_use;//至CMNET带宽（Gbps）资源用途
	private String network_3;//IP承载网地址需求（个）
	private String network_3_scene;//IP承载网地址需求（个）使用场景描述
	private String network_3_use;//IP承载网地址需求（个）资源用途
	private String network_14;//至IP专网带宽（Gbps）
	private String network_14_scene;//至IP专网带宽（Gbps）使用场景描述
	private String network_14_use;//至IP专网带宽（Gbps）资源用途
}
