package com.aspire.ums.cmdb.ipResource.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/19 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetInfoBaseResp implements Serializable {
    private static final long serialVersionUID = 6204183311261795210L;
    // 资源列表Id
    private String assetId;
    // 管理IP
    private String manageIp;
    // 所属资源池
    private String idcVal;
    // 所属资源池Id
    private String idcId;
    // 主备
    private String hostBack;
    // 业务ip1
    private String businessIp1;
    // 业务ip2
    private String businessIp2;
}
