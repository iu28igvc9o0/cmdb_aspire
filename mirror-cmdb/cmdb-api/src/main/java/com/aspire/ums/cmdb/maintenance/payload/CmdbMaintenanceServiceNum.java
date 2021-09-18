package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceServiceNum {
    private String id;
    /*
     * 维保项目ID
     * */
    private String projectId;
    /*
     * 服务形式
     * */
    private String serviceType;
    /*
    * 服务数量
    * */
    private String serviceNum;
}
