package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsFileClassEnum.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/13 17:01
 * 版本:      v1.0
 */
@Getter
public enum OpsFileClassEnum {
    FILE_CLASS_1(1, "基线文件"), FILE_CLASS_2(2, "账号文件"), FILE_CLASS_3(3, "巡检报告文件"), FILE_CLASS_4(4, "汇总结果保存文件"), FILE_CLASS_5(5, "日志文件"),
    FILE_CLASS_6(6, "加密文件"), FILE_CLASS_9(9, "其他");

    private OpsFileClassEnum(Integer statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }

    private Integer statusCode;
    private String label;

    public static OpsFileClassEnum of(int statusCode) {
        for (OpsFileClassEnum enumItem : OpsFileClassEnum.values()) {
            if (enumItem.getStatusCode() == statusCode) {
                return enumItem;
            }
        }
        return null;
    }
}
