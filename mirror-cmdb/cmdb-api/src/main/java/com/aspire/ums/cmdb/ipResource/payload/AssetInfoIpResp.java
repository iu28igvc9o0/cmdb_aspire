package com.aspire.ums.cmdb.ipResource.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 资产管理流程-原IP、回收IP 弹窗实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/19 16:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AssetInfoIpResp extends AssetInfoBaseResp implements Serializable {
    private static final long serialVersionUID = 1654085854205329831L;
    // IP类型
    private String ipType;
    // IP
    private String ip;
}