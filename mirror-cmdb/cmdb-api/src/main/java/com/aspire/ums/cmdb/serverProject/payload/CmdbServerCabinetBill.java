package com.aspire.ums.cmdb.serverProject.payload;

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
public class CmdbServerCabinetBill {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;
}
