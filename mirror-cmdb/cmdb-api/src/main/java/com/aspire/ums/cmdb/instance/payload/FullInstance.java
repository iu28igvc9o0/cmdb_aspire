package com.aspire.ums.cmdb.instance.payload;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FullInstance
 * Author:   zhu.juwang
 * Date:     2019/5/21 15:03
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullInstance {
    private Module module;
    private String tableName;
    private List<CmdbCode> codeList;
    private List<ColumnInfo> instanceColumns;
    private List<ColumnInfo> otherColumns;
}
