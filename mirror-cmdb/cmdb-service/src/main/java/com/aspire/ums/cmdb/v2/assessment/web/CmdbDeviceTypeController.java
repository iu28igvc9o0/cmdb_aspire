package com.aspire.ums.cmdb.v2.assessment.web;

import com.aspire.ums.cmdb.assessment.IDeviceTypeAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceTypeController
 * Author:   hangfang
 * Date:     2019/6/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbDeviceTypeController implements IDeviceTypeAPI {

    @Autowired
    private ICmdbDeviceTypeService deviceTypeService;

    /**
     * 查询所有设备类型
     */
    @Override
    public List<CmdbDeviceType> list() {
        return deviceTypeService.list();
    }
}
