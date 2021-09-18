package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.aspire.ums.cmdb.common.CloudResult;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.restful.service.ICloudRestfulService;
import com.aspire.ums.cmdb.v2.restful.service.ICommonRestfulService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbSyncFiledMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbSyncFiledMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudRestfulServiceImpl
 * Author:   hangfang
 * Date:     2021/1/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CloudRestfulServiceImpl implements ICloudRestfulService {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbSyncFiledMapperService filedMapperService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private ICommonRestfulService commonRestfulService;

    @Override
    public CloudResult<Map<String, Object>> getCRCfgZYC(Map<String, Object> params) {
        return tohandleData("getCRCfgZYC", params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgFWQ(Map<String, Object> params) {
        return tohandleData("getCRCfgFWQ", params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgXNJ(Map<String, Object> params) {
        return tohandleData("getCRCfgXNJ", params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgKCC(Map<String, Object> params) {
        return tohandleData("getCRCfgKCC", params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgLJS(Map<String, Object> params) {
        return tohandleData("getCRCfgLJS", params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCBIZ(Map<String, Object> params) {
        return tohandleData("getCRCBIZ", params);
    }

    public CloudResult<Map<String, Object>> tohandleData(String apiName, Map<String, Object> params) {
        CloudResult<Map<String, Object>> cloudResult = new CloudResult<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        String msg = "成功";
        String returnCode="200";
        try {
            StatisticRequestEntity statisticRequestEntity = new StatisticRequestEntity();
            statisticRequestEntity.setName(String.format(Constants.CLOUD_SQL_PREFIX, apiName));
            statisticRequestEntity.setParams(params);
            dataList = (List<Map<String, Object>>)commonRestfulService.getInstanceStatistics(statisticRequestEntity);
        } catch (Exception e) {
            log.info("获取数据失败.error:{}", e.getMessage());
            msg = "失败，error:" + e.getMessage();
            returnCode = "500";
        }
        cloudResult.setRcd(dataList);
        cloudResult.setReturnmsg(msg);
        cloudResult.setReturncode(returnCode);
        return cloudResult;
    }
}
