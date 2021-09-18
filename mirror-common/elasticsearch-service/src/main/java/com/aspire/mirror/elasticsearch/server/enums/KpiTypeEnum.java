package com.aspire.mirror.elasticsearch.server.enums;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sf.json.JSONObject;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.enums
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 16:32
 * @Description: ${Description}
 */
@Getter
@AllArgsConstructor
public enum KpiTypeEnum {
    CPU_PUSED("cpu利用率"),
    MEMORY_PUSED("内存利用率"),
    DISK_PUSED("磁盘利用率"),
    DISK_PUSED_ROOT("磁盘根目录利用率"),
    SAN_STORAGE_TOTAL("san存储总容量"),
    SAN_STORAGE_PUSED("san存储容量使用率"),
    SAN_STORAGE_USED ("san存储容量使用量");

    private String value;

    public static JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        for (KpiTypeEnum e : KpiTypeEnum.values()) {
            JSONObject object = new JSONObject();
            object.put("type", e.name());
            object.put("desc", e.getValue());
            jsonArray.add(object);
        }
        return jsonArray;
    }
}
