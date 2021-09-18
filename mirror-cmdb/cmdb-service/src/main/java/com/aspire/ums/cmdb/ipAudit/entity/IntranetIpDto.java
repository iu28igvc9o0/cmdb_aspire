package com.aspire.ums.cmdb.ipAudit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 内网IP Dto
 * @Date: create in 2020/5/22 20:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntranetIpDto {
    private String ip;
    private String dc;
}
