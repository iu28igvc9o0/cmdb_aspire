package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-12-25 14:35
 * @description 网段-端口组入库实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbVmwareNetworkPortGroup extends BaseCmdbVmwareEntity implements Serializable {
    private static final long serialVersionUID = -1273524390618336023L;

    // 网段
    private String name;
    // 资源池
    private String resourcePool;
    // 数据中心
    private String dataCenterName;
    // 端口组
    private String portGroupName;
}
