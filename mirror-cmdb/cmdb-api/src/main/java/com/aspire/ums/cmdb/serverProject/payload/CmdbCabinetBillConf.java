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
public class CmdbCabinetBillConf {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

    /**
     * 合同号
     */
    private String contractNo;

    /**
     * 资源池
     */
    private String idcType;

    /**
     * 机房
     */
    private String serverRoomLocation;

    /**
     * 机柜规格
     */
    private String serverCabinetStandard;

    /**
     * 单价(不含税)
     */
    private String contractorPrice;

    /**
     * 计费开始时间
     */
    private String billingStartDate;

    /**
     * 计费截止时间
     */
    private String billEndTime;

    /**
     * 备注
     */
    private String remark;
}
