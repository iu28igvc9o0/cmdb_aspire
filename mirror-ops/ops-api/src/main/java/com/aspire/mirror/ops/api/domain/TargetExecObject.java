package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 目标执行对象
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    TargetExecObject
 * 类描述:    目标执行对象
 * 创建人:    JinSu
 * 创建时间:  2021/1/22 14:25
 * 版本:      v1.0
 */
@Data
public class TargetExecObject {
    @JsonProperty("pool_name")
    private String poolName;

    @JsonProperty("department1")
    private String department1;

    @JsonProperty("department2")
    private String department2;

    @JsonProperty("biz_system")
    private String bizSystem;
    // 操作系统类型
    @JsonProperty("os_type")
    private String osType;
    // 交维状态
    @JsonProperty("os_status")
    private String osStatus;
}
