package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * mirror自监控设备表
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MirrorHostVO
 * 类描述:    mirror自监控设备表
 * 创建人:    JinSu
 * 创建时间:  2020/11/2 10:21
 * 版本:      v1.0
 */
@Data
public class MirrorHostVO {
    private Long id;

    @JsonProperty("hostid")
    private String hostid;

    @JsonProperty("host")
    private String ip;

    private String name;

    private String pool;

    @JsonProperty("proxy_identity")
    private String proxyIdentity;

    private Integer status;
}
