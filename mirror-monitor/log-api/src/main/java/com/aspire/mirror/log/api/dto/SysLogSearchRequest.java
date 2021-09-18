package com.aspire.mirror.log.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.logs.payload
 * 类名称:    SysLogSearchRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 10:46
 * 版本:      v1.0
 */
@Data
@ApiModel(value = "业务日志查询请求")
public class SysLogSearchRequest {
    /**
     * ip
     */
    @ApiModelProperty(value = "设备IP")
    private String ip;

    @ApiModelProperty(value = "资源池")
    private String pool;
    /**
     * 日志创建开始时间
     */
    @ApiModelProperty(value = "日志创建开始时间")
    @JsonProperty(value = "create_time_start")
    private String createTimeStart;
    /**
     * 日志创建结束时间
     */
    @ApiModelProperty(value = "日志创建结束时间")
    @JsonProperty(value = "create_time_end")
    private String createTimeEnd;
    /**
     * 包含
     */
    private String include;
    /**
     * 包含关键字
     */
    private String includeKey;
    /**
     * 不包含
     */
    private String noInclude;
    /**
     * 不包含关键字
     */
    private String noIncludeKey;
    /**
     * 查询关键字
     */
    @ApiModelProperty(value = "查询关键字集合")
    private List<String> params;
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    @JsonProperty(value = "page_no")
    private String pageNo;
    /**
     * 每页数据条数
     */
    @ApiModelProperty(value = "每页数据条数")
    @JsonProperty(value = "page_size")
    private String pageSize;

    @ApiModelProperty(value = "是否是导出")
    @JsonProperty(value = "is_export")
    private String isExport = "0";
}
