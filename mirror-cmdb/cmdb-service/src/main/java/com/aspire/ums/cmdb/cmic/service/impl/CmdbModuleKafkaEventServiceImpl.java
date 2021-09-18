package com.aspire.ums.cmdb.cmic.service.impl;

import com.aspire.ums.cmdb.cmic.mapper.CmdbModuleKafkaEventMapper;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleKafkaEvent;
import com.aspire.ums.cmdb.cmic.service.CmdbModuleKafkaEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleKafkaEventServiceImpl
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbModuleKafkaEventServiceImpl implements CmdbModuleKafkaEventService {

    @Autowired
    private CmdbModuleKafkaEventMapper kafkaEventMapper;

    @Override
    public List<CmdbModuleKafkaEvent> listByModuleId(String moduleId) {
        return kafkaEventMapper.listByModuleId(moduleId);
    }
}
