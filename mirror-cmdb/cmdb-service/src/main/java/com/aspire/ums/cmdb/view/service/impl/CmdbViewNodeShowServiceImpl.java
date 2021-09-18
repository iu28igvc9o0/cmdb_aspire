package com.aspire.ums.cmdb.view.service.impl;

import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.view.mapper.CmdbViewNodeShowMapper;
import com.aspire.ums.cmdb.view.payload.CmdbViewNodeShow;
import com.aspire.ums.cmdb.view.service.ICmdbViewNodeShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeShowServiceImpl
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbViewNodeShowServiceImpl implements ICmdbViewNodeShowService {

    @Autowired
    private CmdbViewNodeShowMapper nodeShowMapper;
    @Override
    public void saveByBatch(List<CmdbViewNodeShow> nodeShowList) {
        nodeShowMapper.saveByBatch(nodeShowList);
    }

    @Override
    public void deleteByNodeId(String nodeId) {
        nodeShowMapper.deleteByNodeId(nodeId);
    }

    @Override
    public void deleteByViewId(String viewId) {
        nodeShowMapper.deleteByViewId(viewId);
    }
}
