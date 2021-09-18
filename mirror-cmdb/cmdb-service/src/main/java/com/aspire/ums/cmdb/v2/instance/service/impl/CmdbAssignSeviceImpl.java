package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbAssignMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAssignSeviceImpl
 * Author:   hangfang
 * Date:     2019/11/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbAssignSeviceImpl implements ICmdbAssignService {

    @Autowired
    private CmdbAssignMapper assignMapper;

    @Override
    public List<CmdbAssign> list(CmdbAssignQuery query) {
        return assignMapper.list(query);
    }

    @Override
    public Integer listCount(CmdbAssignQuery query) {
        return assignMapper.listCount(query);
    }

    @Override
    public void save(CmdbAssign assign) {
        if (StringUtils.isEmpty(assign.getId())) {
            assign.setId(UUIDUtil.getUUID());
            assign.setCreateTime(new Date());
        }
        assignMapper.insert(assign);
    }

    @Override
    public void delete(String id) {
        assignMapper.delete(id);
    }
}
