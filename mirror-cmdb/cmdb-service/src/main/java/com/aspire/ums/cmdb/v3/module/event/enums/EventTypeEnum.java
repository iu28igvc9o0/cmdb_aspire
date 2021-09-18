package com.aspire.ums.cmdb.v3.module.event.enums;

import lombok.Getter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ApprovalStatusEnum
 * Author:   hangfang
 * Date:     2020/5/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Getter
public enum EventTypeEnum {
    EVENT_TYPE_DATA_INSERT("data_insert", "模型数据新增"),
    EVENT_TYPE_DATA_UPDATE("data_update", "模型数据修改"),
    EVENT_TYPE_DATA_DELETE("data_delete", "模型数据删除"),
    EVENT_TYPE_CODE_UPDATE("code_update", "配置项数据修改"),
    EVENT_TYPE_CODE_DELETE("code_delete", "配置项数据删除"),
    EVENT_TYPE_CODE_CHANGE("code_change", "配置项数据变更");

    EventTypeEnum(String statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }
    public static String getLabel(String statusCode) {
        for (EventTypeEnum enumItem : EventTypeEnum.values()) {
            if (enumItem.getStatusCode().equals(statusCode)) {
                return enumItem.getLabel();
            }
        }
        return null;
    }

    public String getCode(String label) {
        for (EventTypeEnum enumItem : EventTypeEnum.values()) {
            if (enumItem.getLabel().equals(label)) {
                return enumItem.getStatusCode();
            }
        }
        return null;
    }

    private String	statusCode;
    private String	label;
}
