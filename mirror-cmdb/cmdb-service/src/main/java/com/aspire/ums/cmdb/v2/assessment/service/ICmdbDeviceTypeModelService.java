package com.aspire.ums.cmdb.v2.assessment.service;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbDeviceTypeModelService
 * Author:   hangfang
 * Date:     2019/12/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbDeviceTypeModelService {


    CmdbDeviceTypeModel getDeviceTypeByModel(String deviceModel);

    List<CmdbDeviceTypeModel> getModelByDeviceType(String deviceType);

}
