package com.migu.tsg.microservice.atomicservice.composite.service.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BizLogInfoDTOJakiro {
	// 集群ID
	@SerializedName("cluster_name")
	private String regionName;

	// 服务名称
	@SerializedName("service_name")
	private String serviceName;
	
	// 容器所在机器的node
	@SerializedName("nodes")
	private String machine;	
	
	// 服务对应的实例的id
	@SerializedName("instance_id")
	private String instanceId;	
	
	@SerializedName("instance_id_full")
	private String instance_id_full;
	
	@SerializedName("paths")
	private String fileName;
	
	// 日志内容
	@SerializedName("message")
	private String logData;
	
	// 日志的收集到的时间戳
	@SerializedName("time")
	private String time;
	// 追踪ID
	@SerializedName("biz_id")
    private String traceId;
    // 日志级别
    @SerializedName("biz_level")
    private String logLevel;
}
