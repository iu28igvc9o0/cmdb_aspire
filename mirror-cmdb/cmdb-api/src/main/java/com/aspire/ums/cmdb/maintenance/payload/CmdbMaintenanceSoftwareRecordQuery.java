package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/4
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceSoftwareRecordQuery {
    private Integer pageNo;
    private Integer pageSize;
    private Date serverStartTimeBegin;
    private Date serverStartTimeEnd;
    private String project;
    private String softwareId;
    private String softwareName;
    private String company;
}
