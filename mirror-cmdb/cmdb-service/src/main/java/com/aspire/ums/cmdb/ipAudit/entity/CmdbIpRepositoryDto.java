package com.aspire.ums.cmdb.ipAudit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description:
 * @Date: create in 2020/5/22 15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpRepositoryDto {
    private String ip;
    private String idcType;
    private String ipType;
    private String requestTime;
    private String usefulLife;
    private String networkSegmentOwner;
}
