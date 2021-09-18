package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanwenhui
 * @date 2020-08-26 15:27
 * @description 冲突IP二次校验请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpClashRebuildRequest implements Serializable {
    private static final long serialVersionUID = 1345642505013790965L;
    // 冲突IP
    private String ip;
    // 网关设备IP
    private String gateway;
    // 资源池名称
    private String resource;
    // mac地址
    private String mac;
    // 检测时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
