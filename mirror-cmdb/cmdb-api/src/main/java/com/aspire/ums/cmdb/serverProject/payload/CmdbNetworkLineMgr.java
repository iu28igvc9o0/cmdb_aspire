package com.aspire.ums.cmdb.serverProject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbNetworkLineMgr {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

    /**
     * 线路名称
     */
    private String serverLine;

    /**
     * 资源池
     */
    private String idcType;

    /**
     * 网络类型
     */
    private String serverNetworkType;

    /**
     * 计费方式
     */
    private String serverBillType;

    /**
     * 单位
     */
    private String serverBillUnit;

    /**
     * 规格
     */
    private String serverLineStandard;

    /**
     * A端
     */
    private String serverLineA;

    /**
     * B端
     */
    private String serverLineB;

    /**
     * 线路新增时间
     */
    private String serverLineCreateDate;

    /**
     * 线路调整时间
     */
    private String serverLineUpdateDate;

    /**
     * 线路取消时间
     */
    private String serverLineCancalDate;

    /**
     * 所属项目
     */
    private String projectBelongTo;

    /**
     * 所属业务
     */
    private String businessBelongTo;
}
