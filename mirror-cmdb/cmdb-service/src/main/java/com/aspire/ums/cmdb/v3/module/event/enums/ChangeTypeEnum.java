package com.aspire.ums.cmdb.v3.module.event.enums;

import com.aspire.ums.cmdb.v3.module.event.EventConst;
import lombok.Getter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ChangeTypeEnum
 * Author:   hangfang
 * Date:     2020/9/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Getter
public enum ChangeTypeEnum {
    /** CI变更类型（1-新增） */
    CHANGETYPE_ADD(EventConst.EVENT_TYPE_DATA_INSERT, 1),
    /** CI变更类型（2-更新） */
    CHANGETYPE_UPDATE(EventConst.EVENT_TYPE_DATA_UPDATE, 2),
    /** CI变更类型（3-删除） */
    CHANGETYPE_DELETE(EventConst.EVENT_TYPE_DATA_DELETE, 3);

    ChangeTypeEnum(String type, Integer code) {
        this.type = type;
        this.code = code;
    }
    public static Integer getCode(String type) {
        for (ChangeTypeEnum enumItem : ChangeTypeEnum.values()) {
            if (type.equals(enumItem.getType())) {
                return enumItem.getCode();
            }
        }
        return null;
    }

    private Integer	code;
    private String	type;
}
