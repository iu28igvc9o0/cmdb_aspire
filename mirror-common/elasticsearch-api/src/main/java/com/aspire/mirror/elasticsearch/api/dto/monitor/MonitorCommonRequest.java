package com.aspire.mirror.elasticsearch.api.dto.monitor;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.api.dto.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-04-26 17:47
 * @Description: ${Description}
 */
@Data
public class MonitorCommonRequest {
    private String index;
    private JSONObject dsl;
}
