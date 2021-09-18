package com.aspire.mirror.ops.api.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api
 * 类名称:    OpsParamValueDetail.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/16 10:08
 * 版本:      v1.0
 */
@Data
public class OpsParamValueDetail {
    @JsonProperty("param_name")
    private String paramName;

//    @Excel(name = "作业实例名称", width = 30)
    @JsonProperty("pipeline_name")
    private String pipelineName;

//    @Excel(name = "步骤实例名称", width = 30)
    @JsonProperty("step_name")
    private String stepName;



    @Excel(name="资源池名称", width = 25)
    private String pool;

    @Excel(name = "设备大类", width = 17)
    @JsonProperty("device_class")
    private String deviceClass;

    @Excel(name = "设备小类", width = 17)
    @JsonProperty("device_type")
    private String deviceType;

    @Excel(name = "业务系统", width = 17)
    @JsonProperty("bizSystem")
    private String bizSystem;

    @Excel(name = "主机名", width = 17)
    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("agent_ip")
    private String agentIp;

    @Excel(name = "设备IP", width = 20)
    private String ip;

    @Excel(name="脚本参数", width = 20)
    @JsonProperty("script_param")
    private String scriptParam;

//    @Excel(name="执行用户", width = 50)
    @JsonProperty("target_ops_user")
    private String targetOpsUser;

    @Excel(name = "参数编码", width = 20)
    @JsonProperty("param_code")
    private String paramCode;


    @Excel(name="密码值", width = 30)
    @JsonProperty("param_value")
    private String paramValue;

    @Excel(name="原密码值", width = 30)
    @JsonProperty("old_param_value")
    private String oldParamValue;

    @Excel(name = "修改人")
    protected String			operator;										// 操作发起人

    @Excel(name = "修改时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;

    /**
     * 是否已生效   0无效/1生效
     */
    @Excel(name = "是否生效", replace = {"无效_0", "生效_1"})
    @JsonProperty("is_valid")
    private String isValid;

    @JsonProperty("step_instance_id")
    private Long stepInstanceId;

    @JsonProperty("pipeline_instance_id")
    private Long pipelineInstanceId;



}
