package com.aspire.ums.cmdb.serverProject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbNetworkLineRecord {

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
     * 负责人
     */
    private String ownerPerson;

    /**
     * 工单号
     */
    private String orderNo;
}
