package com.aspire.ums.cmdb.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: QueryEntity
 * Author:   zhu.juwang
 * Date:     2019/3/26 9:47
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryEntity {
    private String id;
    private String queryName;
    private String user;
    private String menuType;
    private String moduleId;
    private String queryInfo;
    private String insertTime;
    private String updateTime;
}
