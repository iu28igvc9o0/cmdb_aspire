package com.aspire.ums.cmdb.ipCollect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 生成Excel的参数类.
 *
 */
@Data
@AllArgsConstructor
public class ExcelTemplateParam {

    /** 生成Excel列数据对应Map的 key. */
    private String[] keys;

    /** 生成Excel头. */
    private String[] headers;

    /** Excel sheetName */
    private String sheetName;

    /** 文件路径. */
    private String filePath;
}
