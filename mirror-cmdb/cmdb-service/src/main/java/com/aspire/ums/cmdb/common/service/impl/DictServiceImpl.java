package com.aspire.ums.cmdb.common.service.impl;

import com.aspire.ums.cmdb.common.entity.DictEntity;
import com.aspire.ums.cmdb.common.mapper.DictMapper;
import com.aspire.ums.cmdb.common.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DictServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/3/12 9:11
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;
    /**
     * 根据字典值编码，查询字典值列表
     * @param code 字典编码
     * @return 字典值列表
     */
    @Override
    public List<DictEntity> getDictByCode(String code) {
        return dictMapper.getDictByCode(code);
    }
}
