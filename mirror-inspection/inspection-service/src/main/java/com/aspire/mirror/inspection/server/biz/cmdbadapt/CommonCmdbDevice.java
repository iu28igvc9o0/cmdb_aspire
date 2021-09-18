package com.aspire.mirror.inspection.server.biz.cmdbadapt;

import lombok.Data;

/**
 * CMDB公用对象    <br/>
 * Project Name:inspection-service
 * File Name:CmdbDevice.java
 * Package Name:com.aspire.mirror.inspection.server.biz.cmdbadapt
 * ClassName: CmdbDevice <br/>
 * date: 2018年9月12日 下午2:03:46 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Data
public class CommonCmdbDevice {
    private String deviceId;
    private String name;
    private String deviceIp;
    private String deviceType;
    private String roomId;
}
