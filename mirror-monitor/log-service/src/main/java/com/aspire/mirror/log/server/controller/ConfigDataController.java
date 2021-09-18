package com.aspire.mirror.log.server.controller;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigDataResponse;
import com.aspire.mirror.log.api.dto.ConfigDataSearchRequest;
import com.aspire.mirror.log.api.dto.ConfigFileUploadReq;
import com.aspire.mirror.log.api.service.IConfigDataService;
import com.aspire.mirror.log.server.biz.ConfigDataBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.controller
 * 类名称:    ConfigDataController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 14:38
 * 版本:      v1.0
 */
@RestController
public class ConfigDataController implements IConfigDataService {
    Logger logger = LoggerFactory.getLogger(ConfigDataController.class);

    @Autowired
    private ConfigDataBiz configDataBiz;

    @Override
    public PageResponse<ConfigDataResponse> getConfigData(@RequestBody ConfigDataSearchRequest request) {
        if (request == null) {
            logger.error("get config data param is null");
            throw new RuntimeException("get syslog data param is null");
        }
        return configDataBiz.getConfigData(request);
    }

    @Override
    public ConfigDataResponse getConfigById(@PathVariable("index") String index, @PathVariable("id") String id) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(index)) {
            logger.error("ConfigDataController[getConfigById] param is null");
            return null;
        }
        return configDataBiz.getConfigById(index, id);
    }

    @Override
    public void uploadConfigFile(@RequestBody List<ConfigFileUploadReq> request) {
        configDataBiz.uploadConfigFile(request);
    }
}
