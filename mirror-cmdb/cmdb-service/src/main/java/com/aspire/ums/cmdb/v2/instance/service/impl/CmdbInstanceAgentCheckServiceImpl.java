package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceAgentCheckMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceAgentCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CmdbInstanceAgentCheckServiceImpl
 * @Description
 * @Author luowenbo
 * @Date 2020/5/25 17:40
 * @Version 1.0
 */
@Service
public class CmdbInstanceAgentCheckServiceImpl implements ICmdbInstanceAgentCheckService {
    @Autowired
    private CmdbInstanceAgentCheckMapper mapper;

    @Override
    public void insert(CmdbInstanceAgentCheck obj) {
        if(null != obj) {
            obj.setId(UUIDUtil.getUUID());
            mapper.insert(obj);
        }
    }

    @Override
    public void batchInsert(List<CmdbInstanceAgentCheck> list) {
        if(null != list && !list.isEmpty()) {
            for(CmdbInstanceAgentCheck item : list) {
                item.setId(UUIDUtil.getUUID());
            }
            mapper.batchInsert(list);
        }
    }

    @Override
    public void batchDelete(CmdbInstanceAgentCheckQuery query) {
        String[] ids = query.getIds();
        if(ids.length > 0) {
            mapper.batchDelete(ids);
        }
    }

    @Override
    public Result<CmdbInstanceAgentCheck> list(CmdbInstanceAgentCheckQuery query) {
        if(null != query.getPageNo() && null != query.getPageSize()) {
            query.setPageNo((query.getPageNo()-1)*query.getPageSize());
        }
        List<CmdbInstanceAgentCheck> data = mapper.list(query);
        int total = mapper.listByCount(query);
        return new Result<>(total,data);
    }

    @Override
    public List<CmdbInstanceAgentCheck> getCIWithAgent(String idcType) {
        return mapper.getCIWithAgent(idcType);
    }
}
