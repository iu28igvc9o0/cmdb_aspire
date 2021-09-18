package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenStatusInfoStatistRequest {
    private Integer pageNo;
    private Integer pageSize;
    private String resourcePool;
    private String maintenProjectType;
    private String deviceType;
    private String serviceProduce;
    private String timeStatus; // zj | yj | cb | jjcb
}
