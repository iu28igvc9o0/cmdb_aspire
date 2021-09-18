package com.aspire.mirror.zabbixintegrate.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Http请求返回实体类
 * <p>
 * 项目名称: 微服务运维平台
 * 包:      com.migu.tsg.microservice.monitor.manage.api.dto
 * 类名称:   MetricHttpResponse.java
 * 类描述:   Http请求返回实体类
 * 创建人:   LiangJun
 * 创建时间: 2017-09-26 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorInfoHw {
    private Integer monitor_status;
    private long indicator_id;
    private long obj_type_id;
    private String data_type;
    private String data_unit;
    private String en_us;
    private String zh_cn;
    private String group_en_us;
    private String group_zh_cn;
    private String tag;
    private String indicator_name;
    private String item;
    private String operator;
    private String operator_value;
    private String data_unit_own;
    private Date update_time;
    private String zh_cn_obj_type;
    private int  tag_type;
    private String history_type;
}
