package com.aspire.ums.cmdb.code.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbCodeUpdateEntity
 * Author:   zhu.juwang
 * Date:     2020/3/10 15:09
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CmdbCodeUpdateEntity {
    private CmdbCode cmdbCode;
    private Object filedValue;
}
