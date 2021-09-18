package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.logs.payload
 * 类名称:    ConfigDataExportRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 21:36
 * 版本:      v1.0
 */
@Data
public class ConfigDataExportRequest {
    /**
     * ip
     */
    private String ip;
    /**
     * 日志创建开始时间
     */
    @JsonProperty(value = "create_time_start")
    private String createTimeStart;
    /**
     * 日志创建结束时间
     */
    @JsonProperty(value = "create_time_end")
    private String createTimeEnd;

    /**
     * 查询关键字
     */
    @ApiModelProperty(value = "查询关键字集合")
    private List<String> params;

    @JsonProperty(value = "pool")
    private String pool;
}
