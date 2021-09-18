package com.aspire.ums.cmdb.resource.entity;

import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@ToString
public class ResourceDemandCollect implements Serializable {

	private static final long serialVersionUID = -7368241802385018177L;

	private String id;
    private String resource_pool;//资源池
    private String primary_department ; //一级部门
    private String secondary_department; // 二级部门
    private String app_system;//应用系统
    private Integer estimate_id;
	private String is_project; //是否立项
	private String project_time;//立项时间
	private String resource_put_date;//资源提出时间
	private String resource_produce_date;//资源预期投资时间
    private String 	system_description;//系统描述
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
	private String special_requirement;//与前期上报需求有无变化，如有，请说明原因
	private String address_requirement;// 物理局址要求
    private String department_head;//部门负责人
	private String inter_contact;//接口联系人
	private String phone;//电话
	private String email;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResource_pool() {
		return resource_pool;
	}
	public void setResource_pool(String resource_pool) {
		this.resource_pool = resource_pool;
	}
	public String getPrimary_department() {
		return primary_department;
	}
	public void setPrimary_department(String primary_department) {
		this.primary_department = primary_department;
	}
	public String getSecondary_department() {
		return secondary_department;
	}
	public void setSecondary_department(String secondary_department) {
		this.secondary_department = secondary_department;
	}
	public String getApp_system() {
		return app_system;
	}
	public void setApp_system(String app_system) {
		this.app_system = app_system;
	}
	public Integer getEstimate_id() {
		return estimate_id;
	}
	public void setEstimate_id(Integer estimate_id) {
		this.estimate_id = estimate_id;
	}
	public String getIs_project() {
		return is_project;
	}
	public void setIs_project(String is_project) {
		this.is_project = is_project;
	}
	
	
	public String getProject_time() {
		return project_time;
	}
	public void setProject_time(String project_time) {
		this.project_time = project_time;
	}
	public String getResource_put_date() {
		return resource_put_date;
	}
	public void setResource_put_date(String resource_put_date) {
		this.resource_put_date = resource_put_date;
	}
	public String getResource_produce_date() {
		return resource_produce_date;
	}
	public void setResource_produce_date(String resource_produce_date) {
		this.resource_produce_date = resource_produce_date;
	}
	public String getSystem_description() {
		return system_description;
	}
	public void setSystem_description(String system_description) {
		this.system_description = system_description;
	}
	public Integer getVm_model1() {
		return vm_model1;
	}
	public void setVm_model1(Integer vm_model1) {
		this.vm_model1 = vm_model1;
	}
	public Integer getVm_model2() {
		return vm_model2;
	}
	public void setVm_model2(Integer vm_model2) {
		this.vm_model2 = vm_model2;
	}
	public Integer getVm_model3() {
		return vm_model3;
	}
	public void setVm_model3(Integer vm_model3) {
		this.vm_model3 = vm_model3;
	}
	public Integer getVm_model4() {
		return vm_model4;
	}
	public void setVm_model4(Integer vm_model4) {
		this.vm_model4 = vm_model4;
	}
	public Integer getVm_model5() {
		return vm_model5;
	}
	public void setVm_model5(Integer vm_model5) {
		this.vm_model5 = vm_model5;
	}
	public Integer getVm_model6() {
		return vm_model6;
	}
	public void setVm_model6(Integer vm_model6) {
		this.vm_model6 = vm_model6;
	}
	public Integer getVm_model7() {
		return vm_model7;
	}
	public void setVm_model7(Integer vm_model7) {
		this.vm_model7 = vm_model7;
	}
	public Integer getVm_model8() {
		return vm_model8;
	}
	public void setVm_model8(Integer vm_model8) {
		this.vm_model8 = vm_model8;
	}
	public Integer getPm_low_app_server() {
		return pm_low_app_server;
	}
	public void setPm_low_app_server(Integer pm_low_app_server) {
		this.pm_low_app_server = pm_low_app_server;
	}
	public Integer getPm_analytical_server() {
		return pm_analytical_server;
	}
	public void setPm_analytical_server(Integer pm_analytical_server) {
		this.pm_analytical_server = pm_analytical_server;
	}
	public Integer getPm_distributed_server() {
		return pm_distributed_server;
	}
	public void setPm_distributed_server(Integer pm_distributed_server) {
		this.pm_distributed_server = pm_distributed_server;
	}
	public Integer getPm_cache_server() {
		return pm_cache_server;
	}
	public void setPm_cache_server(Integer pm_cache_server) {
		this.pm_cache_server = pm_cache_server;
	}
	public Integer getPm_high_app_server() {
		return pm_high_app_server;
	}
	public void setPm_high_app_server(Integer pm_high_app_server) {
		this.pm_high_app_server = pm_high_app_server;
	}
	
