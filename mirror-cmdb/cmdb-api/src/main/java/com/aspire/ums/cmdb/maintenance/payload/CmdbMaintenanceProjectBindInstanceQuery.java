package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbMaintenanceProjectQuery
 * Author:   zhu.juwang
 * Date:     2019/7/29 23:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceProjectBindInstanceQuery {
    private Integer pageNo;
    private Integer pageSize;
    private String deviceSn;
    private String projectId;
    // bpm 调用参数 模糊查找IP
    private String ip;
}
