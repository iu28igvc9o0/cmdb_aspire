package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 配置数据查询
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.api.dto
 * 类名称:    CompConfigDataSearchRequest.java
 * 类描述:    配置数据查询
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 13:48
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompConfigDataSearchRequest {
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

    /**
     * 页码
     */
    @JsonProperty(value = "page_no")
    private String pageNo;

    /**
     * 每页数据条数
     */
    @JsonProperty(value = "page_size")
    private String pageSize;

}