	public Double getStorage_distributd_file_capacity() {
		return storage_distributd_file_capacity;
	}
	public void setStorage_distributd_file_capacity(Double storage_distributd_file_capacity) {
		this.storage_distributd_file_capacity = storage_distributd_file_capacity;
	}
	public Double getStorage_distributd_block_performance() {
		return storage_distributd_block_performance;
	}
	public void setStorage_distributd_block_performance(Double storage_distributd_block_performance) {
		this.storage_distributd_block_performance = storage_distributd_block_performance;
	}
	public Double getStorage_object_capacity() {
		return storage_object_capacity;
	}
	public void setStorage_object_capacity(Double storage_object_capacity) {
		this.storage_object_capacity = storage_object_capacity;
	}
	public Double getStorage_fc_san_capacity() {
		return storage_fc_san_capacity;
	}
	public void setStorage_fc_san_capacity(Double storage_fc_san_capacity) {
		this.storage_fc_san_capacity = storage_fc_san_capacity;
	}
	public Double getStorage_ip_san_capacity() {
		return storage_ip_san_capacity;
	}
	public void setStorage_ip_san_capacity(Double storage_ip_san_capacity) {
		this.storage_ip_san_capacity = storage_ip_san_capacity;
	}
	public Double getStorage_fc_san_naked() {
		return storage_fc_san_naked;
	}
	public void setStorage_fc_san_naked(Double storage_fc_san_naked) {
		this.storage_fc_san_naked = storage_fc_san_naked;
	}
	public Double getStorage_ip_san_naked() {
		return storage_ip_san_naked;
	}
	public void setStorage_ip_san_naked(Double storage_ip_san_naked) {
		this.storage_ip_san_naked = storage_ip_san_naked;
	}
	public Double getStorage_back_up() {
		return storage_back_up;
	}
	public void setStorage_back_up(Double storage_back_up) {
		this.storage_back_up = storage_back_up;
	}
	public Integer getCmnet_address() {
		return cmnet_address;
	}
	public void setCmnet_address(Integer cmnet_address) {
		this.cmnet_address = cmnet_address;
	}
	public Double getBandwidth_gbps_cmnet() {
		return bandwidth_gbps_cmnet;
	}
	public void setBandwidth_gbps_cmnet(Double bandwidth_gbps_cmnet) {
		this.bandwidth_gbps_cmnet = bandwidth_gbps_cmnet;
	}
	public Integer getIp_address() {
		return ip_address;
	}
	public void setIp_address(Integer ip_address) {
		this.ip_address = ip_address;
	}
	public Double getBandwidth_gbps_ip() {
		return bandwidth_gbps_ip;
	}
	public void setBandwidth_gbps_ip(Double bandwidth_gbps_ip) {
		this.bandwidth_gbps_ip = bandwidth_gbps_ip;
	}
	public Integer getDb_mysql() {
		return db_mysql;
	}
	public void setDb_mysql(Integer db_mysql) {
		this.db_mysql = db_mysql;
	}
	public Integer getDb_mongodb() {
		return db_mongodb;
	}
	public void setDb_mongodb(Integer db_mongodb) {
		this.db_mongodb = db_mongodb;
	}
	public Integer getDb_postgresql() {
		return db_postgresql;
	}
	public void setDb_postgresql(Integer db_postgresql) {
		this.db_postgresql = db_postgresql;
	}
	public Integer getDb_memory() {
		return db_memory;
	}
	public void setDb_memory(Integer db_memory) {
		this.db_memory = db_memory;
	}
	public Integer getDb_other() {
		return db_other;
	}
	public void setDb_other(Integer db_other) {
		this.db_other = db_other;
	}
	public Integer getCache_redis() {
		return cache_redis;
	}
	public void setCache_redis(Integer cache_redis) {
		this.cache_redis = cache_redis;
	}
	public Integer getCache_memcached() {
		return cache_memcached;
	}
	public void setCache_memcached(Integer cache_memcached) {
		this.cache_memcached = cache_memcached;
	}
	public Integer getInfo_middleware_activemq() {
		return info_middleware_activemq;
	}
	public void setInfo_middleware_activemq(Integer info_middleware_activemq) {
		this.info_middleware_activemq = info_middleware_activemq;
	}
	public Integer getInfo_middleware_kafka() {
		return info_middleware_kafka;
	}
	public void setInfo_middleware_kafka(Integer info_middleware_kafka) {
		this.info_middleware_kafka = info_middleware_kafka;
	}
	public Integer getInfo_middleware_rocketmq() {
		return info_middleware_rocketmq;
	}
	public void setInfo_middleware_rocketmq(Integer info_middleware_rocketmq) {
		this.info_middleware_rocketmq = info_middleware_rocketmq;
	}
	public Integer getApp_middleware_apache() {
		return app_middleware_apache;
	}
	public void setApp_middleware_apache(Integer app_middleware_apache) {
		this.app_middleware_apache = app_middleware_apache;
	}
	public Integer getApp_middleware_jboos() {
		return app_middleware_jboos;
	}
	public void setApp_middleware_jboos(Integer app_middleware_jboos) {
		this.app_middleware_jboos = app_middleware_jboos;
	}
	public Integer getApp_middleware_tomcat() {
		return app_middleware_tomcat;
	}
	public void setApp_middleware_tomcat(Integer app_middleware_tomcat) {
		this.app_middleware_tomcat = app_middleware_tomcat;
	}
	public Integer getBalancer_nginx() {
		return balancer_nginx;
	}
	public void setBalancer_nginx(Integer balancer_nginx) {
		this.balancer_nginx = balancer_nginx;
	}
	public Integer getBalancer_haproxy() {
		return balancer_haproxy;
	}
	public void setBalancer_haproxy(Integer balancer_haproxy) {
		this.balancer_haproxy = balancer_haproxy;
	}
	public Integer getServer_zookeeper() {
		return server_zookeeper;
	}
	public void setServer_zookeeper(Integer server_zookeeper) {
		this.server_zookeeper = server_zookeeper;
	}
	public Integer getServer_etcd() {
		return server_etcd;
	}
	public void setServer_etcd(Integer server_etcd) {
		this.server_etcd = server_etcd;
	}
	public Integer getSearch_middleware_es() {
		return search_middleware_es;
	}
	public void setSearch_middleware_es(Integer search_middleware_es) {
		this.search_middleware_es = search_middleware_es;
	}
	public Integer getServer_docker_registry() {
		return server_docker_registry;
	}
	public void setServer_docker_registry(Integer server_docker_registry) {
		this.server_docker_registry = server_docker_registry;
	}
	public Integer getStream_jbpm() {
		return stream_jbpm;
	}
	public void setStream_jbpm(Integer stream_jbpm) {
		this.stream_jbpm = stream_jbpm;
	}
	public Integer getStream_activity() {
		return stream_activity;
	}
	public void setStream_activity(Integer stream_activity) {
		this.stream_activity = stream_activity;
	}
	public Integer getImage_openjdk() {
		return image_openjdk;
	}
	public void setImage_openjdk(Integer image_openjdk) {
		this.image_openjdk = image_openjdk;
	}
	public Integer getImage_python() {
		return image_python;
	}
	public void setImage_python(Integer image_python) {
		this.image_python = image_python;
	}
	public Integer getImage_go() {
		return image_go;
	}
	public void setImage_go(Integer image_go) {
		this.image_go = image_go;
	}
	public Integer getImage_nodejs() {
		return image_nodejs;
	}
	public void setImage_nodejs(Integer image_nodejs) {
		this.image_nodejs = image_nodejs;
	}
	public Integer getImage_ruby() {
		return image_ruby;
	}
	public void setImage_ruby(Integer image_ruby) {
		this.image_ruby = image_ruby;
	}
	public Integer getImage_net() {
		return image_net;
	}
	public void setImage_net(Integer image_net) {
		this.image_net = image_net;
	}
	public Integer getCicd_jenkins() {
		return cicd_jenkins;
	}
	public void setCicd_jenkins(Integer cicd_jenkins) {
		this.cicd_jenkins = cicd_jenkins;
	}
	public Integer getFramework_springcloud() {
		return framework_springcloud;
	}
	public void setFramework_springcloud(Integer framework_springcloud) {
		this.framework_springcloud = framework_springcloud;
	}
	public Integer getFramework_other() {
		return framework_other;
	}
	public void setFramework_other(Integer framework_other) {
		this.framework_other = framework_other;
	}
	public String getSpecial_requirement() {
		return special_requirement;
	}
	public void setSpecial_requirement(String special_requirement) {
		this.special_requirement = special_requirement;
	}
	public String getAddress_requirement() {
		return address_requirement;
	}
	public void setAddress_requirement(String address_requirement) {
		this.address_requirement = address_requirement;
	}
	public String getDepartment_head() {
		return department_head;
	}
	public void setDepartment_head(String department_head) {
		this.department_head = department_head;
	}
	public String getInter_contact() {
		return inter_contact;
	}
	public void setInter_contact(String inter_contact) {
		this.inter_contact = inter_contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
    
    
   
}
