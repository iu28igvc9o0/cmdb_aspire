package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
*   维护人员信息(资源池 + 设备分类)
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenancePersonInfo {
    private Integer id;
    private String resourcePool;
    private String deviceClass;
    private String name;
    private String phone;
    private String email;
}
