package com.aspire.mirror.alert.server.dao.filter.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertFilterScene {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("filter_id")
    private Integer filterId;
    
    @JsonProperty("filter_name")
    private String filterName;

    /**告警过滤名称*/
    @JsonProperty("name")
    private String name;
    
    /**维护用户*/
    @JsonProperty("operate_user")
    private String operateUser;
    
    /**告警过滤名称*/
    @JsonProperty("option_condition")
    private String optionCondition;

    /** 创建人*/
    @JsonProperty("creater")
    private String creater;
    
    /**
                   修改人
     */
    @JsonProperty("editer")
    private String editer;

    /** 业务系统 */
    @JsonProperty("created_at")
    private Date created_at;

    /** 监控指标/内容，关联触发器name */
    @JsonProperty("updated_at")
    private Date updated_at;

    /** 监控对象 */
    @JsonProperty("note")
    private String note;

}
