package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class CompConfigDataResponse {
    /**
     * 数据ID
     */
    @ApiModelProperty(value = "数据ID")
    String id;

    /**
     * 索引
     */
    @ApiModelProperty(value = "索引")
    String index;

    /**
     * 资源池
     */

    String pool;

    /**
     * 资源池名称
     */
    @ApiModelProperty(value = "资源池")
    @Excel(name="资源池", width = 20)
    @JsonProperty(value = "pool_name")
    String poolName;

    @Excel(name = "设备IP", width = 20)
    String ip;
    /**
     * 采集时间
     */
    @Excel(name = "采集时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    @JsonProperty(value = "log_create_time")
    Date logCreateTime;

    /**
     * 配置内容
     */
    @Excel(name = "采集内容", width = 100, height = 20)
    String massage;


    /**
     * 文件创建时间（修改时间）
     */
    @JsonProperty(value = "create_time")
    Date createTime;
}
