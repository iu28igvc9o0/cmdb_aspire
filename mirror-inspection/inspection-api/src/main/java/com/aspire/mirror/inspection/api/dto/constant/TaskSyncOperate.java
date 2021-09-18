package com.aspire.mirror.inspection.api.dto.constant;

import lombok.Getter;

@Getter
public enum TaskSyncOperate{
    U("U", "修改"),D("D", "删除"),A("A", "全量同步"), XJ_MARK("xj", "巡检标识");

    TaskSyncOperate (String key, String value) {
        this.key = key;
        this.value = value;
    }
    private final String key;
    private final String value;
}
