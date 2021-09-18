package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 17:43
 */
@Data
@ToString
public class CmdbIpCollectResponse implements Serializable {

    private static final long serialVersionUID = -5177810205505241493L;
    // 主键ID
    private String id;

    // 实例ID
    private String instanceId;

    // IP地址
    private String ip;

    // IP类型
    private String iptype;

    // MAC地址
    private String mac;

    // 所属资源池
    private String resource;

    // 网关设备IP
    private String gateway;

    // 数据来源
    private String source;

    // 检查时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    // ip和资源池字符组合,用于区分是否唯一 create by fanwenhui 20200810
    private String ip2Idc;

    // 小时单位的世界戳 create by fanwenhui 20210303
    private String timeKey;
}
