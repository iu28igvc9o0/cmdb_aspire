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
public class CmdbCabinetRecord {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 资源池
     */
    private String idcType;

    /**
     * 机房
     */
    private String serverRoomLocation;

    /**
     * 机柜编码
     */
    private String serverCabinetCode;

    /**
     * 机柜规格
     */
    private String serverCabinetStandard;

    /**
     * 设备台数
     */
    private String serverDeviceCount;

    /**
     * 首次加电日期
     */
    private String firstOnlineDate;

    /**
     * 计费起始日期
     */
    private String billStartDate;

    /**
     * 下架时间
     */
    private String offlineDate;

    /**
     * 所属项目
     */
    private String projectBelongTo;

    /**
     * 所属业务
     */
    private String businessBelongTo;

    /**
     * 负责人
     */
    private String ownerPerson;

    /**
     * 工单号
     */
    private String orderNo;
}
