package com.aspire.mirror.log.server.dao.po;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author baiwenping
 * @Title: ConfigCompareLogs
 * @Package com.aspire.mirror.alert.server.dao.po
 * @Description: TODO
 * @date 2020/12/10 17:37
 */
@Data
public class ConfigCompareLogs {
    private Integer id;
    private Integer compareId;
    private String idcType;
    private String masterIp;
    private String backupIp;
    private String masterConfigFile;
    private String backupConfigFile;
    private String addResult;
    private String modifyResult;
    private String delResult;
    private Date compareTime;
    private String compareUser;
}
