package com.aspire.mirror.log.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SysLogResponse {
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
     * 日志内容
     */
    @ApiModelProperty(value = "日志内容")
    String massage;
    /**
     * 日志创建时间
     */
    @ApiModelProperty(value = "日志创建时间")
    @JsonProperty(value = "log_create_time")
    Date logCreateTime;

    @ApiModelProperty(value="代理IP")
    String proxyip;

    @ApiModelProperty(value="代理端口")
    String proxyport;
}
