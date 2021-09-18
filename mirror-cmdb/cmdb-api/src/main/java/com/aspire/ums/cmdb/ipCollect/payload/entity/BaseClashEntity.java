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
 * 冲突IP基类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/30 11:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseClashEntity implements Serializable {
    private static final long serialVersionUID = 8324099169242319718L;
    // 检测时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;
    // 上次绑定的MAC地址
    private String oldMac;
    // 当前绑定的MAC地址
    private String nowMac;
    // 网关设备IP
    private String gateway;
    // 所属资源池
    private String resource;
}
