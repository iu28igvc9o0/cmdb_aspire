package com.aspire.ums.cmdb.ipResource.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 资产管理流程-资源回收、IP变更 回填实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 18:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AssetInfoBackfillResp extends AssetInfoResp implements Serializable {
    private static final long serialVersionUID = 9115250147669173264L;
    // 项目归属
    private String projectBelongto;
    // 型号
    private String deviceModel;
    // 配置
    private String deviceConfig;
    // 操作系统
    private String deviceOsType;
    // 机柜
    private String deviceCell;
    // 序列号
    private String devicesn;
    // 资产编号
    private String assetNum;
    // consoleip
    private String consoleip;
    // 回收IP
    private String recycleIps;
}
