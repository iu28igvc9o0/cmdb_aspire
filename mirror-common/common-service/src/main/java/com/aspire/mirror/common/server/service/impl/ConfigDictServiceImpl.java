package com.aspire.mirror.common.server.service.impl;

import com.aspire.mirror.common.api.dto.model.ConfigDict;
import com.aspire.mirror.common.server.dao.ConfigDictMapper;
import com.aspire.mirror.common.server.service.ConfigDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectController
 * Author:   ws
 * Date:     2019/4/1
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Service
@Transactional
public class ConfigDictServiceImpl implements ConfigDictService {

    @Autowired
    private ConfigDictMapper configDictMapper;


    @Override
    @SuppressWarnings("ALL")
    public List<ConfigDict> selectDictsByType(String type, String pid, String pValue, String pType) {
        List<ConfigDict> resultList = null;
        if (StringUtils.isEmpty(type)) {
            return resultList;
        }

        resultList = configDictMapper.selectDictsByType(type, pid, pValue, pType);
        return resultList;
    }
}
