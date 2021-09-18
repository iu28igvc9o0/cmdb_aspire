package com.aspire.ums.cmdb.v2.restful.service;

import com.aspire.ums.cmdb.common.CloudResult;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICloudRestfulService
 * Author:   hangfang
 * Date:     2021/1/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICloudRestfulService {

    CloudResult<Map<String, Object>> getCRCfgZYC(Map<String, Object> params);

    CloudResult<Map<String, Object>> getCRCfgFWQ(Map<String, Object> params) ;

    CloudResult<Map<String, Object>> getCRCfgXNJ(Map<String, Object> params) ;

    CloudResult<Map<String, Object>> getCRCfgKCC(Map<String, Object> params);

    CloudResult<Map<String, Object>> getCRCfgLJS(Map<String, Object> params) ;

    CloudResult<Map<String, Object>> getCRCBIZ(Map<String, Object> params);


}
