package com.aspire.ums.cmdb.instance.payload;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbInstanceQuery extends GeneralAuthConstraintsAware {

    private CmdbV3CondicationSetting condication;

    private Map<String, List<Map<String, Object>>> queryParams;

    private Integer pageSize;

    private Integer pageNumber;

    private String moduleId;

}
