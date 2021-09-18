package com.aspire.ums.cmdb.report.web;

import com.aspire.ums.cmdb.report.IProvinceReportTableAPI;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProvinceReportTableController
 * Author:   hangfang
 * Date:     2020/5/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
public class ProvinceReportTableController implements IProvinceReportTableAPI {

    @Autowired
    ICmdb31ProvinceReportTableService tableService;

    @Override
    public List<Cmdb31ProvinceTable> listByOwnerAndPage(@RequestParam(value = "resourceOwner", required = false) String resourceOwner,
                                                @RequestParam("type") String type) {
//        if (!"overview".equals(type) && StringUtils.isEmpty(resourceOwner)) {
//            throw new RuntimeException("未传参数[归属类型]");
//        }
        return tableService.listByOwnerAndPage(resourceOwner, type);
    }
}
