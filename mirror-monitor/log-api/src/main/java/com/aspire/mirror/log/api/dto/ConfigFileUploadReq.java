package com.aspire.mirror.log.api.dto;

import lombok.Data;

@Data
public class ConfigFileUploadReq {

    // uuid
    private String uuid;
    // 资源池
    private String idcType;
    // 设备ip
    private String deviceIp;
    // 上传时间
    private String uploadTime;
    // 上传内容
    private String uploadContent;
    // 上传信息
    private String uploadInfo;
    // 上传人
    private String userName;

}
