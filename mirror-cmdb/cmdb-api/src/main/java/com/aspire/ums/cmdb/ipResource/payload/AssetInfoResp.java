package com.aspire.ums.cmdb.ipResource.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 资产管理流程-管理IP、关联资产 弹窗列表实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 18:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AssetInfoResp extends AssetInfoBaseResp implements Serializable {
    private static final long serialVersionUID = 4854027599446638440L;
    // 是否资源池管理
    private String isPool;
    // 设备分类
    private String deviceClassName;
    // 设备类型
    private String deviceTypeName;
    // 设备类型Id
    private String deviceTypeId;
    // 独立业务线
    private String aloneBusiness;
    // 独立业务线子模块
    private String aloneBusiness2;
    // 机房位置
    private String machineRoomName;
}
