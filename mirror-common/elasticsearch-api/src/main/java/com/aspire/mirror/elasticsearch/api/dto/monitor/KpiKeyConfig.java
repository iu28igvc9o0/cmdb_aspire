package com.aspire.mirror.elasticsearch.api.dto.monitor;

import lombok.Data;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.api.dto.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 15:13
 * @Description: ${Description}
 */
@Data
public class KpiKeyConfig {
    private String kpiType;
    private String kpiKey;
    private String kpiKeyModel;
    private String kpiTypeDesc;
    private String kpiDesc;
}
