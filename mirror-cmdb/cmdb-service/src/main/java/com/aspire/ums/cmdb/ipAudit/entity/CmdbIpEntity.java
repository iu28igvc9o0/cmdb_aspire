package com.aspire.ums.cmdb.ipAudit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description:
 * @Date: create in 2020/5/16 15:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpEntity {
    private String ip;
    private String idcType;
    private String deviceIp;
    private String ipType;
    private String deviceStatus;
}
