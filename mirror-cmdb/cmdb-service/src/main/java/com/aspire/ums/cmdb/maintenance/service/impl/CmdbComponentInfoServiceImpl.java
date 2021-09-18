package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbComponentInfoMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbComponentInfoService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class CmdbComponentInfoServiceImpl implements ICmdbComponentInfoService {

    @Autowired
    private CmdbComponentInfoMapper cmdbComponentInfoMapper;

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void save(CmdbComponentInfo obj) {
        if(null != obj) {
            obj.setId(UUIDUtil.getUUID());
        }
        cmdbComponentInfoMapper.save(obj);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void delete(CmdbComponentInfo obj) {
        cmdbComponentInfoMapper.delete(obj);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void update(CmdbComponentInfo obj) {
        cmdbComponentInfoMapper.update(obj);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public Result<CmdbComponentInfo> list(CmdbComponentInfoQueryRequest request) {
        if (StringUtils.isNotEmpty(request.getPageNo()) && StringUtils.isNotEmpty(request.getPageSize())) {
            request.setPageNo((request.getPageNo() - 1)* request.getPageSize());
        }
        List<CmdbComponentInfo> list = cmdbComponentInfoMapper.list(request);
        Integer totalCount = cmdbComponentInfoMapper.listByCount(request);
        return new Result<>(totalCount, list);
    }
}
