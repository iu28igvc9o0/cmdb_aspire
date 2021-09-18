package com.aspire.mirror.alert.server.clientservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设备详情返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    InnerCmdbDeviceDetail.java
 * 类描述:    设备详情返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/15 18:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class BizSystemDetail implements Serializable {
	private static final long serialVersionUID = 381193553816039630L;
	private String bizSysId;
	private String bizSysCode;
    private String bizSystemName;
}
