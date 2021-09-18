package com.aspire.mirror.composite.payload.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ComAlertFilter {
    @JsonProperty("id")
    private Integer id;

    /**告警过滤名称*/
    @JsonProperty("name")
    private String name;

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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private Date updated_at;

    /** 监控对象 */
    @JsonProperty("note")
    private String note;
    
    @JsonProperty("scene_num")
    private Integer sceneNum;

}
