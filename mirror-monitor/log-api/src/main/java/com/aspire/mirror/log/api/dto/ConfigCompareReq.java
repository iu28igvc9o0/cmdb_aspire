package com.aspire.mirror.log.api.dto;

import lombok.Data;

/**
 *
 * @author baiwenping
 * @Title: ConfigCompare
 * @Package com.aspire.mirror.alert.server.dao.po
 * @Description: TODO
 * @date 2020/12/10 17:36
 */
@Data
public class ConfigCompareReq {
    private String idcType;
    private String masterIp;
    private String backupIp;
    private String brand;
    private String createUser;
}
