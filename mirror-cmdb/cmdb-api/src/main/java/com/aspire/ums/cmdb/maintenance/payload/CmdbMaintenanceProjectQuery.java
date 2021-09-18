package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CmdbMaintenanceProjectQuery {
    private Integer pageNo;
    private Integer pageSize;
    private String serviceStartTime;
    private String serviceEndTime;
    // 维保项目名称
    private String projectName;
    // 维保项目编号
    private String projectNo;
    // 维保厂家名称
    private String maintenProduce;
    // 合同厂家名称
    private String contractProduce;
    // 维保对象类型 硬件|软件
    private String maintenanceProjectType;
    // 与服务结束时间的间隔时间
    private Integer intervalTime;
}
