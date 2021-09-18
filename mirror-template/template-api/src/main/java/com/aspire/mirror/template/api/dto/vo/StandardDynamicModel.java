package com.aspire.mirror.template.api.dto.vo;

import com.aspire.mirror.template.api.dto.TemplateDateSyncVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 动态阈值模型
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    StandardDynamicModel
 * 类描述:    动态阈值模型
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 16:33
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(of = {"triggerId", "deviceId", "modelJson", "modelStatus", "deviceItemId"}, callSuper = false)
public class StandardDynamicModel extends TemplateDateSyncVo {
    @JsonProperty("model_id")
    private String modelId;

    @JsonProperty("trigger_id")
    private String triggerId;

    @JsonProperty("device_item_id")
    private String deviceItemId;

    @JsonProperty("zabbix_item_id")
    private String zabbixItemId;

    @JsonProperty("device_id")
    private String deviceId;

    private String ip;

    @JsonProperty("idc_type")
    private String idcType;

    @JsonProperty("create_time")
    private Date createTime;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("model_json")
    private String modelJson;

    @JsonProperty("thrid_system_id")
    private String thridSystemId;

    /**
     * 模型状态 ON:开启 OFF:关闭
     */
    @JsonProperty("model_status")
    private String modelStatus;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("key")
    private String key;

//    @JsonProperty("model_ext")
//    private StandardDynamicModelExt modelExt;
}
