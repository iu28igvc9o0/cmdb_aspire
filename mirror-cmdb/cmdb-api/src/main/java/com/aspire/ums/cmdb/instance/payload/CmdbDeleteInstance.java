package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbDeleteInstance
 * Author:   zhu.juwang
 * Date:     2020/6/11 15:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CmdbDeleteInstance {
    private List<Map<String, Object>> instanceList;
}
