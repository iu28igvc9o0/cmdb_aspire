package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统日志返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.logs.payload
 * 类名称:    CompSysLogResponse.java
 * 类描述:    系统日志返回
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 11:07
 * 版本:      v1.0
 */
@Data
public class CompSysLogResponse {
    /**
     * 资源池
     */
    String pool;

    /**
     * 资源池名称
     */
    @Excel(name = "资源池", width = 20)
    @JsonProperty(value = "pool_name")
    String poolName;

    @Excel(name = "设备IP", width = 20)
    String ip;

    /**
     * 日志创建时间
     */
    @Excel(name = "日志生成时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    @JsonProperty(value = "log_create_time")
    Date logCreateTime;

    /**
     * 日志内容
     */
    @Excel(name = "日志内容", width = 200)
    String massage;

    @Excel(name = "代理IP", width = 200)
    String proxyip;

    @Excel(name = "代理端口", width = 200)
    String proxyport;
}
