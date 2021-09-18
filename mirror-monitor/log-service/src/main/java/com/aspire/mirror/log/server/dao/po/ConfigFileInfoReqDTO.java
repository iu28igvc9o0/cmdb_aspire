package com.aspire.mirror.log.server.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class ConfigFileInfoReqDTO {

    private String ip;
    private String proxyip;
    private String cmd;
    private String poolcode;
    private String poolname;
    private Date log_create_time;
    private String message;
}
