package com.aspire.mirror.log.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 系统日志返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.logs.payload
 * 类名称:    SysLogResponse.java
 * 类描述:    系统日志返回
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 11:07
 * 版本:      v1.0
 */
@Data
@ToString
public class ConfigDataResponse {
    /**
     * 数据ID
     */
    @ApiModelProperty(value = "数据ID")
    String id;
    /**
     * 数据索引
     */
    @ApiModelProperty(value = "数据索引")
    String index;
    /**
     * 资源池
     */
    @ApiModelProperty(value = "资源池编码")
    String pool;

    /**
     * 资源池名称
     */
    @ApiModelProperty(value = "资源池名称")
    @JsonProperty(value = "pool_name")
    String poolName;

    @ApiModelProperty(value = "设备IP")
    String ip;
    /**
     * 配置内容
     */
    @ApiModelProperty(value = "配置内容")
    String massage;
    /**
     * 采集时间
     */
    @ApiModelProperty(value = "采集时间")
    @JsonProperty(value = "log_create_time")
    Date logCreateTime;

    /**
     * 文件创建时间（修改时间）
     */
    @ApiModelProperty(value = "文件创建时间")
    @JsonProperty(value = "create_time")
    Date createTime;
}
