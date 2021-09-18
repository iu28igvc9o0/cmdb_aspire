package com.aspire.ums.cmdb.serverProject.payload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCabinet implements Serializable {

    private static final long serialVersionUID = -8805358505917092487L;

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

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
     * 加电设备数
     */
    private String onlineCount;

    /**
     * 机柜规格
     */
    private String serverCabinetStandard;

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
     * 所属业务
     */
    private String businessBelongTo;

    /**
     * 所属项目
     */
    private String projectBelongTo;

    /**
     * 是否已加电
     */
    private String serverOnlineStatus;
}
